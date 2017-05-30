package save.img;

import businfo.busstop.BusInfo;
import businfo.lists.ListContainer;

import java.io.File;
import java.io.IOException;

/**
 * Created by umat on 26.05.17.
 */
public class ImageHandler {
    public static void saveAllImages(ListContainer listContainer, String path) throws IOException {
        int iterator = 1;
        for(BusInfo tram : listContainer.getOnlyTrams()){
            String fullPath = path + String.format("%03d", iterator) + "_" + tram.getStreetName()+ "_" + tram.getLineNumberString();
            HtmlToImage.imageGenerator(tram.getRawHtml(), fullPath);
            iterator++;
        }
        iterator = 1;
        for(BusInfo bus : listContainer.getOnlyBuses()){
            String fullPath = path + String.format("%03d", iterator) + "_" + bus.getStreetName() + "_" + bus.getLineNumberString();
            HtmlToImage.imageGenerator(bus.getRawHtml(), fullPath);
            iterator++;
        }
    }
}
