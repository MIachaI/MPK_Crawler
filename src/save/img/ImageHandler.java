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
        new File(path).mkdir();

        for(BusInfo tram : listContainer.getOnlyTrams()){
            HtmlToImage.imageGenerator(tram.getRawHtml(), path +"\\"+tram.getStreetName()+"_"+tram.getLineNumberString());
        }
        for(BusInfo bus : listContainer.getOnlyBuses()){
            HtmlToImage.imageGenerator(bus.getRawHtml(), path +"\\"+bus.getStreetName()+"_"+bus.getLineNumberString());
        }
    }
}
