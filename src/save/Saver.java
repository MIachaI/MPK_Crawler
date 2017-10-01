package save;

import businfov2.BusStop;
import businfov2.CertificationMethod;
import businfov2.timetable.Timetable;
import save.excel.ExcelSaver;
import save.img.ImageSaver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Saver {
    public static String excelName;
    public static String imgPrefix;
    public static String logName;
    static {
        excelName = "Sheet";
        imgPrefix = "";
        logName = "linki.txt";
    }

    public static ArrayList<Timetable> getTimetablesToSave(ArrayList<BusStop> selectedStops, CertificationMethod method)
    throws CertificationMethod.NotImplementedException {
        ArrayList<Timetable> result = new ArrayList<>();
        for(BusStop busStop : selectedStops){
            result.addAll(busStop.getTimetables(method));
        }
        return result;
    }

    public static void saveAll(String path, ArrayList<BusStop> selectedStops, CertificationMethod method)
    throws CertificationMethod.NotImplementedException, IOException {
        new File(path).mkdir();
        ArrayList<Timetable> timetablesToSave = getTimetablesToSave(selectedStops, method);
        ImageSaver.saveAllImages(path + File.separator + imgPrefix, timetablesToSave);
        TextSaver.saveLinksToTextFile(path + File.separator + logName, timetablesToSave);
        ExcelSaver.save(path + File.separator + excelName, selectedStops, method);
        System.out.println("Save successful");
    }
}
