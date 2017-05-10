import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


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
        Map<Integer,BusInfo> map = new TreeMap<>();
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
        StringBuilder result = new StringBuilder();
        for(BusInfo busInfo : this.busInfosPurified) {
            result
                    .append(busInfo.getLineNumber()).append("\t")
                    .append(busInfo.getStreetName()).append("\t")
                    .append(busInfo.getVehicleType()).append("\t")
                    .append("\t") // empty cell for distance from building
                    .append(busInfo.getWeekdayCourseCount()).append("\t")
                    .append(busInfo.getSaturdayCourseCount()).append("\t")
                    .append(busInfo.getSundayCourseCount()).append("\t")
                    .append((busInfo.getSaturdayCourseCount() + busInfo.getSundayCourseCount()) / 2); // weekend average
            if (!busInfo.checkColumnNames(busInfo.getColumnNames())) {
                for (String warning : busInfo.getWarnings()) {
                    result.append("\t").append(warning);
                }
            }
            result.append("\n");
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
