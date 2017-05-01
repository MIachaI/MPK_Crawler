import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException{
    	System.out.println("Podaj odpowiedni link: ");
    	Scanner scanner = new Scanner(System.in);
    	String html = scanner.nextLine();
    	scanner.close();
    	//String html = "http://rozklady.mpk.krakow.pl/?lang=PL&rozklad=20170429&linia=151";
        MPKinfo busCount = new MPKinfo(html);        

        System.out.println(busCount);
        System.out.println("\nDodatkowe informacje: " + busCount.getAdditionalInfo());

        System.out.println("Lista weekday:\n"+busCount.getWeekdayList());
        System.out.println("Lista Saturday:\n"+busCount.getSaturdayList());
        System.out.println("Lista Sunday:\n"+busCount.getSundayList());
        System.out.println(busCount.getRawResult(html));

    }
}