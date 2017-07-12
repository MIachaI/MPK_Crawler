package save.json;

import businfo.busstop.lines.LineOnStop;
import businfo.busstop.streets.BusStop;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
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
     * @throws NullPointerException if city name was not found in the json file
     */
    public static ArrayList<BusStop> fetchBusStopArray(String jsonFile, String city) throws IOException, ParseException, NullPointerException {
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
            JSONObject line = (JSONObject) busStops.get(keyStr);
            Set lineKeySet = line.keySet();
            for(Object lineKey : lineKeySet){
                // line number
                String lineKeyStr = (String) lineKey;
                // link to schedule
                String lineKeyVal = (String) line.get(lineKeyStr);
                busStopToAdd.addBusLine(new LineOnStop(lineKeyStr, lineKeyVal));
            }

            result.add(busStopToAdd);
        }

        return result;
    }

    /**
     * Generate JSON object for given bus stop list.
     * Usage: provide bus stop list and get JSONObject that you can later use to update json file with data for one city
     * @param busStops ArrayList with bus stops info (should be list for one city)
     * @return JSONObject containing info about bus stops eg. in a city
     */
    public static JSONObject generateCityObject(ArrayList<BusStop> busStops){
        JSONObject result = new JSONObject();
        for(BusStop busStop : busStops){
            JSONObject linesObj = new JSONObject();
            for(LineOnStop line : busStop.getBusLines()){
                // line info (number, link)
                linesObj.put(line.getNumber(), line.getLink());
            }
            // bus stop info (name, line info)
            result.put(busStop.getStreetName(), linesObj);
        }

        return result;
    }

    /**
     * Override data for a given city with provided list (JSONObject, see class method generateCityObject())
     * @param fileName json file name
     * @param cityName
     * @param cityObject city object generated via class method generateCityObject()
     * @throws IOException
     * @throws ParseException
     */
    public static void updateJSONFile(String fileName, String cityName, JSONObject cityObject) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject mainObj = (JSONObject) parser.parse(new FileReader(fileName));
        mainObj.put(cityName, cityObject);

        FileWriter writer = new FileWriter(fileName);
        writer.write(mainObj.toJSONString());
        writer.close();
    }
}
