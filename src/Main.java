import businfo.lists.BusStopList;
import window_interface.WindowInterface;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException{

        WindowInterface.openWindow();
       BusStopList.BusStopLinksGetter();


    }
}