package businfo.busstop;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import window_interface.dialogs.AlertBox;
import window_interface.dialogs.ErrorDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * MPK Poznan main page: http://www.mpk.poznan.pl/
 */
@Deprecated
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
        if (vehicleType.attr("title").contains("autobus"))
            result.append("\nBus");
        else if (vehicleType.attr("title").contains("tramwaj"))
            result.append("\nLight train");
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
            try {
                String rowHour = row.select("td[class='MpkHours'").first().text();
                result.append("\n").append(rowHour);

                Elements colMinutes = row.select("td[class='MpkMinutes'");
                for (Element column : colMinutes) {
                    result.append("\t").append(column.text());
                }
            }
            catch (NullPointerException e){
                AlertBox.display("Jeden z wybranych przystanków nie istnieje!","Link do tego przystanku: " +html+"\nW razie wątpliwości napisz do wsparcia technicznego.");
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

    @Override
    public String getRawHtml() throws IOException {
        StringBuilder result = new StringBuilder();
        Document document = this.jsoupConnection.get();
        Element head = document.select("head").first();
        Element table = document.select("div[id='MpkBoard']").first();

        result.append("<html>");
//        result.append(head.outerHtml());        // append body section
        result.append(table.outerHtml());		// append table section
        result.append("</html>");
        return result.toString();
    }

    @Override
    public boolean checkColumnNames(ArrayList<String> columnNames) {
        if (
                columnNames.size() == 3
                && Objects.equals(columnNames.get(0), "Dni robocze")
                && Objects.equals(columnNames.get(1), "Soboty")
                && Objects.equals(columnNames.get(2), "Święta"))
            return true;
        else {
            this.warnings.add("Niestandardowe nazwy kolumn. Sprawdź przystanek \t" + this.html);
            return false;
        }
    }
}