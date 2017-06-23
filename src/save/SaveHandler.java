package save;

import businfo.lists.ListContainer;
import save.excel.ExcelHandler;
import save.img.ImageHandler;
import window_interface.WindowInterface;

import java.io.IOException;

/**
 * Created by umat on 26.05.17.
 */
public class SaveHandler extends Thread {
    static ListContainer listContainer;
    static String path;
    public static void saveAll() throws IOException {
        ImageHandler.saveAllImages(listContainer, path);
        ExcelHandler.saveExcel(listContainer, path);
    }
    public void injectListAndPath (ListContainer importedContainer, String importedPath){
        listContainer=importedContainer;
        path=importedPath;
    }
    @Override
    public void run(){
        SaveHandler engineStart = new SaveHandler();
        WindowInterface label = new WindowInterface();
        label.setLabelText("rozpoczynam");
        try {
            engineStart.saveAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
