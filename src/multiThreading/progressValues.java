package multiThreading;

/**
 * Created by MIachal on 24.09.2017.
 */
public class progressValues {

    private static int progressValue = 0;
    private static int progressValueLength = 1;

    // SETTERS
    public static void progressValues(int updateValue) {
        progressValue = updateValue;
    }
    public static void progressValues(int updateValue, int setMaxValueSize ){
        progressValue = updateValue;
        progressValueLength = setMaxValueSize;
    }
    public static void setValue(int updateValue){
        progressValue=updateValue;
    }

    // GETTERS
    public static int getValue(){
        return progressValue;
    }
    public static  int getValueLength(){
        return progressValueLength;
    }
}