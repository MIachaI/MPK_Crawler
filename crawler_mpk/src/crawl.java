import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Scanner;
public class crawl {

    public static void main(String[] args) throws IOException{
        /*
         Scanner reader = new Scanner(System.in);
         System.out.println("Enter a number: ");
         String n = reader.nextLine();
         przykładowy format: http://rozklady.mpk.krakow.pl/?lang=PL&akcja=przystanek&rozklad=20170428&przystanek=S3Jvd29kcnphIEfDs3JrYQeEeeEe
        */

        Document doc = Jsoup.connect("http://rozklady.mpk.krakow.pl/?lang=PL&akcja=przystanek&rozklad=20170428&przystanek=S3Jvd29kcnphIEfDs3JrYQeEeeEe").get();
        Elements links = doc.getElementsByTag("a");

        //unikalny początkowy fragment dla interesujacego linka
        String porownanie = "http://rozklady.mpk.krakow.pl/?lang=PL&rozklad=20170428&linia"; //47+14=61

        //fragment do pobierania linków do rozkładów

        for(Element link: links){
            String l = link.attr("href");
            if(l.length()>0){
                if(l.length()<4)
                    l = doc.baseUri()+l.substring(1);
                else if(!l.substring(0, 4).equals("http"))
                    l = doc.baseUri()+l.substring(1);
            }
            // eliminuję nieinteresujące mnie linki
            if(l.length()>62){
                if(l.substring(0,61).equals(porownanie)){
                    if()
                 // przycinanie(l);
                    rozklad(l);
                }
            }
        }
    }

    public static void rozklad(String args) throws IOException {
        try {
            Document doc = Jsoup.connect(args).get();
            //filtrowanie, czy to jest ok? nie umiem bardziej doprecyzować
            Elements trs = doc.select("table table table table table tr:eq(1)");

            trs.remove(0);

            for (Element tr : trs) {
                Elements tds = tr.getElementsByTag("td");
                Element td = tds.first();
                if(td.text().length()>100){
                   // przycinanie(td.text());
                    System.out.println(td.text());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
            // obróbka interesującego stringa
    public static void przycinanie(String args) {

           String bufor1,bufor2;
            if(args.substring(0,37).equals("Godzina Dzień powszedni Soboty Święta")){
                bufor1 = args.substring(38,args.length());
                        for(int i=0;i<args.length();i++){
                            if (bufor1.substring(i,i+1).equals("Z")){
                                bufor2 = bufor1.substring(0,i-1);
                                System.out.println(bufor2);
                                break;
                            }

                        }
            }}

    }
