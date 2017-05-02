import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by umat on 02.05.17.
 */
public abstract class ListHandler {
    protected ArrayList<BusInfo> busInfos;
    protected ArrayList<String> linkList;

    public ListHandler(){
        this.busInfos = new ArrayList<>();
        this.linkList = new ArrayList<>();
    }

    protected abstract ArrayList<String> getLinks(String html) throws IOException;

    public ArrayList<String> getLinkList(){
        return this.linkList;
    }

    public String toString(){
        String result = "";
        for(BusInfo info : this.busInfos){
            result += info;
        }
        return result;
    }
}
