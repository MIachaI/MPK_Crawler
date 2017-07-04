package businfo.busstop;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * MPK Poznan main page: http://www.mpk.poznan.pl/
 */
public class MPKPoznanInfo extends BusInfo {
    public MPKPoznanInfo(){
        super();
    }

    public MPKPoznanInfo(String html) throws IOException {
        super(html);
        // checkColumnNames(this.columnNames);
    }

    public String getRawResult(Connection connection) throws IOException{
        StringBuilder result = new StringBuilder();
        Document document = connection.get();

        // line number
        result.append(document.select("div[class='MpkLineNum'] a[class='MpkLineLink']").text());
        // vehicle type
        Element vehicleType = document.select("img[class='MpkZoneImgBig']").first();
        if (Objects.equals(vehicleType.attr("title"), "autobus"))
            result.append("\nbus");
        else if (Objects.equals(vehicleType.attr("title"),"tramwaj"))
            result.append("\ntram");
        else
            result.append("\nundefined");

        // bus stop name
        result.append("\n").append(document.select("strong[class='link_blue']").text());

        // column titles
        result.append("\n");
        Elements columnTitles = document.select("table[class='timetable'] tbody tr th[colspan='2']");
        for (Element columnTitle : columnTitles){
            result.append(columnTitle.text()).append("\t");
        }

        // timetable
        Elements rows = document.select("tr[class='MpkTimetableRow'] ");
        for(Element row : rows){
            String rowHour = row.select("td[class='MpkHours'").first().text();
            result.append("\n").append(rowHour);

            Elements colMinutes = row.select("td[class='MpkMinutes'");
            for(Element column : colMinutes){
                result.append("\t").append(column.text());
            }
        }

        // additional info
        Elements additionalInfo = document.select("div[class='module orange'] ul li");
        result.append("\n");
        for(Element info : additionalInfo){
            result.append(info.text()).append("\t");
        }
        return result.toString();
    }
}