## Adding new city to program
To add support for a new city you have to follow below steps:
* create parser function  <code>[businfov2.parsers.Parser](https://github.com/MIachaI/MPK_Crawler/blob/master/src/businfov2/parsers/Parser.java)</code> class so it creates `Timetable` object
* create <code>[businfov2.parsers.ImageGetter](https://github.com/MIachaI/MPK_Crawler/blob/master/src/businfov2/parsers/ImageGetter.java)</code> method for newly created city
* create a class inheriting from <code>[businfo.site_scanner.SiteScanner](https://github.com/MIachaI/MPK_Crawler/blob/master/src/businfo/site_scanner/SiteScanner.java)</code>. It must implement `scan()` method
* add city to enum <code>[businfov2.City](https://github.com/MIachaI/MPK_Crawler/blob/master/src/businfov2/City.java)</code>
* update `parse()` function in <code>[businfov2.parsers.Parser](https://github.com/MIachaI/MPK_Crawler/blob/master/src/businfov2/parsers/Parser.java)</code> class (it will distinct city to choose suitable parser)
* update method `updateHandler()` from <code>[businfo.site_scanner.CityUpdate](https://github.com/MIachaI/MPK_Crawler/blob/master/src/businfo/site_scanner/CityUpdate.java)</code>
