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
    /**
     * This metod is prepared to get all bus stop names from certain city, MPK for Cracow
     * @return ArrayList which contains the bus stop names and links to them
     * @throws IOException - just to handle multithreading
     */
    public static ArrayList<BusStopLink> MPKBusStopLinksGetter ()throws IOException {
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
        ArrayList<BusStopLink> MPKstops = new ArrayList<>();
        //parse through elements to collect their links and names and save to ArrayList MPKstop
        Elements links = document.select("form[id='main'] tbody tr a[href]");

        for(Element link : links) {
            String linkToBusStop=link.attr("href");
            String busStopName=link.text();
            MPKstops.add(new BusStopLink(linkToBusStop, busStopName));
        }
        return MPKstops;
    }

    /**
     * This method is prepared to get all bus stops names from certain city, in this case - Wrocław
     * @return ArrayList which contains the bus stop names and links to them
     * @throws IOException - just to handle multithreading
     */
    public static ArrayList<BusStopLink> MPKWroclawBusStopLinksGetter ()throws IOException {
        //connect to main page of MPK Wrocław
        Document initialConnect = Jsoup.connect("http://www.wroclaw.pl/wszystkie-przystanki-wydruk").get();
        ArrayList<BusStopLink> MPKWroclawStop = new ArrayList<>();
        //parse through elements to collect their links and names and save to ArrayList MPKWroclawstop
        Elements links = initialConnect.select("ul [class='filtered-lines-list'] a[href]");

        for(Element link : links) {
            String linkToBusStop="http://www.wroclaw.pl"+link.attr("href");
            String busStopName=link.text();
            MPKWroclawStop.add(new BusStopLink(linkToBusStop, busStopName));
        }
        return MPKWroclawStop;
    }

    /**
     * This method is prepared to get all bus stop names from certain city, ZTM for Warsaw
     * @return ArrayList which contains the bus stop names and links to them
     * @throws IOException - just to handle multithreading
     */
    public static ArrayList<BusStopLink> ZTMBusStopLinksGetter () throws IOException {
        //connect to bus stops page of ZTM Warsaw
        Document initialConnect = Jsoup.connect("http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1").get();
        //choose content
        Elements links = initialConnect.select("div[id='RozkladContent'] a[href]");
        ArrayList<BusStopLink> ZTMstops = new ArrayList<>();
        //parse through elements to collect their links and names and save to ArrayList ZTMstop
        String mainLink = "http://www.ztm.waw.pl/";
        for(Element link : links) {
            String linkToBusStop=mainLink+link.attr("href");
            String busStopName=link.text();
            busStopName = busStopName.replaceAll("\\(.*\\)", "");
            busStopName = busStopName.substring(0, busStopName.length()-1);
            ZTMstops.add(new BusStopLink(linkToBusStop, busStopName));
        }
        return ZTMstops;
    }

    /**
     * We are overriding method run from thread class to get possibility to use multithreading
     */
    @Override
    public void run() {
        try {
            BusStopList.MPKWroclawBusStopLinksGetter();
            BusStopList.MPKBusStopLinksGetter();
            BusStopList.ZTMBusStopLinksGetter();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
