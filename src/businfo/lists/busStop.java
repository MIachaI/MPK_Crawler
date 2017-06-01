package businfo.lists;

public class busStop {
public String link;
public String name;

public busStop(String link, String name){
    this.link = link;
    this.name = name;
}

    public String toString(){
        return this.name + " " + this.link;
    }
    public String toLink(){return this.link;}
    public String toName(){return this.name;}
}
