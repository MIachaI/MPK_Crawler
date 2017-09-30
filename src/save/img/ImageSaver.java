package save.img;

import businfov2.BusStop;
import businfov2.CertificationMethod;
import businfov2.timetable.Timetable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ImageSaver {
    public static void saveAllImages(String directoryPath, ArrayList<Timetable> timetablesToSave)
    throws CertificationMethod.NotImplementedException, IOException{
        int iterator = 1;
        for(Timetable timetable : timetablesToSave){
            String fullPath = directoryPath + File.separator + String.format(
                    "%03d_%s_%s", iterator, timetable.busStopName.replaceAll("/", "-"), timetable.lineNumber);

            HtmlToImage.generateImage(timetable.generateHtmlTable(), fullPath + ".jpg");
            iterator++;
        }
    }

    /**
     * Save JPg copy of a Timetable.
     * @param timetable to save
     * @param fullPath full path to save image
     * @throws IOException when save fails
     */
    public static void saveImage(Timetable timetable, String fullPath)
    throws IOException {
        HtmlToImage.generateImage(timetable.generateHtmlTable(), fullPath + ".jpg");
    }

    public static void saveImage(Timetable timetable, String directory, String filename)
    throws IOException {
        saveImage(timetable, directory + File.separator + filename);
    }
}
