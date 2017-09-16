package businfo.site_scanner;

import businfo.busstop.streets.BusStop;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

/**
 * Created by umat on 13.07.17.
 */
public abstract class SiteScanner {
    protected String mainPageURL;
    protected ArrayList<BusStop> busStops;

    public SiteScanner(){
        this.busStops = new ArrayList<>();
    }

    /**
     * Scan whole site and look for desired information
     * @return array with BusStop objects containing information about all bus stops in the city with lines that have courses on them
     */
    public abstract ArrayList<BusStop> scan() throws ConnectException, IOException, ParseException;
}
