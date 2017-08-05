package businfov2;

import businfov2.timetable.Timetable;

import java.util.ArrayList;

public class BusStop {
    public static final String UNDEFINED = "undefined";

    private String name;
    public ArrayList<Timetable> timetables;

    public BusStop(String name, ArrayList<Timetable> timetables){
        this.name = name;
        this.timetables = timetables;
    }
    public BusStop(String name){ this(name, new ArrayList<Timetable>()); }
    public BusStop(){ this(BusStop.UNDEFINED, new ArrayList<Timetable>()); }

    public ArrayList<Timetable> purifiedTimetables() {
        ArrayList<Timetable> result = new ArrayList<>();

        return result;
    }
}
