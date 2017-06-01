package businfo.lists;

public class busStop {
public String link;
public String name;

public busStop(String a, String b){
    this.link = b;
    this.name = a;
}

    public String toString(){
        return this.name + " " + this.link;
    }
    public String toLink(){return this.link;}
    public String toName(){return this.name;}
}
