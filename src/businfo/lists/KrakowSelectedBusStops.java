package businfo.lists;

import businfo.busstop.MPKinfo;
import businfo.busstop.lines.LineOnStop;
import businfo.busstop.streets.BusStop;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
@Deprecated
public class KrakowSelectedBusStops extends SelectedBusStopsHandler {
    public KrakowSelectedBusStops(){
        super();
    }
    public KrakowSelectedBusStops(ArrayList<BusStop> selectedBusStops) throws IOException {
        super(selectedBusStops);
        for(String link : this.linkList){
            busInfos.add(new MPKinfo(link));
        }
        this.busInfosPurified = purifyList(this.busInfos);
    }
    @Deprecated
    public KrakowSelectedBusStops(String html) throws IOException {
        super();
        this.linkList = findLinks(html);
        for(String link : this.linkList){
            busInfos.add(new MPKinfo(link));
        }
        this.busInfosPurified = purifyList(this.busInfos);
    }

    @Deprecated
    protected ArrayList<String> findLinks(String html) throws IOException{
          ArrayList<String> result = new ArrayList<>();
          Document document = Jsoup.connect(html).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0").followRedirects(true).data("name", "mikel").referrer("https://www.google.pl/").validateTLSCertificates(true).cookie("Cookie","ROZKLADY_JEZYK=PL; ROZKLADY_WIZYTA=20; ROZKLADY_WIDTH=1920; __utma=174679166.1956832196.1504264753.1504264753.1504264753.1; __utmz=174679166.1504264753.1.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); ROZKLADY_OSTATNIA=1505207038; ROZKLADY_LWT=142__2__50").get();
          Elements links = document.select("table[style=' margin-bottom: 20px; '] tbody tr a[href]");
          for(Element link : links) {
              result.add(link.attr("href"));
          }
          return result;
      }
}
