package save;

import businfov2.BusStop;
import businfov2.CertificationMethod;
import save.excel.ExcelSaver;
import save.img.ImageSaver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Saver {public static String excelName;
    public static String imgPrefix;
    static{
        excelName = "Sheet";
        imgPrefix = "";
    }
    public static void saveAll(String path, ArrayList<BusStop> selectedStops, CertificationMethod method)
    throws CertificationMethod.NotImplementedException, IOException {
        new File(path).mkdir();
        ImageSaver.saveAllImages(path + File.separator + imgPrefix, selectedStops, method);
        ExcelSaver.save(path + File.separator + excelName, selectedStops, method);
    }
}
