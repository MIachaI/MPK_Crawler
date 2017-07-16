package businfo.site_scanner;

import businfo.busstop.lines.LineOnStop;
import businfo.busstop.streets.BusStop;
import businfo.lists.BusStopLink;
import businfo.lists.BusStopList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by MIachaI on 16.07.17.
 */
public class WroclawScanner extends SiteScanner {
    public WroclawScanner(){
        super();
        this.mainPageURL = "http://www.wroclaw.pl/wszystkie-przystanki-wydruk";
    }
    @Override
    public ArrayList<BusStop> scan() throws IOException {
        ArrayList<BusStop> result = new ArrayList<>();
        ArrayList<BusStopLink> busStopLinks;
        busStopLinks = BusStopList.MPKWroclawBusStopLinksGetter();
        for(BusStopLink busStopLink : busStopLinks){
            BusStop busStop = new BusStop(busStopLink.getName());

            // now connect to a sub-site and fetch all bus-line-no:link pairs
            String subSite = busStopLink.getLink();
            Document document = Jsoup.connect(subSite).get();

            // get links and numbers elements
            Elements links = document.select("[class='btn']");
            for (int i = 0; i < links.size(); i+=2){ // every second element due to cells with the same information in certain row
                String linkLine ="http://www.wroclaw.pl"+links.get(i).attr("href");
                busStop.addBusLine(new LineOnStop(links.get(i).text(), links.get(i/2).attr("href")));
            }
            // when bus stop ready add to result
            result.add(busStop);
        }
        return result;
    }
}
