package businfo.lists;

import businfo.busstop.BusInfo;

import java.util.ArrayList;

/**
 * Class to hold multiple ListHandlers (multiple lines for one bus stop)
 */
public class ListContainer {
    private ArrayList<SelectedBusStopsHandler> selectedBusStopsHandlers;
    private boolean isNonPurified;

    public ListContainer(){
        this(false);
    }

    public ListContainer(boolean isNonPurified){
        this.selectedBusStopsHandlers = new ArrayList<>();
        this.isNonPurified = isNonPurified;
    }

    public ListContainer(ArrayList<SelectedBusStopsHandler> lists){
        this(lists, false);
    }

    public ListContainer(ArrayList<SelectedBusStopsHandler> lists, boolean isNonPurified){
        this.selectedBusStopsHandlers = lists;
        this.isNonPurified = isNonPurified;
    }

    public void addListHandler(SelectedBusStopsHandler selectedBusStopsHandler){
        this.selectedBusStopsHandlers.add(selectedBusStopsHandler);
    }

    public void deleteListHandler() {
        this.selectedBusStopsHandlers.clear();
    }

    public static ArrayList<BusInfo> getBusInfosFromLists(ArrayList<SelectedBusStopsHandler> selectedBusStopsHandlers){
        ArrayList<BusInfo> result = new ArrayList<>();
        for(SelectedBusStopsHandler list : selectedBusStopsHandlers){
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
            for (SelectedBusStopsHandler selectedBusStopsHandler : this.selectedBusStopsHandlers) {
                result.addAll(selectedBusStopsHandler.getBusInfosNonPurified());
            }
        } else {
            for (SelectedBusStopsHandler selectedBusStopsHandler : this.selectedBusStopsHandlers) {
                result.addAll(selectedBusStopsHandler.getBusInfosPurified());
            }
        }
        return result;
    }

    public ArrayList<BusInfo> getOnlyTrams(){
        ArrayList<BusInfo> result = new ArrayList<>();
        if(this.isNonPurified){
            for(SelectedBusStopsHandler list : this.selectedBusStopsHandlers){
                result.addAll(list.getOnlyTramsNonPurified());
            }
        } else {
            for (SelectedBusStopsHandler list : this.selectedBusStopsHandlers) {
                result.addAll(list.getOnlyTrams());
            }
        }
        return result;
    }

    public ArrayList<BusInfo> getOnlyBuses(){
        ArrayList<BusInfo> result = new ArrayList<>();
        if(this.isNonPurified){
            for(SelectedBusStopsHandler list : this.selectedBusStopsHandlers){
                result.addAll(list.getOnlyBusesNonPurified());
            }
        } else {
            for(SelectedBusStopsHandler list : this.selectedBusStopsHandlers){
                result.addAll(list.getOnlyBuses());
            }
        }
        return result;
    }
}
