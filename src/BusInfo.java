import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.io.IOException;
import java.util.Arrays;

abstract class BusInfo {
    private String html;
    protected int lineNumber;
    protected String vehicleType;
    protected String streetName;
    protected ArrayList<String> columnNames;
    protected String rawResult;
    protected String additionalInfo;
    protected ArrayList<String> warnings;
    // storing hours and minutes
    protected ArrayList<HourMinute> weekdayList;
    protected ArrayList<HourMinute> saturdayList;
    protected ArrayList<HourMinute> sundayList;

    BusInfo(){
        this.weekdayList = new ArrayList<HourMinute>();
        this.saturdayList = new ArrayList<HourMinute>();
        this.sundayList = new ArrayList<HourMinute>();
        this.columnNames = new ArrayList<>();
    }

    BusInfo(String html) throws IOException {
        this();
        setHTML(html);
    }

    public void setHTML(String html) throws IOException {
        this.html = html;
        this.rawResult = getRawResult(this.html);
        this.count(this.rawResult);
        this.lineNumber = findLineNumber();
        this.vehicleType = findVehicleType();
        this.streetName = findStreetName();
        this.columnNames = findColumnNames();
        this.additionalInfo = findAdditionalInfo();
    }
    /**
     * Get information from table posted on MPK site as a string.
     * Information has to be ordered in new lines as follows:<br/>
     *  * Line number (first line)<br/>
     *  * Vehicle type: bus/tram (second line)<br/>
     *  * Bus-stop name (third line)<br/>
     *  * Column titles (fourth line)<br/>
     *  * Following lines - timetable (columns separated with tabulation, rows separated with newlines)<br/>
     *  * Last line contains additional info<br/>
     * @param html page adress from which to get info
     * @return info table formatted as string where columns are separated with tabulation and rows - with new lines
     * @throws IOException
     */
    abstract String getRawResult(String html) throws IOException;

    /**
     * Saves found information to suitable class fields based on rawResult provided as a parameter
     * * weekdayList
     * * saturdayList
     * * sundayList
     * @throws IOException
     */
    protected void count(String rawResult){
        // clear lists if they are not empty
        this.weekdayList.clear();
        this.saturdayList.clear();
        this.sundayList.clear();

        //store information in MPKinfo class

        // cut first 4 and last 2 rows - they contain other informations
        String[] lines = Arrays.copyOfRange(rawResult.split("\n"), 4, rawResult.split("\n").length-1);
        for(String line : lines){
            int rowHour=0;
            line = line.replaceAll("[^\\d^\\s]", "");
            //get columns
            String[] columns = line.split("\t");
            int colIterator = 0;
            for(String column : columns){
                String[] minutes = column.split(" ");

        		/*
                 * Column 0: Hour
                 * Column 1: Minute (weekday)
                 * Column 2: Minute (Saturday)
                 * Column 3: Minute (Sunday)
                 */
                switch(colIterator){
                    case 0:
                        if(StringUtils.isNotBlank(column))
                            rowHour = Integer.parseInt(column);
                        break;
                    case 1:
                        for(String minute : minutes){
                            if(StringUtils.isNotBlank(minute))
                                this.addWeekdayCourse(new HourMinute(
                                        rowHour,
                                        Integer.parseInt(minute)
                                ));
                        }
                        break;
                    case 2:
                        for(String minute : minutes){
                            if(StringUtils.isNotBlank(minute))
                                this.addSaturdayCourse(new HourMinute(
                                        rowHour,
                                        Integer.parseInt(minute)
                                ));
                        }
                        break;
                    case 3:
                        for(String minute : minutes){
                            if(StringUtils.isNotBlank(minute))
                                this.addSundayCourse(new HourMinute(
                                        rowHour,
                                        Integer.parseInt(minute)
                                ));
                        }
                        break;
                }
                colIterator++;
            }
        }
    }

    // add new course to suitable list
    /**
     * Add new item to the list storing timetable for weekday
     * @param time that bus leaves bus stop
     */
    protected void addWeekdayCourse(HourMinute time){
        this.weekdayList.add(time);
    }
    /**
     * Add new item to the list storing timetable for Saturdays
     * @param time that bus leaves bus stop
     */
    protected void addSaturdayCourse(HourMinute time){
        this.saturdayList.add(time);
    }
    /**
     * Add new item to the list storing timetable for weekday
     * @param time that bus leaves bus stop
     */
    protected void addSundayCourse(HourMinute time){
        this.sundayList.add(time);
    }


    protected int findLineNumber(){
        return Integer.parseInt(this.rawResult.split("\n")[0]);
    }
    protected String findVehicleType(){
        return this.rawResult.split("\n")[1];
    }
    protected String findStreetName(){
        return this.rawResult.split("\n")[2];
    }
    protected ArrayList<String> findColumnNames(){
        ArrayList<String> columnNames = new ArrayList<>();
        columnNames.addAll(Arrays.asList(this.rawResult.split("\n")[3].split("\t"))); // add all detected column names to the list
        return columnNames;
    }
    protected String findAdditionalInfo(){
        String[] lines = this.rawResult.split("\n");
        return lines[lines.length - 1];
    }

    // SETTERS
    public void setWeekdayList(ArrayList<HourMinute> list){
        this.weekdayList = list;
    }
    public void setSaturdayList(ArrayList<HourMinute> list){
        this.saturdayList = list;
    }
    public void setSundayList(ArrayList<HourMinute> list){
        this.sundayList = list;
    }

    // GETTERS
    public ArrayList<HourMinute> getWeekdayList(){
        return this.weekdayList;
    }
    public ArrayList<HourMinute> getSaturdayList(){
        return this.saturdayList;
    }
    public ArrayList<HourMinute> getSundayList(){
        return this.sundayList;
    }

    /**
     * @return number of bus courses during a weekday
     */
    public int getWeekdayCourseCount(){
        return weekdayList.size();
    }
    /**
     * @return number of bus courses during Saturday
     */
    public int getSaturdayCourseCount(){
        return saturdayList.size();
    }
    /**
     * @return number of bus courses during Sunday
     */
    public int getSundayCourseCount(){
        return sundayList.size();
    }

    /**
     * @return Bus or tram line number
     */
    public int getLineNumber(){
        return this.lineNumber;
    }
    /**
     * @return vehicle type as a string. Can be either bus or tram
     */
    public String getVehicleType(){
        return this.vehicleType;
    }
    /**
     * @return name of the bus stop or name of the street
     */
    public String getStreetName(){
        return this.streetName;
    }
    public ArrayList<String> getColumnNames(){
        return this.columnNames;
    }
    public String getAdditionalInfo(){
        return this.additionalInfo;
    }

    public String toString(){
        String result = "";
        result += this.getLineNumber() +
                "\t" + this.getStreetName() +
                "\t" + this.getVehicleType() +
                "\t" +                                      // empty cell for distance from building
                "\t" + this.getWeekdayCourseCount() +
                "\t" + this.getSaturdayCourseCount() +
                "\t" + this.getSundayCourseCount() +
                "\t" + (this.getSaturdayCourseCount() + this.getSundayCourseCount())/2 + "\n"; // weekend average

        return result;
    }
}
