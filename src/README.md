###Adding new city to program
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
   