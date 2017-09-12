package businfo.lists;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

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
        //connect to bus stops page

        Connection.Response document1 = Jsoup.connect("http://rozklady.mpk.krakow.pl/?lang=PL&rozklad=20170912&akcja=przystanek").userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0").followRedirects(true).data("name", "mikel").referrer("https://www.google.pl/").validateTLSCertificates(true).cookie("Cookie","ROZKLADY_JEZYK=PL; ROZKLADY_WIZYTA=20; ROZKLADY_WIDTH=1920; __utma=174679166.1956832196.1504264753.1504264753.1504264753.1; __utmz=174679166.1504264753.1.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); ROZKLADY_OSTATNIA=1505207038; ROZKLADY_LWT=142__2__50").execute();
        Document document = document1.parse();
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
