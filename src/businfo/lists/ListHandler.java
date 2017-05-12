package businfo.lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import businfo.busstop.*;


/**
 * Created by umat on 02.05.17.
 */
abstract class ListHandler {
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
        Map<Integer, BusInfo> map = new TreeMap<>();
        for(BusInfo busInfo : allBusInfos){
            if(!map.containsKey(busInfo.getLineNumber())){  // if line number is not yet in the map:
                map.put(busInfo.getLineNumber(), busInfo);  // store it in the map
            } else {                                        // else
                int key = busInfo.getLineNumber();          // check amounts of courses and always save the smallest amount
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
        return purifiedInfos;               // and return it
    }

    public String excelFormattedText(){
        int counter=0;
        int WeekdayCount=0;
        int WeekendCount=0;
        StringBuilder result = new StringBuilder();

        result
                .append("\t")
                .append("Line no.").append("\t")
                .append("Stop Name").append("\t")
                .append("Light train / Bus").append("\t")
                .append("Distance from building").append("\t") // empty cell for distance from building
                .append("Number of rides").append("\t")
                .append("\n")
                .append("\t\t\t\t\t")
                .append("Weekday").append("\t")
                .append("Weekday").append("\t")
                .append("Weekend").append("\t")
                .append("Average");


        for(BusInfo busInfo : this.busInfosPurified) {
            if(counter==0 && busInfo.getLineNumber()>99 ){
                result.append("\n\n")
                        .append("\t\t\t\t\t\t\t")
                        .append("Total")
                        .append("\n\t\t\t\t\t\t\t")
                        .append("Weekday").append("\t")
                        .append("Weekend").append("\t")
                        .append("\n\t\t\t\t\t\t\t")
                        .append(WeekdayCount).append("\t")
                        .append(WeekendCount).append("\n");
                        WeekdayCount=0;
                        WeekendCount=0;
                result.append("\n\n")
                        .append("\t")
                        .append("Line no.").append("\t")
                        .append("Stop Name").append("\t")
                        .append("Light train / Bus").append("\t")
                        .append("Distance from building").append("\t") // empty cell for distance from building
                        .append("Number of rides").append("\t")
                        .append("\n")
                        .append("\t\t\t\t\t")
                        .append("Weekday").append("\t")
                        .append("Weekday").append("\t")
                        .append("Weekend").append("\t")
                        .append("Average");
            counter++;
            }

            result
                    .append("\n")
                    .append("\t")
                    .append(busInfo.getLineNumber()).append("\t")
                    .append(busInfo.getStreetName()).append("\t")
                    .append(busInfo.getVehicleType()).append("\t")
                    .append("\t") // empty cell for distance from building
                    .append(busInfo.getWeekdayCourseCount()).append("\t")
                    .append(busInfo.getSaturdayCourseCount()).append("\t")
                    .append(busInfo.getSundayCourseCount()).append("\t")
                    .append((busInfo.getSaturdayCourseCount() + busInfo.getSundayCourseCount()) / 2); // weekend average

                    WeekdayCount+=busInfo.getWeekdayCourseCount();
                    WeekendCount+=((busInfo.getSaturdayCourseCount() + busInfo.getSundayCourseCount()) / 2);

            if (!busInfo.checkColumnNames(busInfo.getColumnNames()) || busInfo.getWarnings().isEmpty()) {
                result.append("\t");
                for (String warning : busInfo.getWarnings()) {
                    result.append("\t").append(warning);
                    break;
                }
            }

        }
        result.append("\n\n")
                .append("\t\t\t\t\t\t\t")
                .append("Total")
                .append("\n\t\t\t\t\t\t\t")
                .append("Weekday").append("\t")
                .append("Weekend").append("\t")
                .append("\n\t\t\t\t\t\t\t")
                .append(WeekdayCount).append("\t")
                .append(WeekendCount).append("\n");

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
