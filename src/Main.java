import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException{
    System.out.println("Podaj odpowiedni link: ");
//    	Scanner scanner = new Scanner(System.in);
//    	String html = scanner.nextLine();
//    	scanner.close();
        String html = "http://rozklady.mpk.krakow.pl/?lang=PL&rozklad=20170510&linia=132__2__32";

        //MPKList mpkInfo = new MPKList(html);
        MPKinfo mpkInfo = new MPKinfo(html);

        System.out.println(mpkInfo.getRawResult(html));
        for(String columnName : mpkInfo.getColumnNames()){
            System.out.println(columnName);
        }
    }
}