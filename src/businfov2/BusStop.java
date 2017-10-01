package businfov2;

import businfo.busstop.lines.LineOnStop;
import businfov2.parsers.Parser;
import businfov2.timetable.Timetable;

import java.util.*;

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

    public void setTimetables(ArrayList<Timetable> timetables){
        this.timetables = timetables;
    }
    public ArrayList<Timetable> getTimetables() { return this.timetables; }

    public ArrayList<Timetable> getTimetables(CertificationMethod method)
    throws CertificationMethod.NotImplementedException {
        if(!method.isImplemented()) throw new CertificationMethod.NotImplementedException("Method not yet implemented");
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

    public ArrayList<Timetable> getTimetables(VehicleType vehicleType){
        ArrayList<Timetable> result = new ArrayList<>(this.timetables);
        result.removeIf(timetable -> timetable.vehicleType != vehicleType);
        return result;
    }

    public ArrayList<Timetable> getTimetables(VehicleType vehicle, CertificationMethod method)
    throws CertificationMethod.NotImplementedException {
        ArrayList<Timetable> result = new ArrayList<>(this.getTimetables(method));
        result.removeIf(timetable -> timetable.vehicleType != vehicle);
        return result;
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
        Set<String> result = new HashSet<>();
        HashMap<String, Integer> numberOfOccurances = new HashMap<>();
        for(Timetable timetable : this.timetables){
            int number = numberOfOccurances.getOrDefault(timetable.lineNumber, 1);
            numberOfOccurances.put(timetable.lineNumber, ++number);
            if(number > 1) result.add(timetable.lineNumber);
        }

        return result;
    }

    /**
     * Chooses only lines that have courses in both ways on given BusStop. Also choose the lowest amount of hours among
     * found
     * @return
     */
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

    /**
     * Now SaveAllTask class takes care of this
     */
    @Deprecated
    public static ArrayList<BusStop> convertBusStops(City city, ArrayList<businfo.busstop.streets.BusStop> stops)
    throws Exception {
        city.isImplemented();
        ArrayList<BusStop> result = new ArrayList<>();
        for(businfo.busstop.streets.BusStop stop : stops){
            ArrayList<Timetable> timetablesToAdd = new ArrayList<>();
            for(LineOnStop line : stop.getBusLines()){
                timetablesToAdd.add(Parser.parse(line.getLink(), city));
            }
            result.add(new BusStop(stop.getStreetName(), timetablesToAdd));
        }

        return result;
    }
}
