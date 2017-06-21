import businfo.lists.BusStopList;
import window_interface.WindowInterface;

import java.io.IOException;

public class Main extends Thread{

    @Override
    public void run(){
        WindowInterface.openWindow();
    }


    public static void main(String[] args) throws IOException {
/**
 * First attempt in multithreading in Java, there might be consequences...
 */

        BusStopList addBusListInThreads = new BusStopList();
        addBusListInThreads.start();

        WindowInterface.openWindow();
    //    Main tak = new Main();
      //  tak.start();
    }


}