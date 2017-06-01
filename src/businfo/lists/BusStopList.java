package businfo.lists;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by MIachaI on 29.05.2017.
 */
public class BusStopList {
    public static ArrayList<busStop> BusStopLinksGetter ()throws IOException {
        //connect to main page of MPK Cracow
        Document initialConnect = Jsoup.connect("http://rozklady.mpk.krakow.pl").get();
        //choose polish language
        Element polishLanguageConnection = initialConnect.select("table[class='nav'] td[style='text-align: right; white-space: nowrap; '] a[href]").first();
        String polishLanguageLink = polishLanguageConnection.attr("href");
        //connect to polishLanguage link
        Document polishLangugeConnect = Jsoup.connect(polishLanguageLink).get();
        //get link to bus stops page
        Element connectionToSourcePage =polishLangugeConnect.select("table[class='nav'] td[style=' width: 100px; '] a[href]").first();
        String connection = connectionToSourcePage.attr("href");
        //connect to bus stops page
        Document document = Jsoup.connect(connection).get();
        ArrayList<busStop> stops = new ArrayList<>();
        //parse through elements to collect their links and names and save to ArrayList stop
        Elements links = document.select("form[id='main'] tbody tr a[href]");

        for(Element link : links) {
            String linkToBusStop=link.attr("href");
            String busStopName=link.text();
            stops.add(new busStop(linkToBusStop, busStopName));
        }
      //  for(busStop x : stops){
      //      System.out.println(x);
      //  }
        return stops;
    }
}
