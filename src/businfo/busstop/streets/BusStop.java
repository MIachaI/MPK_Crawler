package businfo.busstop.streets;

import businfo.busstop.lines.LineOnStop;

import java.util.ArrayList;

/**
 * Created by umat on 06.07.17.
 */
public abstract class BusStop {
    private String streetName;
    /**
     * Bus lines that pass through
     */
    private ArrayList<LineOnStop> linesOnStops;

    public BusStop(){
        this.linesOnStops = new ArrayList<>();
    }

    public BusStop(ArrayList<LineOnStop> busList){
        this();
        this.setLinesOnStop(busList);
    }

    /**
     * @return ArrayList with information about specific lines
     */
    public ArrayList<LineOnStop> getBusLines(){
        return this.linesOnStops;
    }

    /**
     * Add one more bus info to the list
     * @param busLine to be added
     */
    public void addBusLine(LineOnStop busLine){
        this.linesOnStops.add(busLine);
    }

    /**
     * Set a list with bus information
     * @param lines to be set in ArrayList
     */
    public void setLinesOnStop(ArrayList<LineOnStop> lines){
        this.linesOnStops = lines;
    }
}
