package businfo.lists;

public class busStop {
String link;
String name;

    /**
     * SETTERS
     * @param link
     * @param name
     */
    public busStop(String link, String name){
    this.link = link;
    this.name = name;
}

    /**
     * GETTERS
     * @return
     */
    public String toString(){return this.name + " " + this.link;}
    public String toName(){return this.name;}
    public String toLink(){return this.link;}
}
