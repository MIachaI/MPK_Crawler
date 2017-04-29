import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException{
    	System.out.println("Podaj odpowiedni link: ");
    	Scanner scanner = new Scanner(System.in);
    	String html = scanner.nextLine();
    	scanner.close();
        BusCount busCount = BusCount.count(html);
        
        
        System.out.println(busCount);

        System.out.println();
        System.out.println("Lista weekday:\n"+busCount.getWeekdayList());
        System.out.println("Lista Saturday:\n"+busCount.getSaturdayList());
        System.out.println("Lista Sunday:\n"+busCount.getSundayList());
        System.out.println(busCount.exceptions());
        
    }

}