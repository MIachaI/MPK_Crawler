import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MPKList extends ListHandler {
    public MPKList(){
        super();
    }
    public MPKList(String html) throws IOException {
        super();
        this.linkList = findLinks(html);
        for(String link : this.linkList){
            busInfos.add(new MPKinfo(link));
        }
        this.busInfosPurified = purifyList(this.busInfos);
    }
    protected ArrayList<String> findLinks(String html) throws IOException{
          ArrayList<String> result = new ArrayList<>();
          Document document = Jsoup.connect(html).get();
          Elements links = document.select("table[style=' margin-bottom: 20px; '] tbody tr a[href]");
          for(Element link : links) {
              result.add(link.attr("href"));
          }
          return result;
      }
}
