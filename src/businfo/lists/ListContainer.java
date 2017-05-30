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
        this.busInfos = new ArrayList<>();
    }
    public ListContainer(ArrayList<ListHandler> lists){
        this.listHandlers = lists;
        this.busInfos = findBusInfos(this.listHandlers);
    }

    // TODO implement function that will save results to save.excel

    public void addListHandler(ListHandler listHandler){
        this.listHandlers.add(listHandler);
        this.busInfos.addAll(listHandler.getBusInfos());
    }

    public static ArrayList<BusInfo> getBusInfosFromLists(ArrayList<ListHandler> listHandlers){
        ArrayList<BusInfo> result = new ArrayList<>();
        for(ListHandler list : listHandlers){
            result.addAll(list.getBusInfos());
        }
        return result;
    }

    private ArrayList<BusInfo> findBusInfos(ArrayList<ListHandler> listHandlers){
        ArrayList<BusInfo> purified = new ArrayList<>();
        for(ListHandler list : listHandlers){
            purified.addAll(list.getBusInfos());
        }
        return purified;
    }

    // GETTERS
    public ArrayList<BusInfo> getBusInfos(){
        return this.busInfos;
    }
    public ArrayList<BusInfo> getOnlyTrams(){
        ArrayList<BusInfo> result = new ArrayList<>();
        for(ListHandler list : this.listHandlers){
            result.addAll(list.getOnlyTrams());
        }
        return result;
    }
    public ArrayList<BusInfo> getOnlyBuses(){
        ArrayList<BusInfo> result = new ArrayList<>();
        for(ListHandler list : this.listHandlers){
            result.addAll(list.getOnlyBuses());
        }
        return result;
    }

    public ArrayList<BusInfo> getOnlyTramsNonPurified(){
        ArrayList<BusInfo> result = new ArrayList<>();
        for(ListHandler list : this.listHandlers){
            result.addAll(list.getOnlyTramsNonPurified());
        }
        return result;
    }

    public ArrayList<BusInfo> getOnlyBusesNonPurified(){
        ArrayList<BusInfo> result = new ArrayList<>();
        for(ListHandler list : this.listHandlers){
            result.addAll(list.getOnlyBusesNonPurified());
        }
        return result;
    }
}
