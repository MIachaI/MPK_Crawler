package save;

import businfov2.timetable.Timetable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class TextSaver {
    /**
     * Method saves links to selected timetables in a text file
     * @param path to saved file
     * @param timetablesToSave
     */
    public static void saveLinksToTextFile(String path, ArrayList<Timetable> timetablesToSave) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        String numeralForm;
        switch(timetablesToSave.size()){
            case 1:
                numeralForm = "rozkład";
                break;
            case 2:
            case 3:
            case 4:
                numeralForm = "rozkłady";
                break;
            default:
                numeralForm = "rozkładów";
                break;
        }

        lines.add("===========================================================================");
        lines.add(String.format("                         Zapisano %d %s jazdy", timetablesToSave.size(), numeralForm));
        lines.add("===========================================================================");
        lines.add("");
        lines.add("Źródła");
        int iterator = 0;
        for(Timetable timetable : timetablesToSave){
            lines.add("----");
            lines.add(String.format("%s %s", timetable.lineNumber, timetable.busStopName));
            lines.add(String.format("Obrazek: %03d_%s_%s", ++iterator, timetable.busStopName.replaceAll("/", "-"), timetable.lineNumber));
            lines.add("Źródło:");
            lines.add(timetable.sourceHtml);
        }
        lines.add("----");

        Path file = Paths.get(path);
        Files.write(file, lines, Charset.forName("UTF-8"));
    }
}
