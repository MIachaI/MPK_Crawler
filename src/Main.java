import businfo.busstop.BusInfo;
import businfo.lists.ListHandler;
import businfo.lists.ListContainer;
import businfo.lists.MPKList;
import businfo.lists.ZTMList;
import excel.ExcelHandler;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException{
        ArrayList<String> mpkLinks = new ArrayList<String>() {{
            add("http://rozklady.mpk.krakow.pl/?lang=PL&akcja=przystanek&rozklad=20170515&przystanek=S3J5c3BpbsOzdweEeeEe");
            add("http://rozklady.mpk.krakow.pl/?lang=PL&akcja=przystanek&rozklad=20170515&przystanek=UnVjemFq");
            add("http://rozklady.mpk.krakow.pl/?lang=PL&akcja=przystanek&rozklad=20170515&przystanek=S3J6eXN6dG9mb3J6eWNlIEfDs3Jh");
        }};

        ArrayList<String> ztmLinks = new ArrayList<String>() {{
            add("http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=4194");
            add("http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=3868");
            add("http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1&a=3039");
        }};


        ArrayList<ListHandler> mpkLists = new ArrayList<>();
        ArrayList<ListHandler> ztmLists = new ArrayList<>();

        for(String link : mpkLinks){
            mpkLists.add(new MPKList(link));
        }

        for(String link : ztmLinks){
            ztmLists.add(new ZTMList(link));
        }

        ListContainer container = new ListContainer(ztmLists);
        //ArrayList<BusInfo> infosPurified = container.getBusInfos();
        for(BusInfo info : container.getBusInfos()){
            System.out.println(info);
        }

        ExcelHandler.saveExcel(container,"/home/umat/workspace/java/BusCounter/try");
    }
}