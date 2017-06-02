package businfo.lists;

import businfo.busstop.BusInfo;

import java.util.ArrayList;

/**
 * Class to hold multiple ListHandlers (multiple lines for one bus stop)
 */
public class ListContainer {
    private ArrayList<ListHandler> listHandlers;
    private boolean isNonPurified;

    public ListContainer(){
        this(false);
    }

    public ListContainer(boolean isNonPurified){
        this.listHandlers = new ArrayList<>();
        this.isNonPurified = isNonPurified;
    }

    public ListContainer(ArrayList<ListHandler> lists){
        this(lists, false);
    }

    public ListContainer(ArrayList<ListHandler> lists, boolean isNonPurified){
        this.listHandlers = lists;
        this.isNonPurified = isNonPurified;
    }

    public void addListHandler(ListHandler listHandler){
        this.listHandlers.add(listHandler);
    }

    public void deleteListHandler() {
        this.listHandlers.clear();
    }

    public static ArrayList<BusInfo> getBusInfosFromLists(ArrayList<ListHandler> listHandlers){
        ArrayList<BusInfo> result = new ArrayList<>();
        for(ListHandler list : listHandlers){
            result.addAll(list.getBusInfosPurified());
        }
        return result;
    }

    public void setNonPurified(boolean setting){
        this.isNonPurified = setting;
    }

    // GETTERS
    public ArrayList<BusInfo> getBusInfos(){
        ArrayList<BusInfo> result = new ArrayList<>();
        if(this.isNonPurified){
            for (ListHandler listHandler : this.listHandlers) {
                result.addAll(listHandler.getBusInfosNonPurified());
            }
        } else {
            for (ListHandler listHandler : this.listHandlers) {
                result.addAll(listHandler.getBusInfosPurified());
            }
        }
        return result;
    }

    public ArrayList<BusInfo> getOnlyTrams(){
        ArrayList<BusInfo> result = new ArrayList<>();
        if(this.isNonPurified){
            for(ListHandler list : this.listHandlers){
                result.addAll(list.getOnlyTramsNonPurified());
            }
        } else {
            for (ListHandler list : this.listHandlers) {
                result.addAll(list.getOnlyTrams());
            }
        }
        return result;
    }

    public ArrayList<BusInfo> getOnlyBuses(){
        ArrayList<BusInfo> result = new ArrayList<>();
        if(this.isNonPurified){
            for(ListHandler list : this.listHandlers){
                result.addAll(list.getOnlyBusesNonPurified());
            }
        } else {
            for(ListHandler list : this.listHandlers){
                result.addAll(list.getOnlyBuses());
            }
        }
        return result;
    }
}
