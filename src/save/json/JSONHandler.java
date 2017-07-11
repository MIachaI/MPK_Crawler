package save.json;

import businfo.busstop.lines.LineOnStop;
import businfo.busstop.streets.BusStop;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by umat on 12.07.17.
 */
public abstract class JSONHandler {

    /**
     * Get ArrayList with BusStop class from given json file in given city
     * @param jsonFile path to file containing json
     * @param city city from which to fetch info
     * @return ArrayList containing BusStop objects for a city
     * @throws IOException upon no file found
     * @throws ParseException when wrong format
     */
    public static ArrayList<BusStop> fetchBusStopArray(String jsonFile, String city) throws IOException, ParseException {
        ArrayList<BusStop> result = new ArrayList<>();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(jsonFile));
        JSONObject jsonObject = (JSONObject) obj;

        JSONObject busStops = (JSONObject) jsonObject.get(city);
        // set with key names in city object (key names = bus stop names)
        Set keySet = busStops.keySet();
        for (Object key : keySet) {
            String keyStr = (String) key;
            BusStop busStopToAdd = new BusStop(keyStr);
            Object keyVal = busStops.get(keyStr);
            JSONArray linesArray = (JSONArray) keyVal;
            for (Object item : linesArray) {
                JSONObject line = (JSONObject) item;
                Set lineKeySet = line.keySet();
                for(Object lineKey : lineKeySet){
                    // line number
                    String lineKeyStr = (String) lineKey;
                    // link to schedule
                    String lineKeyVal = (String) line.get(lineKeyStr);
                    busStopToAdd.addBusLine(new LineOnStop(lineKeyStr, lineKeyVal));
                }
            }

            result.add(busStopToAdd);
        }

        return result;
    }
}
