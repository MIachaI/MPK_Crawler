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

    public ArrayList<BusInfo> getBusInfosNonPurified(){
        return this.busInfos;
    }

    /**
     * Get only information about light trains from purified list
     * @return list of found light train courses
     */
    public ArrayList<BusInfo> getOnlyTrams(){
        ArrayList<BusInfo> result = new ArrayList<>();
        for(BusInfo tramInfo : this.busInfosPurified){
            if(tramInfo.getVehicleType().equals("Light train")){
                result.add(tramInfo);
            }
        }
        return result;
    }
    /**
     * Get only information about buses from purified list
     * @return list of found bus courses
     */
    public ArrayList<BusInfo> getOnlyBuses(){
        ArrayList<BusInfo> result = new ArrayList<>();
        for(BusInfo busInfo : this.busInfosPurified){
            if(busInfo.getVehicleType().equals("Bus") || busInfo.getVehicleType().equals("undefined")){
                result.add(busInfo);
            }
        }
        return result;
    }

    /**
     * Get only information about light trains from non-purified list
     * @return list of found light train courses
     */
    public ArrayList<BusInfo> getOnlyTramsNonPurified(){
        ArrayList<BusInfo> result = new ArrayList<>();
        for(BusInfo tramInfo : this.busInfos){
            if(tramInfo.getVehicleType().equals("Light train")){
                result.add(tramInfo);
            }
        }
        return result;
    }
    /**
     * Get only information about buses from non-purified list
     * @return list of found bus courses
     */
    public ArrayList<BusInfo> getOnlyBusesNonPurified(){
        ArrayList<BusInfo> result = new ArrayList<>();
        for(BusInfo busInfo : this.busInfos){
            if(busInfo.getVehicleType().equals("Bus") || busInfo.getVehicleType().equals("undefined")){
                result.add(busInfo);
            }
        }
        return result;
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
