package businfo.busstop;

import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by umat on 11.05.17.
 */
public class ZTMinfo extends BusInfo {
    public ZTMinfo(){
        super();
    }
    public ZTMinfo(String html) throws IOException {
        super(html);
        checkColumnNames(this.columnNames);
    }

    @Override
    public String getRawResult(String html) throws IOException {
        StringBuilder result = new StringBuilder();
        Document document = Jsoup.connect(html).get();

        // first, find "Print timetable for all days" link
        Elements additionalLinks = document.select("div[class='LinkiDodatkowe'] a");

        String scheduleLink = "";
        for(Element link : additionalLinks){
            if (link.text().equals("Drukuj rozkład na wszystkie dni") || link.text().equals("Print timetable for all days")){
                scheduleLink = "http://www.ztm.waw.pl/" + link.attr("href");
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
        String vehicleType = document.select("img[class='busico']").first().attr("src");
        if(vehicleType.contains("ico_tram")) vehicleType = "Light train";
        else if(vehicleType.contains("ico_bus")) vehicleType = "Bus";
        else vehicleType = "undefined";
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
        for (Element table : scheduleTables){
            Elements subTables = table.select("table");
            for(Element subTable : subTables) {
                Elements subTableRows = subTable.select("tr");
                subTableRows.remove(0); // remove first row (it only says "Minutes")
                for(Element subTableRow : subTableRows){
                    int hour = Integer.parseInt(subTableRow.getElementsByClass("gd").first().text());
                    String minutes = subTableRow.getElementsByClass("nr").text();

                    String existing = hourMinutes.get(hour);
                    hourMinutes.put(hour, existing == null ? minutes + "\t" : existing + minutes + "\t");
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

    public boolean checkColumnNames(ArrayList<String> columnNames){
        if(columnNames.get(0).equals("Dzień Powszedni")
                && columnNames.get(1).equals("Święto")){ // copy saturdayCourses to sundayCourses
            this.setSundayList(this.getSaturdayList());
            warnings.add("Dwie kolumny: \"Dzień Powszedni\" i \"Święto\". Rozkład z kolumny \"Święto\" przepisano dla sobót i niedziel");
            return true;
        }
        else if (columnNames.get(0).equals("Dzień Powszedni")
                && columnNames.get(1).equals("Sobota")
                && columnNames.get(2).equals("Święto")){
                    return true;
        }
        else if (columnNames.size() == 1 && columnNames.get(0).equals("Dzień powszedni")){
            return true;
        }
        else if (columnNames.size() != 3 ){
            warnings.add("Niestandardowe nazwy kolumn. Sprawdź przystanek " + this.html);
            return false;
        } else {
            warnings.add("Niestandardowe rozłożenie kolumn. Sprawdź przystanek " + this.html);
            return false;
        }
    }
}
