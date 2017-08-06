package businfo.busstop;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MPKWroclaw extends BusInfo {
    public MPKWroclaw(String html) throws IOException {
        super(html);
        this.checkColumnNames(this.columnNames);
    }

    @Override
    public String getRawResult(Connection connection) throws IOException {
        StringBuilder result = new StringBuilder();
        Document document = connection.get();

        // line number
        result.append(document.select("table[class='table table-bordered table-schedule hide-to-print'] thead tr td h2 a").text());

        // vehicle type
        String vehicle = document.select("table[class='table table-bordered table-schedule hide-to-print'] thead tr td[colspan='2'] h2").text();
        if(vehicle.contains("Tramwaj") || vehicle.contains("tramwaj")){
            vehicle = "Light train";
        } else if (vehicle.contains("Autobus") || vehicle.contains("bus")){
            vehicle = "Bus";
        } else {
            vehicle = "undefined";
        }
        result.append("\n").append(vehicle);

        // bus stop name
        result.append("\n").append(
                document.select("table[class='table table-bordered table-schedule hide-to-print'] tbody tr td a[class='btn btn-primary']").text()
        );

        // column names
        Elements columns = document.select("table[class='table table-bordered table-schedule table-departures'] thead tr td b");
        result.append("\n");
        for(Element column : columns){
            result.append(column.text()).append("\t");
        }
        result.append("\n");

        // schedule
        Elements hourTables = document.select("table[class='table table-bordered table-schedule table-departures'] tbody tr td table[class='table-hours']");
        HashMap<Integer, ArrayList<String>> hours = new HashMap<>();
        for(Element table : hourTables){
            Elements rows = table.select("tbody tr");
            for(Element row : rows){
                Element hourCell = row.select("td b").first();
                int hour = Integer.parseInt(hourCell.text());

                // after storing hour - delete cell
                row.select("td b").remove();
                Elements cells = row.select("td");
                ArrayList<String> colMins = hours.get(hour);
                if(colMins == null || colMins.isEmpty()){
                    colMins = new ArrayList<String>();
                }
                String colText = "";
                for(Element cell : cells){
                    if(cell.text() != null || cell.text().isEmpty()) {
                        colText += cell.text() + " ";
                    }
                }
                colMins.add(colText);
                hours.put(hour, colMins);
            }
        }

        // now save all from map to string
        ArrayList<Integer> sortedKeys = new ArrayList<>();
        sortedKeys.addAll(hours.keySet());
        Collections.sort(sortedKeys);
        for(int hour : sortedKeys){
            result.append(hour).append("\t");
            ArrayList<String> minuteColumns = hours.get(hour);
            for(String minuteString : minuteColumns){
                result.append(minuteString).append("\t");
            }
            result.append("\n");
        }

        // additional info
        result.append("undefined");

        return result.toString();
    }

    @Override
    public String getRawHtml() throws IOException {
        Document document = this.jsoupConnection.get();
        Element table = document.select("table[class='table table-bordered table-schedule table-departures']").first();
        Element head = document.select("head").first();
        StringBuilder result = new StringBuilder();
        result.append("<!DOCTYPE html><html>");
        // result.append(head);
        result.append("<head></head>");
        result.append("<body>");
        result.append(table);
        result.append("</body></html>");
        return result.toString();
    }
}
