import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException{
    System.out.println("Podaj odpowiedni link: ");
//    	Scanner scanner = new Scanner(System.in);
//    	String html = scanner.nextLine();
//    	scanner.close();
        String html = "http://rozklady.mpk.krakow.pl/?lang=PL&akcja=przystanek&rozklad=20170502&przystanek=QUdI";

        MPKList mpkInfo = new MPKList(html);
        System.out.print(mpkInfo);

    }
}