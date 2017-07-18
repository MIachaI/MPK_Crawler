package businfo.site_scanner;

import businfo.busstop.streets.BusStop;
import org.json.simple.JSONObject;
import save.json.JSONHandler;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Michal on 18.07.2017.
 */
public class CityUpdate {

    public void ZTMWarsawUpdate () throws IOException {
         WarsawScanner scanner = new WarsawScanner(); // stworz scanner
         ArrayList<BusStop> warsawList = new ArrayList<>(); // stworz listę do przechowania wyników dzialania scannera
         warsawList = scanner.scan(); // skanuj (i zapisz do tablicy)
         JSONObject warsawObject; // stwórz obiekt JSONObiekt (żeby biblioteka json.simple mogła się nim posłużyć)
         warsawObject = JSONHandler.generateCityObject(warsawList); // do tego obiektu wrzuć zeskanowaną listę za pomocą funkcji "generateCityObiect(ArrayList<BusStop>)"
         try {
             JSONHandler.updateJSONFile("test.json","Warszawa", warsawObject); // zrób update pliku
         // argumenty:
         // 1. nazwa pliku json
         // 2. nazwa miasta, które updatować
         // 3. JSONObiekt z listą przystanków
         // UWAGA! Jeżeli podany plik json przed rozpoczęciem skanowania będzie pusty to biblioteka przy próbie zapisu wyrzuci błąd.
         // Zanim zaczniesz skanować dodaj w pliku puste nawiasy klamrowe: "{}" albo wykorzystaj ten plik, który ci wysłałem.
         } catch (org.json.simple.parser.ParseException e) {
             e.printStackTrace();
             System.out.println("WYSTĄPIŁ BŁĄD");
         }

    }
    public void MPKKrakowUpdate () throws IOException {
        System.out.println("działam");
        KrakowScanner scanner = new KrakowScanner(); // stworz scanner
        ArrayList<BusStop> krakowList = new ArrayList<>(); // stworz listę do przechowania wyników dzialania scannera
        krakowList = scanner.scan(); // skanuj (i zapisz do tablicy)
        JSONObject krakowObject; // stwórz obiekt JSONObiekt (żeby biblioteka json.simple mogła się nim posłużyć)
        krakowObject = JSONHandler.generateCityObject(krakowList); // do tego obiektu wrzuć zeskanowaną listę za pomocą funkcji "generateCityObiect(ArrayList<BusStop>)"
        try {
            JSONHandler.updateJSONFile("test.json","Kraków", krakowObject); // zrób update pliku
            // argumenty:
            // 1. nazwa pliku json
            // 2. nazwa miasta, które updatować
            // 3. JSONObiekt z listą przystanków
            // UWAGA! Jeżeli podany plik json przed rozpoczęciem skanowania będzie pusty to biblioteka przy próbie zapisu wyrzuci błąd.
            // Zanim zaczniesz skanować dodaj w pliku puste nawiasy klamrowe: "{}" albo wykorzystaj ten plik, który ci wysłałem.
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            System.out.println("WYSTĄPIŁ BŁĄD");
        }

    }
    public void MPKWroclawUpdate () throws IOException {
        WroclawScanner scanner = new WroclawScanner(); // stworz scanner
        ArrayList<BusStop> wroclawList = new ArrayList<>(); // stworz listę do przechowania wyników dzialania scannera
        wroclawList = scanner.scan(); // skanuj (i zapisz do tablicy)
        JSONObject wroclawObject; // stwórz obiekt JSONObiekt (żeby biblioteka json.simple mogła się nim posłużyć)
        wroclawObject = JSONHandler.generateCityObject(wroclawList); // do tego obiektu wrzuć zeskanowaną listę za pomocą funkcji "generateCityObiect(ArrayList<BusStop>)"
        try {
            JSONHandler.updateJSONFile("test.json","Wrocław", wroclawObject); // zrób update pliku
            // argumenty:
            // 1. nazwa pliku json
            // 2. nazwa miasta, które updatować
            // 3. JSONObiekt z listą przystanków
            // UWAGA! Jeżeli podany plik json przed rozpoczęciem skanowania będzie pusty to biblioteka przy próbie zapisu wyrzuci błąd.
            // Zanim zaczniesz skanować dodaj w pliku puste nawiasy klamrowe: "{}" albo wykorzystaj ten plik, który ci wysłałem.
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            System.out.println("WYSTĄPIŁ BŁĄD");
        }

    }




}
