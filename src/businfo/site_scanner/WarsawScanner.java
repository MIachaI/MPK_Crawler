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
 * Created by MIachaI on 15.07.17.
 */
public class WarsawScanner extends SiteScanner {
    public WarsawScanner(){
        super();
        this.mainPageURL = "http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=2145";
    }
    @Override
    public ArrayList<BusStop> scan() throws IOException {
        ArrayList<BusStop> result = new ArrayList<>();
        ArrayList<BusStopLink> busStopLinks;
        busStopLinks = BusStopList.ZTMBusStopLinksGetter();
        for(BusStopLink busStopLink : busStopLinks){
            BusStop busStop = new BusStop(busStopLink.getName());

            // now connect to a sub-site and fetch all bus-line-no:link pairs
            String subSite = busStopLink.getLink();
            Document document = Jsoup.connect(subSite).get();


            // get links and numbers elements
            Elements links = document.select("div[class=' PrzystanekLineList'] a[href]");
            Elements numbers = document.select("div[class='PrzystanekLineList'] a[href]");
            Element link = document.select("div[class=' PrzystanekLineList'] a[href]").first();
            String wyswietl = link.attr("href");


            for(Element item : links){
                String lineLink = "http://www.ztm.waw.pl/"+item.attr("href");
                String lineNumber = item.text();
                busStop.addBusLine(new LineOnStop(lineNumber,lineLink));
            }

            // when bus stop ready add to result
            result.add(busStop);
        }

        return result;
    }
}
