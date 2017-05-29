package businfo.busstop;

import java.io.IOException;
import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by umat on 11.05.17.
 */
public class ZTMinfo extends BusInfo {
    private String innerHtml;
    public ZTMinfo(){
        super();
    }
    public ZTMinfo(String html) throws IOException {
        super(html);
        checkColumnNames(this.columnNames);
    }

    @Override
    public String getRawResult(Connection connection) throws IOException {
        StringBuilder result = new StringBuilder();
        Document document = connection.get();

        // condition when schedule for weekend is empty - jump to weekday
        String emptyWarning;
        emptyWarning = document.select("div[id='PrzystanekRozklad']").text();
        if(emptyWarning.equals("Niestety, w tym dniu linia nie kursuje. Aby przejrzeć rozkłady jazdy tej linii, przejdź do rozkładów na inne dni.")){
            // find next day on schedule
            Elements links = document.select("div[id='RozkladyJazdyTopDays'] a[class='textmore']");
            String link = links.first().attr("href");
            // recursive search next stops
            return getRawResult(Jsoup.connect("http://www.ztm.waw.pl/" + link));
        }
        else if(emptyWarning.equals("Niestety, zadana data jest zbyt odległa.")){
            this.addWarning("Brak rozkładu jazdy dla tego przystanku");
            return "0"+"\nundefined"+"\nbłąd"+"\nbrak"+"\nundefined";
        }

        // first, find "Print timetable for all days" link
        Elements additionalLinks = document.select("div[class='LinkiDodatkowe'] a");

        String scheduleLink = "";
        for(Element link : additionalLinks){
            if (link.text().equals("Drukuj rozkład na wszystkie dni") || link.text().equals("Print timetable for all days")){
                scheduleLink = "http://www.ztm.waw.pl/" + link.attr("href");
                this.innerHtml = scheduleLink;
                additionalLinks = null; // free memory (hopefully)
                break;
            }
        }
        if(scheduleLink.isEmpty()) this.addWarning("Nie znaleziono linku z rozkładem na cały tydzień");

        // now we have schedule for whole week - get info from it
        document = Jsoup.connect(scheduleLink).get();

        // line number
        Elements lineNumber = document.select("h4 span");
        result.append(lineNumber.text()).append("\n");

        // vehicle type
        String vehicleType;
        try{
            vehicleType = document.select("img[class='busico']").first().attr("src");
            if(vehicleType.contains("ico_tram")) vehicleType = "Light train";
            else if(vehicleType.contains("ico_bus")) vehicleType = "Bus";
            else vehicleType = "undefined";
        } catch (NullPointerException e){
            vehicleType = "undefined";
        }
        result.append(vehicleType).append("\n");

        // stop name
        String stopName = document.select("div[id='PrzystanekRozklad'] p strong").first().text();
        result.append(stopName).append("\n");

        // column titles
        Elements columnNames = document.select("table[id='maintable'] p[class='RozkladObowiazuje']");
        for(Element columnName : columnNames){
            result.append(columnName.text()).append("\t");
        }
        result.append("\n");

        Elements scheduleTables = document.select("td[class='rtc']");
        scheduleTables.add(document.select("td[class='rtc last']").first());

        Map<Integer, String> hourMinutes = new TreeMap<>(); // stores hour as a key, next minutes - divided by spaces (inside one column) and with tabulations when column ends
        for(int i = 0 ; i < 24; i++){
            hourMinutes.put(i, "");
        }
        // TODO probably one unnecessary for loop
        for (Element table : scheduleTables){
            Elements subTables = table.select("table");
            for(Element subTable : subTables) {
                Elements subTableRows = subTable.select("tr");
                subTableRows.remove(0); // remove first row (it only says "Minutes")
                Map<Integer,String> prevTableHourMinute = new HashMap<>(hourMinutes);
                for(Element subTableRow : subTableRows){
                    int hour = Integer.parseInt(subTableRow.getElementsByClass("gd").first().text());
                    String minutes = subTableRow.getElementsByClass("nr").text();
                    String existing = hourMinutes.get(hour);
                    hourMinutes.put(hour, existing + minutes + "\t");
                }

                for(Integer hour : hourMinutes.keySet()){
                    if (Objects.equals(hourMinutes.get(hour), prevTableHourMinute.get(hour))) { // if column was empty - add tab
                        String value = hourMinutes.get(hour);
                        hourMinutes.put(hour, value + "\t");
                    }
                }
            }
        }

        for (Integer hour : hourMinutes.keySet()){
            result
                    .append(hour).append("\t")
                    .append(hourMinutes.get(hour)).append("\t") // gets all minutes
                    .append("\n");
        }

        // last line
        result.append("undefined");

        return result.toString();
    }

    public String getRawHtml() throws IOException {
        StringBuilder result = new StringBuilder();
        if(this.innerHtml.isEmpty())
            return "Błąd";

        Connection connection = Jsoup.connect(this.innerHtml);
        Document document = connection.get();
        Elements scripts = document.select("script");
        Element img = document.select("img[alt='Ikona'][class='busico']").first();
        String imgLink = img.attr("src");
        // imgLink now is in relative path - transform it into absolute path
        imgLink = "http://www.ztm.waw.pl" + imgLink.substring(1);
        // now paste in proper adress
        img.attr("src", imgLink);
        document.select("meta[http-equiv]").remove();
        // "rtc last" class was causing table borders not to display - so just change it to "rtc"
        document.select("td[class='rtc last']").attr("class","rtc");
        scripts.remove();
        result.append(document.outerHtml());
        //result.append(imgLink);
        return result.toString();
    }

    public boolean checkColumnNames(ArrayList<String> columnNames){

        if (columnNames.size() ==  3 && columnNames.get(0).equals("Dzień Powszedni")
                && columnNames.get(1).equals("Sobota")
                && columnNames.get(2).equals("Święto")){
            return true;
        }
        else if(columnNames.size() == 2 &&  columnNames.get(0).equals("Dzień Powszedni")
                && columnNames.get(1).equals("Święto")){ // copy saturdayCourses to sundayCourses
            this.setSundayList(this.getSaturdayList());
            warnings.add("Dwie kolumny: \"Dzień Powszedni\" i \"Święto\". Rozkład z kolumny \"Święto\" przepisano dla sobót i niedziel\t" + this.innerHtml);
            return true;
        }
        else if (columnNames.size() == 1 && columnNames.get(0).equals("Dzień Powszedni")){
            return true;
        } else {
            warnings.add("Niestandardowe rozłożenie kolumn. Sprawdź przystanek " +"\t"+ this.innerHtml);
        }
        return false;
    }
}
