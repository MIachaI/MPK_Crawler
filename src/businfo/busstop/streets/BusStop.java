package businfo.busstop.streets;

import businfo.busstop.lines.LineOnStop;

import java.util.ArrayList;

/**
 * Created by umat on 06.07.17.
 */
public class BusStop {
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
    public BusStop(String streetName, ArrayList<LineOnStop> busList){
        this(busList);
        this.streetName = streetName;
    }
    public BusStop(String streetName){
        this();
        this.streetName = streetName;
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

    public String getStreetName(){
        return this.streetName;
    }

    /**
     * Set a list with bus information
     * @param lines to be set in ArrayList
     */
    public void setLinesOnStop(ArrayList<LineOnStop> lines){
        this.linesOnStops = lines;
    }

    public String toString(){
        StringBuilder result = new StringBuilder("\n" + this.streetName);
        for (LineOnStop line : this.linesOnStops){
            result.append("\n\t> ").append(line.getNumber()).append(": ").append(line.getLink());
        }
        return result.toString();
    }
}
