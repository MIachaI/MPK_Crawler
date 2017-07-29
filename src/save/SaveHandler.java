package save;

import businfo.lists.SelectedBusStopsHandler;
import save.excel.ExcelHandler;
import save.img.ImageHandler;

import java.io.File;
import java.io.IOException;

/**
 * Created by umat on 26.05.17.
 */
public class SaveHandler{
    public static String excelName;
    public static String imgPrefix;
    static{
        excelName = "Sheet";
        imgPrefix = "";
    }
    public static void saveAll(SelectedBusStopsHandler listContainer, String path) throws IOException {
        new File(path).mkdir();
        ImageHandler.saveAllImages(listContainer, path + "/" + imgPrefix);
        ExcelHandler.saveExcel(listContainer, path + "/" + excelName);
    }
}
