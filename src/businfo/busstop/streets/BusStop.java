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

    public BusStop(String html){
        this();
        this.fetchLines(html);
    }

    /**
     * Fetch all info for LineOnStop class from one bus stop.
     * You need to provide proper link
     * @param html link with information
     */
    protected abstract void fetchLines(String html);
}
