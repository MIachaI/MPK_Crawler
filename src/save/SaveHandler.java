package save;

import businfo.lists.ListContainer;
import save.excel.ExcelHandler;
import save.img.ImageHandler;

import java.io.IOException;

/**
 * Created by umat on 26.05.17.
 */
public class SaveHandler {
    public static void saveAll(ListContainer listContainer, String path) throws IOException {
        ImageHandler.saveAllImages(listContainer, path);
        ExcelHandler.saveExcel(listContainer, path);
    }
}
