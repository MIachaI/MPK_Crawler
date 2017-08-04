package businfo.lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import businfo.busstop.*;
import businfo.busstop.lines.LineOnStop;
import businfo.busstop.streets.BusStop;

/**
 * Created by umat on 02.05.17.
 */
public class SelectedBusStopsHandler {
    protected ArrayList<BusInfo> busInfos;
    protected ArrayList<String> linkList;
    protected ArrayList<BusInfo> busInfosPurified;

    public SelectedBusStopsHandler(){
        this.busInfos = new ArrayList<>();
        this.linkList = new ArrayList<>();
        this.busInfosPurified = new ArrayList<>();
    }
    @Deprecated
    public SelectedBusStopsHandler(ArrayList<BusStop> selectedBusStops){
        this();
        for(BusStop stop : selectedBusStops){
            for(LineOnStop line : stop.getBusLines()){
                this.linkList.add(line.getLink());
            }
        }
    }

    /**
     * Prepare object for counting
     * Remember: after adding new city always update switch statement of this constructor
     * @param city in which bus stops are
     * @param selectedBusStops chosen to analyses
     * @throws Exception when incorrect city name provided
     */
    public SelectedBusStopsHandler(String city, ArrayList<BusStop> selectedBusStops) throws Exception {
        this();
        for(BusStop stop : selectedBusStops){
            for(LineOnStop line : stop.getBusLines()){
                this.linkList.add(line.getLink());
            }
        }

        switch(city.toLowerCase()){
            case "kraków":
            case "krakow":
            case "cracow":{
                for(String link : this.linkList){
                    busInfos.add(new MPKinfo(link));
                }
            }
            break;
            case "warszawa":
            case "warsaw": {
                for(String link : this.linkList){
                    busInfos.add(new ZTMinfo(link));
                }
            }
            break;
            case "poznan":
                throw new Exception("Not yet implemented");
                // break;
            case "wrocław":
            case "wroclaw":
            case "breslau":
                for(String link: this.linkList){
                    busInfos.add(new MPKWroclaw(link));
                }
            break;
            default:
                throw new Exception("Invalid city");
        }

        // after adding all BusInfos - purify the list
        this.busInfosPurified = purifyList(this.busInfos);
    }

    @Deprecated
    protected ArrayList<String> findLinks(String html) throws IOException{
        return null;
    }

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
