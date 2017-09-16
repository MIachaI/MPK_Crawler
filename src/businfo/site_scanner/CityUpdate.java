package businfo.site_scanner;

import businfo.busstop.streets.BusStop;
import businfov2.City;
import org.json.simple.JSONObject;
import save.json.JSONHandler;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Michal on 18.07.2017.
 */
public class CityUpdate {
    public static void ZTMWarsawUpdate() throws IOException {
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
    public static void MPKKrakowUpdate() throws IOException {
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
    public static void MPKWroclawUpdate() throws IOException {
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

    /**
     * Automatically update given JSON file with provided city name
     * @param cityName city to update. Supported cities can be found inside switch statement
     * @param filename JSON file path
     * @throws businfov2.City.NotImplementedException if city is not implemented by application
     * @throws InvalidParameterException if wrong city name given
     */
    public static void updateHandler(String cityName, String filename)
    throws City.NotImplementedException, InvalidParameterException, IOException, org.json.simple.parser.ParseException {
        City city = City.stringToEnum(cityName);
        city.isImplemented();
        ArrayList<BusStop> busStops;
        SiteScanner scanner;
        switch (city) {
            case KRAKOW:
                scanner = new KrakowScanner();
                break;
            case WARSZAWA:
                scanner = new WarsawScanner();
                break;
            case POZNAN:
                scanner = new PoznanScanner();
                break;
            case WROCLAW:
                scanner = new WroclawScanner();
                break;
            default:
                throw new InvalidParameterException("Invalid city");
        }
        busStops = scanner.scan();
        JSONObject cityObject;
        cityObject = JSONHandler.generateCityObject(busStops);
        JSONHandler.updateJSONFile(filename, cityName, cityObject);
    }


    public void checkUpdates(){

    }
}
