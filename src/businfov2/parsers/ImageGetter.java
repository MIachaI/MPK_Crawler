package businfov2.parsers;

import businfo.busstop.ZTMinfo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public abstract class ImageGetter {
    public static String krakowImage(String html) throws IOException {
        Connection jsoupConnection = Jsoup.connect(html);
        StringBuilder result = new StringBuilder();
        // find print view page (looks nicer)
        Element printLink = jsoupConnection.get().select("td[style=' width: 100px; '] a[target='_blank']").first();
        String link = printLink.attr("href");
        // go to print view page
        Connection connection2 = Jsoup.connect(link);
        Document document2 = connection2.get();
        Element table = document2.select("table[style=' width: 700px; ']").first();
        Element head = document2.select("head").first();
        // add style absolute path
        head.append("<link rel='stylesheet' href='http://rozklady.mpk.krakow.pl/widok/GP/CSS/style.css' type='text/css' />");

        // add html tags at the beginning and the end
        result.append("<!DOCTYPE HTML>\n<html>");
        result.append(head.outerHtml()); 		// append body section
        result.append(table.outerHtml());		// append table section
        result.append("</html>");

        return result.toString();
    }

    public static String warszawaImage(String html) throws IOException {
        ZTMinfo info = new ZTMinfo(html);
        return info.getRawHtml();
    }

    public static String poznanImage(String html) throws IOException {
        StringBuilder result = new StringBuilder();
        Connection jsoupConnection = Jsoup.connect(html);
        Document document = jsoupConnection.get();
        Element head = document.select("head").first();
        Element table = document.select("div[id='MpkBoard']").first();

        result.append("<!DOCTYPE HTML>\n<html>");
        result.append(head.outerHtml());        // append body section
        result.append(table.outerHtml());		// append table section
        result.append("</html>");
        return result.toString();
    }

    public static String wroclawImage(){
        return null;
    }
}
