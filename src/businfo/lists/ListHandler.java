package businfo.lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import businfo.busstop.*;

/**
 * Created by umat on 02.05.17.
 */
public abstract class ListHandler {
    protected ArrayList<BusInfo> busInfos;
    protected ArrayList<String> linkList;
    protected ArrayList<BusInfo> busInfosPurified;

    public ListHandler(){
        this.busInfos = new ArrayList<>();
        this.linkList = new ArrayList<>();
        this.busInfosPurified = new ArrayList<>();
    }

    protected abstract ArrayList<String> findLinks(String html) throws IOException;

    //GETTERS
    public ArrayList<String> getLinkList(){
        return this.linkList;
    }
    public ArrayList<BusInfo> getBusInfos(){
        return this.busInfos;
    }
    public ArrayList<BusInfo> getBusInfosPurified(){
        return this.busInfosPurified;
    }

    /**
     * Clear busInfo from redundant BusInfos objects.
     * Choose the most pesimistic case (by weekday).
     * @param allBusInfos list to clear
     * @return sanitized BusInfo list
     */
    protected ArrayList<BusInfo> purifyList(ArrayList<BusInfo> allBusInfos){
        ArrayList<BusInfo> purifiedInfos = new ArrayList<>();
        Map<String, BusInfo> map = new TreeMap<>();
        for(BusInfo busInfo : allBusInfos){
            if(!map.containsKey(busInfo.getLineNumberString())){  // if line number is not yet in the map:
                map.put(busInfo.getLineNumberString(), busInfo);  // store it in the map
            } else {                                        // else
                String key = busInfo.getLineNumberString();          // check amounts of courses and always save the smallest amount
                if(map.get(key).getWeekdayCourseCount() > busInfo.getWeekdayCourseCount()){
                    BusInfo buffer = map.get(key);
                    buffer.setWeekdayList(busInfo.getWeekdayList());
                    map.put(key,buffer);
                }
                if(map.get(key).getSaturdayCourseCount() + map.get(key).getSundayCourseCount() > busInfo.getSaturdayCourseCount() + busInfo.getSundayCourseCount()){
                    BusInfo buffer = map.get(key);
                    buffer.setSaturdayList(busInfo.getSaturdayList());
                    buffer.setSundayList(busInfo.getSundayList());
                    map.put(key,buffer);
                }
            }
        }
        purifiedInfos.addAll(map.values()); // copy results to ArrayList
        purifiedInfos.sort(Comparator.comparingInt(BusInfo::getLineNumber)); // sort'em
        return purifiedInfos;               // and return
    }

    public String excelFormattedText(){
        // just to handle single row (title, busInfo and summary)
        class Row{
            private String title(){
                return
                        "Line no." + "\t"
                        + "Stop Name" + "\t"
                        + "Light train / Bus" + "\t"
                        + "Distance from building" + "\t"
                        + "Number of rides" + "\t" + "\n" + "\t\t\t\t"
                        + "Weekday" + "\t"
                        + "Weekend" + "\t"
                        + "Weekend" + "\t"
                        + "Average";
            }

            private String busInfo(BusInfo busInfo){
                StringBuilder result = new StringBuilder();
                result
                        .append("\n")
                        .append(busInfo.getLineNumberString()).append("\t")
                        .append(busInfo.getStreetName()).append("\t")
                        .append(busInfo.getVehicleType()).append("\t")
                        .append("\t") // empty cell for distance from building
                        .append(busInfo.getWeekdayCourseCount()).append("\t")
                        .append(busInfo.getSaturdayCourseCount()).append("\t")
                        .append(busInfo.getSundayCourseCount()).append("\t")
                        .append((busInfo.getSaturdayCourseCount() + busInfo.getSundayCourseCount()) / 2); // weekend average

                if (!busInfo.getWarnings().isEmpty()) {
                    result.append("\t");
                    for (String warning : busInfo.getWarnings()) {
                        result.append("\t").append(warning);
                    }
                }

                return result.toString();
            }

            private String summary(int weekdaySum, int weekendSum){
                return
                        "\n\n"
                        + "\t\t\t\t\t\t"
                        + "Total" + "\n\t\t\t\t\t\t"
                        + "Weekday" + "\t"
                        + "Weekend" + "\t"
                        + "\n\t\t\t\t\t\t"
                        + weekdaySum + "\t"
                        + weekendSum + "\n";
            }
        }
        Row row = new Row();

        // create separate lists for lighttrain and bus
        ArrayList<BusInfo> trams = new ArrayList<>();
        ArrayList<BusInfo> buses = new ArrayList<>();
        int tramWeekdaySum = 0, tramWeekendAvgSum = 0;
        int busWeekdaySum = 0, busWeekendAvgSum = 0;

        StringBuilder result = new StringBuilder();

        // save busInfos into corresponding lists and count courses sums
        for(BusInfo busInfo : this.getBusInfosPurified()){
            if(busInfo.getVehicleType().equals("Light train")) {
                trams.add(busInfo);
                tramWeekdaySum += busInfo.getWeekdayCourseCount();
                tramWeekendAvgSum += (busInfo.getSaturdayCourseCount() + busInfo.getSundayCourseCount())/2;
            }
            else if (busInfo.getVehicleType().equals("Bus") || busInfo.getVehicleType().equals("undefined")){
                buses.add(busInfo);
                busWeekdaySum += busInfo.getWeekdayCourseCount();
                busWeekendAvgSum += (busInfo.getSaturdayCourseCount() + busInfo.getSundayCourseCount())/2;
            }
        }

        // display trams (if list not empty)
        if (!trams.isEmpty()) {
            result.append(row.title());
            for(BusInfo tramInfo : trams) {
                result.append(row.busInfo(tramInfo));
            }
            result.append(row.summary(tramWeekdaySum,tramWeekendAvgSum));
        }

        // display buses (if list not empty)
        if(!buses.isEmpty()){
            result.append(row.title());
            for(BusInfo busInf : buses) {
                result.append(row.busInfo(busInf));
            }
            result.append(row.summary(busWeekdaySum,busWeekendAvgSum));
        }

        return result.toString();
    }

    public String toString(){
        StringBuilder result = new StringBuilder();
        for(BusInfo info : this.busInfos){
            result.append(info);
        }
        result.append("\n====Purified====\n");
        for (BusInfo info : this.busInfosPurified) result.append(info);
        return result.toString();
    }
}
