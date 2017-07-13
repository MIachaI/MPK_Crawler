package businfo.lists;

public class BusStopLink {
    private String link;
    private String name;

    public BusStopLink(String link, String name){
        this.link = link;
        this.name = name;
    }
    // GETTERS
    public String toString(){
        return this.name + " " + this.link;
    }
    public String getName(){
        return this.name;
    }
    public String getLink(){
        return this.link;
    }
}
