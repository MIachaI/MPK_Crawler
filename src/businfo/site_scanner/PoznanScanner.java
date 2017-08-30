package businfo.site_scanner;

import businfo.busstop.lines.LineOnStop;
import businfo.busstop.streets.BusStop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import businfo.busstop.lines.LineOnStop;
import businfo.busstop.streets.BusStop;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Created by MIachaI on 06.08.2017.
 */
public class PoznanScanner extends SiteScanner {
    //public BusStop busStop;
    public PoznanScanner(){
        super();
        this.mainPageURL = "http://www.mpk.poznan.pl/rozklad-jazdy";
    }
    @Override
    public ArrayList<BusStop> scan() throws Exception {
        Set<String> busStopNames = new HashSet<>();
        ArrayList<BusStop> result = new ArrayList<>();
        JSONParser parser = new JSONParser();
        String poznan = readUrl("http://www.poznan.pl/mim/plan/map_service.html?mtype=pub_transport&co=cluster");
        Object primarObject = parser.parse(poznan);

        JSONObject obj = (JSONObject) primarObject;
        Object initialMatch = obj.get("features");
        String linkPattern = "http://www.mpk.poznan.pl/component/transport/";
        String buffer ="Aleje Solidarności";
        BusStop busStop = new BusStop("Aleje Solidarnosci");
        for (Object jsonObject : (JSONArray) initialMatch){

            Object jsonProperties = ((JSONObject) jsonObject).get("properties");
            Object jsonID = ((JSONObject) jsonObject).get("id");
            String jsonUpdatedID = jsonID.toString().replaceAll(" ", "");

            Object jsonStopName = ((JSONObject) jsonProperties).get("stop_name");
            Object jsonLines = ((JSONObject) jsonProperties).get("headsigns");
            String jsonLinesPurified = jsonLines.toString().replaceAll(" ", "");
            String[] linesArray = jsonLinesPurified.split(",");

            if( buffer.equals(jsonStopName.toString())==false){
                result.add(busStop);
                busStop = new BusStop(jsonStopName.toString());
                buffer = jsonStopName.toString();
          }
            for (String line : linesArray){
                String linkToTimetable = linkPattern + line +"/" + jsonUpdatedID;
                busStop.addBusLine(new LineOnStop(line, linkToTimetable));
            }
        }
        result.add(busStop);
        return result;
    }
    private static String readUrl(String input) throws Exception {
        BufferedReader reader = null;

            URL url = new URL(input);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();

    }

}
