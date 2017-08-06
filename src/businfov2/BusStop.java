package businfov2;

import businfov2.timetable.Timetable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BusStop {
    public static final String UNDEFINED = "undefined";

    private String name;
    private ArrayList<Timetable> timetables;

    public BusStop(String name, ArrayList<Timetable> timetables){
        this.name = name;
        this.timetables = timetables;
    }
    public BusStop(String name){ this(name, new ArrayList<Timetable>()); }
    public BusStop(){ this(BusStop.UNDEFINED, new ArrayList<Timetable>()); }

    public void setTimetables(ArrayList<Timetable> timetables){
        this.timetables = timetables;
    }
    public ArrayList<Timetable> getTimetables() { return this.timetables; }

    public ArrayList<Timetable> getTimetables(CertificationMethod method){
        if(!method.isImplemented()) return new ArrayList<>();
        switch(method){
            case LEED_2009:
                return this.leed2009Timetables();
            case BREEAM:
                return this.breeamTimetables();
            case LEED_v4:
                return this.leedv4Timetables();
            default:
                return new ArrayList<>();
        }
    }

    /**
     * @return timetables meeting BREEAM method requirements
     */
    private ArrayList<Timetable> breeamTimetables() {
        ArrayList<Timetable> result = new ArrayList<>();
        HashMap<String, Timetable> lineTimetable = new HashMap<>();
        for(Timetable timetable : this.timetables){
            if(!lineTimetable.containsKey(timetable.lineNumber)) {
                lineTimetable.put(timetable.lineNumber, timetable);
                continue;
            }
            Timetable comparedTimetable = lineTimetable.get(timetable.lineNumber);
            // compare all columns and replace for lowest scores
            for(Timetable.Column column : timetable.columns){
                Timetable.Day analyzedDay = column.getDayType();
                if(comparedTimetable.getDeparturesAmount(analyzedDay) > timetable.getDeparturesAmount(analyzedDay)) {
                    for (int i = 0; i < comparedTimetable.columns.size(); i++) {
                        if(comparedTimetable.columns.get(i).getDayType() == analyzedDay)
                            comparedTimetable.columns.set(i, column);
                    }
                }
            }
            lineTimetable.put(timetable.lineNumber, comparedTimetable);
        }
        return result;
    }

    private ArrayList<Timetable> leed2009Timetables(){
        return this.timetables;
    }

    // LEED v4 utils
    /**
     * @return only lineNumbers that occur in a bus stop more than once
     */
    private Set<String> getLineNumbersThatHavePair(){
        Set<String> result = new HashSet<String>();
        HashMap<String, Integer> numberOfOccurances = new HashMap<>();
        for(Timetable timetable : this.timetables){
            int number = numberOfOccurances.getOrDefault(timetable.lineNumber, 1);
            numberOfOccurances.put(timetable.lineNumber, ++number);
            if(number > 1) result.add(timetable.lineNumber);
        }

        return result;
    }

    private ArrayList<Timetable> leedv4Timetables(){
        ArrayList<Timetable> result = new ArrayList<>();
        Set<String> linesThatHavePair = this.getLineNumbersThatHavePair();
        HashMap<String, Timetable> lineTimetable = new HashMap<>();
        for(Timetable timetable : this.timetables){
            if(linesThatHavePair.contains(timetable.lineNumber)){
                if(!lineTimetable.containsKey(timetable.lineNumber)) {
                    lineTimetable.put(timetable.lineNumber, timetable);
                    continue;
                }
                Timetable comparedTimetable = lineTimetable.get(timetable.lineNumber);
                // compare all columns and replace for lowest scores
                for(Timetable.Column column : timetable.columns){
                    Timetable.Day analyzedDay = column.getDayType();
                    if(comparedTimetable.getDeparturesAmount(analyzedDay) > timetable.getDeparturesAmount(analyzedDay)) {
                        for (int i = 0; i < comparedTimetable.columns.size(); i++) {
                            if(comparedTimetable.columns.get(i).getDayType() == analyzedDay)
                                comparedTimetable.columns.set(i, column);
                        }
                    }
                }
                lineTimetable.put(timetable.lineNumber, comparedTimetable);
            }
        }
        for(String lineNumber : lineTimetable.keySet()){
            result.add(lineTimetable.get(lineNumber));
        }
        return result;
    }
}
