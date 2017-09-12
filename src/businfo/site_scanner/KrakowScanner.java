package businfo.site_scanner;

import businfo.busstop.lines.LineOnStop;
import businfo.busstop.streets.BusStop;
import businfo.lists.BusStopLink;
import businfo.lists.BusStopList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by umat on 13.07.17.
 */
public class KrakowScanner extends SiteScanner {
    public KrakowScanner(){
        super();
        this.mainPageURL = "http://rozklady.mpk.krakow.pl";
    }
    @Override
    public ArrayList<BusStop> scan() throws IOException {
        ArrayList<BusStop> result = new ArrayList<>();
        ArrayList<BusStopLink> busStopLinks;
        busStopLinks = BusStopList.MPKBusStopLinksGetter();
        for(BusStopLink busStopLink : busStopLinks){
            BusStop busStop = new BusStop(busStopLink.getName());

            // now connect to a sub-site and fetch all bus-line-no:link pairs
            String subSite = busStopLink.getLink();
            Document document = Jsoup.connect(subSite).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0").followRedirects(true).data("name", "mikel").referrer("https://www.google.pl/").validateTLSCertificates(true).cookie("Cookie","ROZKLADY_JEZYK=PL; ROZKLADY_WIZYTA=20; ROZKLADY_WIDTH=1920; __utma=174679166.1956832196.1504264753.1504264753.1504264753.1; __utmz=174679166.1504264753.1.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); ROZKLADY_OSTATNIA=1505207038; ROZKLADY_LWT=142__2__50").get();
            // get links and numbers elements
            Elements links = document.select("table[style=' margin-bottom: 20px; '] tbody tr a[href]");
            Elements numbers = document.select("table[style=' margin-bottom: 20px; '] tbody tr td[style=' text-align: right ;']");
            for (int i = 1; i < numbers.size(); i+=2){ // every second element cause numbers' even cells are empty with this jsoup selection
                busStop.addBusLine(new LineOnStop(numbers.get(i).text(), links.get(i/2).attr("href")));
            }

            // when bus stop ready add to result
            result.add(busStop);
        }

        return result;
    }
}
