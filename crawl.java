import java.io.IOException;
import java.util.Scanner;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class crawl {
    public static void main(String[] args) throws IOException {
        System.out.println("Podaj odpowiedni link: ");
        Scanner scanner = new Scanner(System.in);
        String html = scanner.nextLine();
        scanner.close();

        Document doc = Jsoup.connect(html).get();
        Elements links = doc.getElementsByTag("a");

        String porownanie = "http://rozklady.mpk.krakow.pl/?lang=PL&rozklad=20170429&linia"; //47+14=61
        String porownanie_kierunkow = "";

        for (Element link : links) {
            String l = link.attr("href");
            if (l.length() > 0) {
                if (l.length() < 4)
                    l = doc.baseUri() + l.substring(1);
                else if (!l.substring(0, 4).equals("http"))
                    l = doc.baseUri() + l.substring(1);
            }

            // eliminuję nieinteresujące mnie linki
            if (l.length() > 62) {
                if (l.substring(0, 61).equals(porownanie)) {
                     if (!l.substring(0, 66).equals(porownanie_kierunkow)) {
                        porownanie_kierunkow = l.substring(0, 66);
                         System.out.println(l);
                         BusCount busCount = BusCount.count(l);
                         System.out.println(busCount);
                         System.out.println();
                         System.out.println("Lista weekday:\n" + busCount.getWeekdayList());
                         System.out.println("Lista Saturday:\n" + busCount.getSaturdayList());
                         System.out.println("Lista Sunday:\n" + busCount.getSundayList());
                         System.out.println(busCount.exceptions());
                     }

            /*
                        BusCount busCount = BusCount.count(l);
                        System.out.println(busCount);
                        System.out.println();
                        System.out.println("Lista weekday:\n" + busCount.getWeekdayList());
                        System.out.println("Lista Saturday:\n" + busCount.getSaturdayList());
                        System.out.println("Lista Sunday:\n" + busCount.getSundayList());
                        System.out.println(busCount.exceptions());
                        */
                    }
                }
            }
        }

/*
        BusCount busCount = BusCount.count(html);


        System.out.println(busCount);

        System.out.println();
        System.out.println("Lista weekday:\n" + busCount.getWeekdayList());
        System.out.println("Lista Saturday:\n" + busCount.getSaturdayList());
        System.out.println("Lista Sunday:\n" + busCount.getSundayList());
        System.out.println(busCount.exceptions());
*/
    }


