package window_interface;


import businfo.lists.ListContainer;
import businfo.lists.WarszawaSelectedBusStops;

import java.io.IOException;

/**
 * Created by MIachal on 19.06.2017.
 */
public class BusStopAddZTM extends Thread {

    public ListContainer linkContainer;
    public String busStopNameFromList;

    public void addLinkToListCointainer(ListContainer linkContainerFromWindowInterface, String busStopNameFromListFromWindowInterface) throws IOException {

        linkContainer = linkContainerFromWindowInterface;
        busStopNameFromList = busStopNameFromListFromWindowInterface;
        // linkContainer.addListHandler(new KrakowSelectedBusStops(busStopNameFromList));
    }


    @Override
    public void run(){
        // BusStopAdd threadUsage = new BusStopAdd();
        try {
            // threadUsage.addLinkToListCointainer(linkContainer, busStopNameFromList);
            linkContainer.addListHandler(new WarszawaSelectedBusStops(busStopNameFromList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
