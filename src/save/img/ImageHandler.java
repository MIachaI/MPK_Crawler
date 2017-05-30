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
        String bufor ="";
        new File(path).mkdir();

        for(BusInfo tram : listContainer.getOnlyTrams()){
            if(bufor.equals(tram.getLineNumberString()))  HtmlToImage.imageGenerator(tram.getRawHtml(), path +"\\"+tram.getStreetName()+"_"+tram.getLineNumberString()+"_2");
            else  HtmlToImage.imageGenerator(tram.getRawHtml(), path +"\\"+tram.getStreetName()+"_"+tram.getLineNumberString()+"_1");
            bufor =tram.getLineNumberString();
        }
        for(BusInfo bus : listContainer.getOnlyBuses()){
            if(bufor.equals(bus.getLineNumberString()))  HtmlToImage.imageGenerator(bus.getRawHtml(), path +"\\"+bus.getStreetName()+"_"+bus.getLineNumberString()+"_2");
            else  HtmlToImage.imageGenerator(bus.getRawHtml(), path +"\\"+bus.getStreetName()+"_"+bus.getLineNumberString()+"_1");
            bufor =bus.getLineNumberString();

        }
    }
}
