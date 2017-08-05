package businfov2;

public enum VehicleType {
    BUS,
    TRAM,
    UNDEFINED;

    public String toString(){
        switch(this){
            case BUS:
                return "Bus";
            case TRAM:
                return "Light train";
            case UNDEFINED:
                return "undefined";
            default:
                return "undefined";
        }
    }
}