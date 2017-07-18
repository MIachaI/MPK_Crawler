package window_interface;


import businfo.lists.KrakowSelectedBusStops;
import businfo.lists.ListContainer;

import java.io.IOException;

/**
 * Created by MIachal on 19.06.2017.
 *
 */
public class BusStopAddMPK extends Thread {

    public ListContainer linkContainer;
    public String busStopNameFromList;

    /**
     *
     * @param linkContainerFromWindowInterface - this is container prepared for contain bus stops which are significant for case
     * @param busStopNameFromListFromWindowInterface - this is bus stop name which is given by busStopNameTextField from Window Interface
     * @throws IOException - just to handle multithreading
     */
    public void addLinkToListCointainer(ListContainer linkContainerFromWindowInterface, String busStopNameFromListFromWindowInterface) throws IOException {

        linkContainer = linkContainerFromWindowInterface;
        busStopNameFromList = busStopNameFromListFromWindowInterface;
    }


    @Override
    public void run(){
        try {
            linkContainer.addListHandler(new KrakowSelectedBusStops(busStopNameFromList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
