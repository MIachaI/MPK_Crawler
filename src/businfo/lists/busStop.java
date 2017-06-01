package businfo.lists;

public class busStop {
String link;
String name;

public busStop(String link, String name){
    this.link = link;
    this.name = name;
}

    public String toString(){return this.name + " " + this.link;}
    public String toName(){return this.name;}
    public String toLink(){return this.link;}
}
