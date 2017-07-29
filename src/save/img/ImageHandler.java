package save.img;

import businfo.busstop.BusInfo;
import businfo.lists.ListContainer;
import businfo.lists.SelectedBusStopsHandler;
import window_interface.dialogs.ErrorDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by umat on 26.05.17.
 */
public class ImageHandler {
    public static void saveAllImages(SelectedBusStopsHandler selectedStops, String path, boolean purified) throws IOException {
        ArrayList<BusInfo> selectedTrams;
        ArrayList<BusInfo> selectedBuses;

        if(purified){
            selectedTrams = selectedStops.getOnlyTrams();
            selectedBuses = selectedStops.getOnlyBuses();
        } else {
            selectedTrams = selectedStops.getOnlyTramsNonPurified();
            selectedBuses = selectedStops.getOnlyBusesNonPurified();
        }

        int iterator = 1;
        for(BusInfo tram : selectedTrams){
            String fullPath = path + String.format("%03d", iterator) + "_" + tram.getStreetName().replaceAll("/", "-") + "_" + tram.getLineNumberString();
            try { HtmlToImage.imageGenerator(tram.getRawHtml(), fullPath); }
            catch (NullPointerException e) {
                ErrorDialog.displayError(
                        "Błąd zapisu rozkładu do obrazu .jpg",
                        "Błąd wystąpił przy próbie zapisu przystanku " + tram.getStreetName());
                continue;
            }
            iterator++;
        }
        iterator = 1;
        for(BusInfo bus : selectedBuses){
            String fullPath = path + String.format("%03d", iterator) + "_" + bus.getStreetName().replaceAll("/", "-") + "_" + bus.getLineNumberString();
            try { HtmlToImage.imageGenerator(bus.getRawHtml(), fullPath); }
            catch (NullPointerException e) {
                ErrorDialog.displayError(
                        "Błąd zapisu rozkładu do obrazu .jpg",
                        "Błąd wystąpił przy próbie zapisu " + bus.getStreetName());
                continue;
            }
            iterator++;
        }
    }

    public static void saveAllImages(SelectedBusStopsHandler selectedStops, String path) throws IOException {
        saveAllImages(selectedStops, path, true);
    }
}
