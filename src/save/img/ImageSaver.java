package save.img;

import businfov2.BusStop;
import businfov2.CertificationMethod;
import businfov2.timetable.Timetable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ImageSaver {
    public static void saveAllImages(String directoryPath, ArrayList<BusStop> stops, CertificationMethod method)
    throws CertificationMethod.NotImplementedException, IOException{
        int iterator = 1;
        for(BusStop stop : stops){
            for(Timetable timetable : stop.getTimetables(method)){
                String fullPath = directoryPath + File.separator + String.format(
                        "%03d_%s_%s", iterator, timetable.busStopName.replaceAll("/", "-"), timetable.lineNumber);

                HtmlToImage.generateImage(timetable.generateHtmlTable(), fullPath + ".jpg");
                iterator++;
            }
        }
    }
}
