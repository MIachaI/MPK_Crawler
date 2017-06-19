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
public class BusStopList extends Thread {
    public static ArrayList<busStop> MPKBusStopLinksGetter ()throws IOException {
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
        ArrayList<busStop> MPKstops = new ArrayList<>();
        //parse through elements to collect their links and names and save to ArrayList MPKstop
        Elements links = document.select("form[id='main'] tbody tr a[href]");

        for(Element link : links) {
            String linkToBusStop=link.attr("href");
            String busStopName=link.text();
            MPKstops.add(new busStop(linkToBusStop, busStopName));
        }
        return MPKstops;
    }
    public static ArrayList<busStop> ZTMBusStopLinksGetter () throws IOException {

        //connect to bus stops page of ZTM Warsaw
        Document initialConnect = Jsoup.connect("http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1").get();
        //choose content
        Elements links = initialConnect.select("div[id='RozkladContent'] a[href]");
        ArrayList<busStop> ZTMstops = new ArrayList<>();
        //parse through elements to collect their links and names and save to ArrayList ZTMstop
        String mainLink = "http://www.ztm.waw.pl/";
        for(Element link : links) {
            String linkToBusStop=mainLink+link.attr("href");
            String busStopName=link.text();
            busStopName = busStopName.replaceAll("\\(.*\\)", "");
            busStopName = busStopName.substring(0, busStopName.length()-1);
            ZTMstops.add(new busStop(linkToBusStop, busStopName));
        }
        return ZTMstops;
    }

    @Override
    public void run() {

        try {
            BusStopList.MPKBusStopLinksGetter();
            BusStopList.ZTMBusStopLinksGetter();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
