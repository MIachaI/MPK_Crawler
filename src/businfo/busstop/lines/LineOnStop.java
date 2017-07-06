package businfo.busstop.lines;

/**
 * Created by umat on 06.07.17.
 */
public class LineOnStop {
    /**
     * LineOnStop number stored as a string since some cities put letter prefix before line number (eg. N85)
     */
    private String number;
    /**
     * link to a specific timetable
     */
    private String link;
    /**
     * Starting point of bus line
     */
    private String start;
    /**
     * street name assigned to the link
     */
    private  String stop;
    /**
     * Ending point
     */
    private String end;

    /**
     *
     * @param number LineOnStop number stored as a string since some cities put letter prefix before line number (eg. N85)
     * @param link html link to the site with bus schedule on bus stop defined in "stop" parameter
     * @param stop street name assigned to the link
     * @param start Starting point of bus line
     * @param stop Ending point
     */
    public LineOnStop(String number, String link, String stop, String start, String end){
        this.number = number;
        this.link = link;
        this.start = start;
        this.stop = stop;
        this.end = end;
    }
    public LineOnStop(String number, String link, String stop){
        this(number, link, stop, "", "");
    }
    public LineOnStop(String number, String link){
        this(number,link,"", "", "");
    }
    public LineOnStop(String number){
        this(number, "","", "", "");
    }

    public String getNumber() {
        return number;
    }
    public String getLink() {
        return link;
    }
    public String getStart() {
        return start;
    }
    public String getStop() {
        return stop;
    }
    public String getEnd() {
        return end;
    }
}
