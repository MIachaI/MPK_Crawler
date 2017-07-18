package save;

import businfo.lists.ListContainer;
import save.excel.ExcelHandler;
import save.img.ImageHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by umat on 26.05.17.
 */
public class SaveHandler{
    public static void saveAll(ListContainer listContainer, String path) throws IOException {
        new File(path).mkdir();
        ImageHandler.saveAllImages(listContainer, path + "/" + path);
        ExcelHandler.saveExcel(listContainer, path + "/" + path);
    }
}
