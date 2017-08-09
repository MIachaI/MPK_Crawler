## Adding new city to program
To add support for a new city you have to follow below steps:
* create parser function in `Parser` class so it creates `Timetable` object
* create a class inheriting from `SiteScanner`. It must implement `scan()` method
* add city to enum `City`
* update `parse()` function in `Parser` class
* update method `CityUpdate.updateHandler()`


### Adding new city to program (Depreciated)
To add support for a new city you have to follow below steps:
* create a class inheriting from `BusInfo` and implementing following methods:
  * `String getRawResult(Connection)`
  * `String getRawHtml()`
  * `boolean checkColumnNames()` - optional
* create a class inheriting from `SiteScanner`. It must implement `scan()` method
* update `updateHandler()` method from `CityUpdate` class
* add city name in `Main.java file` into the `cities` ArrayList:
    ```java
    private ArrayList<String> cities = new ArrayList<>(Arrays.asList(
                "Kraków",
                "Warszawa"
        ));
    ```
* add city in `SelectedBusStopsHandler` class constructor
   