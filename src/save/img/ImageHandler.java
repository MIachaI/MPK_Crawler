package save.img;

import businfo.busstop.BusInfo;
import businfo.lists.ListContainer;

import java.io.IOException;

/**
 * Created by umat on 26.05.17.
 */
public class ImageHandler {
    public static void saveAllImages(ListContainer listContainer, String path) throws IOException {
       String clearedPath = path.substring(0,path.length()-4);
        System.out.println(clearedPath);
        for(BusInfo tram : listContainer.getOnlyTrams()){
            HtmlToImage.imageGenerator(tram.getRawHtml(), clearedPath +" linia "+ tram.getLineNumberString());
        }
        for(BusInfo bus : listContainer.getOnlyBuses()){
            HtmlToImage.imageGenerator(bus.getRawHtml(), clearedPath +" linia "+ bus.getLineNumberString());
        }
    }
}
