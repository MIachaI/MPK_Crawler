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
        //this.busInfosPurified = purifyList(BusInfo this.busInfos);
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
        ArrayList<BusInfo> result = new ArrayList<>();

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
