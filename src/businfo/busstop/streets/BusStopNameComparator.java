package businfo.busstop.streets;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class BusStopNameComparator implements Comparator<BusStop> {

    @Override
    public int compare(BusStop busStop, BusStop t1) {
        String busStopName = busStop.getStreetName();
        String t1Name = t1.getStreetName();

        Collator collator = Collator.getInstance(new Locale("pl", "PL"));
        //Collator collator = Collator.getInstance();

        return collator.compare(busStopName, t1Name);
    }
}
