package businfo.busstop.timetable;

import java.util.ArrayList;
import java.util.Arrays;

public class Timetable {
    public enum VehicleType {
        BUS,
        TRAM,
        UNDEFINED;

        public String toString(){
            switch(this){
                case BUS:
                    return "Bus";
                case TRAM:
                    return "Light train";
                case UNDEFINED:
                    return "undefined";
                default:
                    return "undefined";
            }
        }
    }

    public String lineNumber;
    public VehicleType vehicleType;
    public String busStopName;
    public ArrayList<Column> columns;
    public ArrayList<String> additionalInfo;

    public Timetable(){
        this.lineNumber = "undefined";
        this.vehicleType = VehicleType.UNDEFINED;
        this.busStopName = "undefined";
        this.columns = new ArrayList<>();
        this.additionalInfo = new ArrayList<>();
    }

    public static Timetable rawResultToTimetable(String rawResult) throws Exception {
        Timetable result = new Timetable();
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(rawResult.split("\n")));
        if(lines.size() < 5) throw new Exception("rawResult not valid");

        result.lineNumber = lines.get(0);
        String vehType = lines.get(1);
        switch(vehType){
            case "Bus":
            case "bus":
                result.vehicleType = VehicleType.BUS;
                break;
            case "Light train":
            case "tram":
                result.vehicleType = VehicleType.TRAM;
            default:
                result.vehicleType = VehicleType.UNDEFINED;
        }
        result.busStopName = lines.get(2);
        for(String columnName : lines.get(3).split("\t")){
            result.columns.add(new Column(columnName));
        }

        ArrayList<String> timetableLines = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(rawResult.split("\n"), 4, rawResult.split("\n").length-1)));
        for(String line : timetableLines){
            line = line.replaceAll("[^\\d\\s]", "");
            ArrayList<String> fields = new ArrayList<>(Arrays.asList(line.split("\t")));
            if(fields.size() != result.columns.size() + 1) throw new Exception("Columns amount does not match timetable");
            int hour = Integer.parseInt(fields.get(0));
            fields = new ArrayList<String>(fields.subList(1, fields.size()));
            for(int i = 0 ; i < result.columns.size(); i++){
                String[] minuteStrings = fields.get(i).split(" ");
                ArrayList<Integer> minutes = new ArrayList<>();
                // convert minutes to integers
                for(String minuteString : minuteStrings)
                    if (!minuteString.isEmpty()) minutes.add(Integer.parseInt(minuteString));
                // add times to column
                for(Integer minute : minutes) result.columns.get(i).departures.add(new HourMinute(hour, minute));
            }
        }

        result.additionalInfo.addAll(Arrays.asList(lines.get(lines.size() - 1).split("\t")));

        return result;
    }

    public String toString(){
        StringBuilder result = new StringBuilder();
        String newline = System.getProperty("line.separator");
        result.append(this.vehicleType).append(" numer: ").append(this.lineNumber).append(newline);
        result.append("Przystanek: ").append(this.busStopName).append(newline);
        for(Column column : this.columns){
            result.append(column.name).append("\t");
        }
        result.append(newline);
        for(int i = 0; i < 24; i++){
            result.append(i).append("\t");
            for(Column column : this.columns){
                for(HourMinute time : column.getDeparturesFromHour(i)){
                    result.append(time.minute).append(" ");
                }
                result.append("\t");
            }
            result.append(newline);
        }
        for(String additionalInfo : this.additionalInfo){
            result.append(additionalInfo).append(newline);
        }

        return result.toString();
    }

    /**
     * Class representing single column in a timetable
     * It usually contains information about departures for a given day of week (e.g.
     */
    public static class Column{
        public String name;
        public ArrayList<HourMinute> departures;
        public Column(){
            this.departures = new ArrayList<>();
        }
        public Column(String name){
            this();
            this.name = name;
        }

        public ArrayList<HourMinute> getDeparturesFromHour(int hour){
            ArrayList<HourMinute> result = new ArrayList<>();
            for(HourMinute time : this.departures)
                if(time.hour == hour) result.add(time);
            return result;
        }
    }
}
