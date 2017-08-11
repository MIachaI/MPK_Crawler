package businfov2.parsers;

import businfo.busstop.MPKPoznanInfo;
import businfo.busstop.MPKWroclaw;
import businfo.busstop.MPKinfo;
import businfo.busstop.ZTMinfo;
import businfov2.City;
import businfov2.timetable.Timetable;
import org.jsoup.Jsoup;

/**
 * Class to parse info from website to Timetable object
 */
public abstract class Parser{
    public static Timetable krakowParser(String link) throws Exception {
        MPKinfo info = new MPKinfo(link);
        String rawResult = info.getRawResult(Jsoup.connect(link));
        String[] lines = rawResult.split("\n");
        lines[3] = lines[3].replaceAll("Godzina\t", "");
        rawResult = "";
        for(String line : lines){
            rawResult += line + "\n";
        }
        Timetable result = Timetable.rawResultToTimetable(rawResult);
        result.sourceHtml = link;

        // also save image html
        result.setImageHtml(ImageGetter.krakowImage(link));

        return result;
    }

    public static Timetable warszawaParser(String link) throws Exception {
        ZTMinfo info = new ZTMinfo(link);
        String rawResult = info.getRawResult(Jsoup.connect(link));
        Timetable result = Timetable.rawResultToTimetable(rawResult);
        result.sourceHtml = link;
        result.setImageHtml(ImageGetter.warszawaImage(link));

        return result;
    }

    public static Timetable poznanParser(String link) throws Exception {
        MPKPoznanInfo info = new MPKPoznanInfo(link);
        String rawResult = info.getRawResult(Jsoup.connect(link));
        Timetable result = Timetable.rawResultToTimetable(rawResult);
        result.sourceHtml = link;
        result.setImageHtml(ImageGetter.poznanImage(link));

        return result;
    }

    public static Timetable wroclawParser(String link) throws Exception {
        MPKWroclaw info = new MPKWroclaw(link);
        String rawResult = info.getRawResult(Jsoup.connect(link));
        Timetable result = Timetable.rawResultToTimetable(rawResult);
        result.sourceHtml = link;
        result.setImageHtml(ImageGetter.wroclawImage(link));

        return result;
    }

    public static Timetable parse(String link, City city) throws Exception {
        switch(city){
            case KRAKOW:
                return krakowParser(link);
            case WARSZAWA:
                return warszawaParser(link);
            case POZNAN:
                return poznanParser(link);
            case WROCLAW:
                return wroclawParser(link);
        }
        return new Timetable();
    }
}
