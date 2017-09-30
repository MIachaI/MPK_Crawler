package tasks;

import businfo.busstop.lines.LineOnStop;
import businfo.busstop.streets.BusStop;
import businfov2.CertificationMethod;
import businfov2.City;
import businfov2.parsers.Parser;
import businfov2.timetable.Timetable;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class SaveAllTask extends Task {
    private City city;
    private ArrayList<businfo.busstop.streets.BusStop> selectedStops;

    public SaveAllTask(City city, ArrayList<businfo.busstop.streets.BusStop> selectedStops)
    throws City.NotImplementedException {
        this.city = city;
        this.city.isImplemented();
        this.selectedStops = selectedStops;
    }

    @Override
    protected Object call() throws Exception {
        // variables keeping track of the progress
        long maxWork = 0;
        long workDone = 0;

        // get size of maxWork
        for(BusStop stop : this.selectedStops){
            maxWork += stop.getBusLines().size();
        }

        ArrayList<businfov2.BusStop> busStops = new ArrayList<>();
        for(BusStop stop : this.selectedStops){
            ArrayList<Timetable> timetablesToAdd = new ArrayList<>();
            for(LineOnStop line : stop.getBusLines()){
                // update progress message
                this.updateMessage(String.format("Pobieranie: %s %s", line.getNumber(), stop.getStreetName()));

                // read HTML contents
                timetablesToAdd.add(Parser.parse(line.getLink(), city ));

                // update progress
                this.updateProgress(++workDone, maxWork);
            }

            busStops.add(new businfov2.BusStop(stop.getStreetName(), timetablesToAdd));
        }

        this.updateMessage("");

        // now we have bus stops - we can filter them by certification method and start generating images
        // Saver.saveAll(this.selectedDirectory, busStops, this.method);

        return busStops;
    }
}
