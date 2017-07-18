package businfo.lists;

import businfo.busstop.ZTMinfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by umat on 11.05.17.
 */
public class WarszawaSelectedBusStops extends SelectedBusStopsHandler {
    public WarszawaSelectedBusStops(){
        super();
    }
    @Deprecated
    public WarszawaSelectedBusStops(String html) throws IOException {
        super();
        this.linkList = findLinks(html);
        for(String link : this.linkList){
            busInfos.add(new ZTMinfo(link));
        }
        this.busInfosPurified = purifyList(this.busInfos);
    }

    @Override
    protected ArrayList<String> findLinks(String html) throws IOException {
        ArrayList<String> result = new ArrayList<>();
        Document document = Jsoup.connect(html).get();
        Elements links = document.select("div[class='PrzystanekLineList'] a");

        for(Element link : links){
            result.add("http://www.ztm.waw.pl/" + link.attr("href"));
        }
        return result;
    }
}
