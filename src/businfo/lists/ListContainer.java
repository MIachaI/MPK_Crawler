package businfo.lists;

import businfo.busstop.BusInfo;

import java.util.ArrayList;

/**
 * Class to hold multiple ListHandlers (multiple lines for one bus stop)
 */
public class ListContainer {
    private ArrayList<ListHandler> listHandlers;
    private ArrayList<BusInfo> busInfos;

    public ListContainer(){
        this.listHandlers = new ArrayList<>();
    }
    public ListContainer(ArrayList<ListHandler> lists){
        this.listHandlers = lists;
        this.busInfos = findBusInfos(this.listHandlers);
    }

    // TODO implement function that will save results to excel

    public void addListHandler(ListHandler listHandler){
        this.listHandlers.add(listHandler);
        this.busInfos.addAll(listHandler.getBusInfosPurified());
    }

    public ArrayList<BusInfo> findBusInfos(ArrayList<ListHandler> listHandlers){
        ArrayList<BusInfo> purified = new ArrayList<>();
        for(ListHandler list : listHandlers){
            purified.addAll(list.getBusInfosPurified());
        }
        return purified;
    }
    public ArrayList<BusInfo> getBusInfos(){
        return this.busInfos;
    }
}
