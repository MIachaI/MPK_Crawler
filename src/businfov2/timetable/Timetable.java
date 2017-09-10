package businfov2.timetable;

import businfov2.VehicleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Stores timetable information about bus and trams departures from a stop
 */
public class Timetable {
    private final static String UNDEFINED = "undefined";

    public String lineNumber;
    public VehicleType vehicleType;
    public String busStopName;
    public String previousStop;
    public String nextStop;
    public ArrayList<Column> columns;
    public ArrayList<String> additionalInfo;
    public String sourceHtml;
    private ArrayList<String> warnings;
    private String imageHtml;


    public Timetable(){
        this.lineNumber = UNDEFINED;
        this.vehicleType = VehicleType.UNDEFINED;
        this.busStopName = UNDEFINED;
        this.previousStop = UNDEFINED;
        this.nextStop = UNDEFINED;
        this.columns = new ArrayList<>();
        this.additionalInfo = new ArrayList<>();
        this.sourceHtml = UNDEFINED;
        this.warnings = new ArrayList<>();
        this.imageHtml = null;

    }
    public Timetable(String sourceHtml){
        this();
        this.sourceHtml = sourceHtml;
    }

    /**
     * @return amount of all departures of this line
     */
    public int getDeparturesAmount(){
        int result = 0;
        for(Column column : this.columns) {
            result += column.departures.size();
        }
        return result;
    }

    /**
     * Get amount of departures depending on the day of the week
     * @param day can either be WEEKDAY, SATURDAY, SUNDAY or HOLIDAY
     * @return amount of departures for a given day
     */
    public int getDeparturesAmount(Day day){
        int result = 0;
        for(Column column : this.columns){
            result += column.day == day ? column.departures.size() : 0;
        }
        return result;
    }

    public int getWeekendAverage(){
        int saturdayAmount = this.getDeparturesAmount(Day.SATURDAY);
        int sundayAmount = this.getDeparturesAmount(Day.SUNDAY);
        return (saturdayAmount + sundayAmount) / 2;
    }

    /**
     * Convert rawResult got from BusInfo class to get Timetable object (compatible with new method)
     * @param rawResult to parse. Raw result can be generated using BusInfo.getRawResult() method
     * @return parsed Timetable
     * @throws Exception upon providing incorrect rawResult
     */
    public static Timetable rawResultToTimetable(String rawResult) throws Exception {
        Timetable result = new Timetable();
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(rawResult.split("\n")));
        if(lines.size() < 5) throw new Exception("rawResult not valid");

        result.lineNumber = lines.get(0);
        String vehType = lines.get(1);
        switch(vehType.toLowerCase()){
            case "bus":
                result.vehicleType = VehicleType.BUS;
                break;
            case "light train":
            case "lighttrain":
            case "tram":
            case "tramwaj":
                result.vehicleType = VehicleType.TRAM;
                break;
            default:
                result.vehicleType = VehicleType.UNDEFINED;
                break;
        }
        result.busStopName = lines.get(2);
        for(String columnName : lines.get(3).split("\t")){
            result.columns.add(new Column(columnName));
        }

        ArrayList<String> timetableLines = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(rawResult.split("\n"), 4, rawResult.split("\n").length-1)));
        for(String line : timetableLines){
            line = line.replaceAll("[^\\d\\s]", "");
            ArrayList<String> fields = new ArrayList<>(Arrays.asList(line.split("\t", -1)));
            // if(fields.size() != result.columns.size() + 2 || fields.size() != result.columns.size() + 1) throw new Exception("Columns amount does not match timetable");
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

    public boolean checkColumnNames(){
        if(this.columns.size() == 3
                && this.columns.get(0).getDayType() == Day.WEEKDAY
                && this.columns.get(1).getDayType() == Day.SATURDAY
                && this.columns.get(2).getDayType() == Day.SUNDAY){
            return true;
        }
        if(this.columns.size() == 1 && this.columns.get(0).getDayType() == Day.WEEKDAY) return true;
        if(this.columns.size() > 3) {
            this.warnings.add("Niestandardowa ilość kolumn. Sprawdź adres " + this.sourceHtml);
            return false;
        }
        if(this.columns.size() == 2
                && this.columns.get(0).getDayType() == Day.WEEKDAY
                && (this.columns.get(1).getDayType() == Day.SATURDAY || this.columns.get(1).getDayType() == Day.SUNDAY)){
            return true;
        } else {
            this.warnings.add("Niestandardowy rozkład. Sprawdź adres " + this.sourceHtml);
            return false;
        }
    }

    public void setImageHtml(String html){
        this.imageHtml = html;
    }

    public String getImageHtml(){
        return this.imageHtml;
    }

    public ArrayList<String> getWarnings(){
        return this.warnings;
    }

    public String log(){
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

    public String toString(){
        String result = this.vehicleType + " " + this.lineNumber;
        for(Column column : this.columns){
            result += "\t" + this.getDeparturesAmount(column.day);
        }
        return result;
    }

    public enum Day{
        WEEKDAY,
        SATURDAY,
        SUNDAY
    }

    /**
     * Class representing single column in a timetable
     * It usually contains information about departures for a given day of week (e.g.
     */
    public static class Column {
        private String name;
        public ArrayList<HourMinute> departures;
        private Day day;

        public Column(){
            this.departures = new ArrayList<>();
        }
        public Column(String name){
            this();
            this.setColumnName(name);
        }

        public ArrayList<HourMinute> getDeparturesFromHour(int hour){
            ArrayList<HourMinute> result = new ArrayList<>();
            for(HourMinute time : this.departures)
                if(time.hour == hour) result.add(time);
            return result;
        }

        /**
         * Sets column name and also sets variable that tells which part of the week column belongs to.
         * @param name street name to be saved
         * @return given name
         */
        public String setColumnName(String name){
            this.name = name;
            String lower = this.name.toLowerCase();
            if (lower.contains("dzień powszedni")
                    || lower.contains("dni powszednie")
                    || lower.contains("dzień roboczy")
                    || lower.contains("dni robocze")
                    || lower.contains("weekday")) {
                this.day = Day.WEEKDAY;
            } else if (lower.contains("sobota")
                    || lower.contains("soboty")
                    || lower.contains("saturday")){
                this.day = Day.SATURDAY;
            } else if (lower.contains("niedziele")
                    || lower.contains("niedziela")
                    || lower.contains("sunday")
                    || lower.contains("święto")
                    || lower.contains("święta")
                    || lower.contains("wolny")
                    || lower.contains("wolne")
                    || lower.contains("holiday"))
                this.day = Day.SUNDAY;
            else
                this.day = Day.WEEKDAY;
            return this.name;
        }

        public Day getDayType(){
            return this.day;
        }
    }
}
