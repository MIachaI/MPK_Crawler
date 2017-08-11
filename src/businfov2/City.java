package businfov2;

import java.util.ArrayList;

public enum City {
    KRAKOW(true, "Kraków", "Cracow", "http://rozklady.mpk.krakow.pl/"),
    WARSZAWA(true, "Warszawa", "Warsaw", "http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1"),
    POZNAN(false, "Poznań", "Poznań", "http://www.mpk.poznan.pl/rozklad-jazdy"),
    WROCLAW(true, "Wrocław", "Wroclaw", "http://www.wroclaw.pl/rozklady-jazdy");

    private String plName;
    private String engName;
    private boolean implemented;
    private String mainPageHtml;
    City(boolean implemented, String plName, String engName, String mainPageHtml){
        this.implemented = implemented;
        this.plName = plName;
        this.engName = engName;
        this.mainPageHtml = mainPageHtml;
    }

    public String toString(){
        return this.plName;
    }

    /**
     * Method to call if we want to check if city is implemented
     * @throws Exception if city is not implemented, else it lets program to continue
     */
    public void isImplemented() throws Exception {
        if(!this.implemented) throw new Exception("This city is not implemented");
    }

    public String getEngName(){
        return this.engName;
    }

    public String getMainPageHtml(){
        return this.mainPageHtml;
    }

    public static ArrayList<City> getImplemented(){
        ArrayList<City> result = new ArrayList<>();
        for(City city : City.values()){
            if(city.implemented) result.add(city);
        }
        return result;
    }

    public static ArrayList<String> getImplementedNames(){
        ArrayList<String> result = new ArrayList<>();
        for(City city : City.getImplemented()){
            result.add(city.toString());
        }
        return result;
    }

    public static City stringToEnum(String cityName) throws Exception {
        switch(cityName.toLowerCase()){
            case "kraków":
            case "krakow":
            case "cracow":
                return KRAKOW;
            case "warszawa":
            case "warsaw":
                return WARSZAWA;
            case "poznań":
            case "poznan":
                return POZNAN;
            case "wrocław":
            case "wroclaw":
            case "breslau":
                return WROCLAW;
            default:
                throw new Exception("Invalid city");
        }
    }
}
