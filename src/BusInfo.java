import java.util.ArrayList;

/**
 * Created by umat on 01.05.17.
 */
class BusInfo {
    String html;
    protected int lineNumber;
    protected String streetName;
    protected String rawResult;
    protected String additionalInfo;
    // storing hours and minutes
    protected ArrayList<HourMinute> weekdayList;
    protected ArrayList<HourMinute> saturdayList;
    protected ArrayList<HourMinute> sundayList;

    // add new course to suitable list
    public void addWeekdayCourse(HourMinute time){
        this.weekdayList.add(time);
    }
    public void addSaturdayCourse(HourMinute time){
        this.saturdayList.add(time);
    }
    public void addSundayCourse(HourMinute time){
        this.sundayList.add(time);
    }

    // get desired list
    public ArrayList<HourMinute> getWeekdayList(){
        return this.weekdayList;
    }
    public ArrayList<HourMinute> getSaturdayList(){
        return this.saturdayList;
    }
    public ArrayList<HourMinute> getSundayList(){
        return this.sundayList;
    }

    // methods that count elements in the lists.
    public int getWeekdayCourseCount(){
        return weekdayList.size();
    }
    public int getSaturdayCourseCount(){
        return saturdayList.size();
    }
    public int getSundayCourseCount(){
        return sundayList.size();
    }

    public int getLineNumber(){
        return this.lineNumber;
    }
    public String getStreetName(){
        return this.streetName;
    }

    public String toString(){
        String result = "";
        result += "Linia: " + getLineNumber() + "\tPrzystanek: " + getStreetName();
        result += "\n----------------------------";
        result += "\nIlości kursów: ";
        result +=
                "\nDni robocze:\t\t" + getWeekdayCourseCount() +
                        "\nSoboty:\t\t\t\t" + getSaturdayCourseCount() +
                        "\nNiedziele:\t\t\t" + getSundayCourseCount() +
                        "\nWeekend (średnia):\t" + (getSaturdayCourseCount() + getSundayCourseCount())/2;
        result += "\n----------------------------";

        return result;
    }
}
