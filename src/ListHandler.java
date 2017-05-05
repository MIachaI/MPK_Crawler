import java.io.IOException;
import java.util.ArrayList;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

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

    public String excelFormattedText(){
        String result = "";
        int WeekdayCount =0;
        int WeekendCount =0;
        int Carry=0;
        int TramWeekdayCount=0;
        int BusWeekdayCount=0;
        int TramWeekendCount=0;
        int BusWeekendcount=0;
        int BusNumberComparition=0;
        for(BusInfo busInfo : this.busInfos) {
            if (!(busInfo.getLineNumber() == BusNumberComparition)) {
                if (busInfo.getLineNumber() > 100 && Carry < 1) {
                    result += "\n\n\t\t\t\t\t" + WeekdayCount + "\t" + WeekendCount + "\n";
                    TramWeekdayCount = WeekdayCount;
                    WeekdayCount = 0;
                    TramWeekendCount = WeekendCount;
                    WeekendCount = 0;
                    Carry += 1;

                }
                result += busInfo.getLineNumber() +
                        "\t" + busInfo.getStreetName() +
                        "\t" + busInfo.getVehicleType() +
                        "\t" +                                      // empty cell for distance from building
                        "\t" + busInfo.getWeekdayCourseCount() +
                        "\t" + busInfo.getSaturdayCourseCount() +
                        "\t" + busInfo.getSundayCourseCount() +
                        "\t" + (busInfo.getSaturdayCourseCount() + busInfo.getSundayCourseCount()) / 2 + "\n"; // weekend average
                WeekdayCount += busInfo.getWeekdayCourseCount();
                WeekendCount += (busInfo.getSaturdayCourseCount() + busInfo.getSundayCourseCount()) / 2;
            }
        }
        return result +="\n\n\t\t\t\t\t" +WeekdayCount +"\t"+ WeekendCount;
    }

    public String toString(){
        String result = "";
        for(BusInfo info : this.busInfos){
            result += info;
        }
        return result;
    }
}
