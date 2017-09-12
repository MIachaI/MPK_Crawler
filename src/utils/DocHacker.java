package utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DocHacker {
    public static Document getDocument(String link) throws IOException {
        return getDocument(Jsoup.connect(link));
    }

    /**
     * Set Jsoup connection parameters
     * @param connection connection to site
     * @return Document object which can be further parsed with JSOUP
     * @throws IOException upon connection error
     */
    public static Document getDocument(Connection connection) throws IOException {
        return connection
                .ignoreContentType(true)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0")
                .referrer("http://rozklady.mpk.krakow.pl")
                .timeout(12000)
                .validateTLSCertificates(true)
                .followRedirects(false)
                .cookie("Cookie","ROZKLADY_JEZYK=PL; ROZKLADY_WIZYTA=20; ROZKLADY_WIDTH=1920; __utma=174679166.1956832196.1504264753.1504264753.1504264753.1; __utmz=174679166.1504264753.1.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); ROZKLADY_OSTATNIA=1505207038; ROZKLADY_LWT=142__2__50")
                .get();
    }
}
