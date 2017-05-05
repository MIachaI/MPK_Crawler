import java.io.IOException;
import java.util.ArrayList;

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
    }

    protected abstract ArrayList<String> getLinks(String html) throws IOException;

    public ArrayList<String> getLinkList(){
        return this.linkList;
    }

    /**
     * Clear busInfo from redundant BusInfos objects.
     * Choose the most pesimistic case (by weekday).
     * @param allBusInfos list to clear
     * @return sanitized BusInfo list
     */
    protected ArrayList<BusInfo> purifyList(ArrayList<BusInfo> allBusInfos){
        // @TODO implement purify list
        ArrayList<BusInfo> purifiedInfos = new ArrayList<>();
        for(BusInfo busInfo : busInfos){
            if(purifiedInfos.isEmpty()) purifiedInfos.add(busInfo);

        }
        return allBusInfos;
    }

    public String excelFormattedText(){
        String result = "";
        for(BusInfo busInfo : this.busInfos){
            result += busInfo.getLineNumber() +
                    "\t" + busInfo.getStreetName() +
                    "\t" + busInfo.getVehicleType() +
                    "\t" +                                      // empty cell for distance from building
                    "\t" + busInfo.getWeekdayCourseCount() +
                    "\t" + busInfo.getSaturdayCourseCount() +
                    "\t" + busInfo.getSundayCourseCount() +
                    "\t" + (busInfo.getSaturdayCourseCount() + busInfo.getSundayCourseCount())/2 + "\n"; // weekend average

        }
        result+="------------------\nPurified\n------------------";
        for(BusInfo busInfo : this.busInfosPurified){
            result += busInfo.getLineNumber() +
                    "\t" + busInfo.getStreetName() +
                    "\t" + busInfo.getVehicleType() +
                    "\t" +                                      // empty cell for distance from building
                    "\t" + busInfo.getWeekdayCourseCount() +
                    "\t" + busInfo.getSaturdayCourseCount() +
                    "\t" + busInfo.getSundayCourseCount() +
                    "\t" + (busInfo.getSaturdayCourseCount() + busInfo.getSundayCourseCount())/2 + "\n"; // weekend average

        }
        return result;
    }

    public String toString(){
        String result = "";
        for(BusInfo info : this.busInfos){
            result += info;
        }
        return result;
    }
}
