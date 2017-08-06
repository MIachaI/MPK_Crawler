package businfov2.parsers;

import businfo.busstop.MPKinfo;
import businfo.busstop.ZTMinfo;
import businfov2.timetable.Timetable;
import org.jsoup.Jsoup;

/**
 * Class to parse info from website to Timetable object
 */
public abstract class Parser {
    public static Timetable krakowParser(String link) throws Exception {
        MPKinfo info = new MPKinfo();
        String rawResult = info.getRawResult(Jsoup.connect(link));
        String[] lines = rawResult.split("\n");
        lines[3] = lines[3].replaceAll("Godzina\t", "");
        rawResult = "";
        for(String line : lines){
            rawResult += line + "\n";
        }
        Timetable result = Timetable.rawResultToTimetable(rawResult);
        result.sourceHtml = link;
        return result;
    }

    public static Timetable warszawaParser(String link) throws Exception {
        ZTMinfo info = new ZTMinfo();
        String rawResult = info.getRawResult(Jsoup.connect(link));
        Timetable result = Timetable.rawResultToTimetable(rawResult);
        result.sourceHtml = link;
        return result;
    }

    public static Timetable poznanParser(String link){
        return null;
    }

    public static Timetable wroclawParser(String link){
        return null;
    }
}
