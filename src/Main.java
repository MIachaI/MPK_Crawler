import businfo.busstop.streets.BusStop;
import businfo.site_scanner.CityUpdate;
import businfov2.CertificationMethod;
import businfov2.City;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import save.Saver;
import save.json.JSONHandler;
import window_interface.dialogs.AlertBox;
import window_interface.dialogs.ConfirmBox;
import window_interface.dialogs.ErrorDialog;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Main extends Application{
    private Stage window;

    private final ArrayList<City> cities = City.getImplemented();
    private final String BASE_DIR = System.getProperty("user.dir"); // current dir
    public final String STYLESHEET_DIR = "file:src/window_interface/css/Theme.css";
    private final String ICON_DIR = "file:src/window_interface/img/31771-200.png";
    private String JSON_SOURCE;
    private String SELECTED_DIRECTORY = null;

    // top menu items
    private MenuBar topMenu = new MenuBar();

    // left pane items
    private VBox leftPane = new VBox();
    private ComboBox<City> chooseCityBox = new ComboBox<>();
    private Button homePageButton = new Button("Strona_ przewoźnika");
    private ComboBox<CertificationMethod> chooseMethodBox = new ComboBox<>();

    // center pane items
    private GridPane centerPane = new GridPane();
    private TextField searchField = new TextField();
    private ListView<BusStop> busStopList = new ListView<>();
    private ObservableList<BusStop> displayedStops;
    private Button addButton = new Button("Dodaj");

    // right pane items
    private GridPane rightPane = new GridPane();
    private Label stopsLabel = new Label("Wybrane przystanki");
    private Label stopsList = new Label("");
    private Button startButton = new Button("Start");
    private Button clearButton = new Button("Wyczyść");

    // border pane
    private BorderPane borderPane = new BorderPane();

    // OTHER
    private Set<BusStop> selectedBusStops = new HashSet<>();

    public static void main(String[] args) throws IOException{
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //setUserAgentStylesheet(STYLESHEET_CASPIAN);
        this.JSON_SOURCE = this.checkJsonExistence();
        updateSelectedStops();
        window = primaryStage;
        window.setTitle("Crawler");
        window.setResizable(false);
        window.getIcons().add(new Image(ICON_DIR));

        // top menu
        topMenu = initMenuBar();

        // left pane
        chooseCityBox.getItems().addAll(cities);
        leftPane.setPadding(new Insets(10, 0, 0, 10));
        leftPane.setSpacing(10);
        leftPane.setPrefWidth(160);

        // ChoiceBox default value
        chooseCityBox.setValue(this.cities.get(0));

        // listen for selection changes in chooseCityBox
        chooseCityBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            try {
                displayedStops = FXCollections.observableArrayList(JSONHandler.fetchBusStopArray(JSON_SOURCE, newValue.toString()));
                busStopList.setItems(displayedStops.sorted());
                selectedBusStops.clear();
                updateSelectedStops();
            } catch (IOException | NullPointerException | ParseException e) {
                busStopList.setItems(null);
                e.printStackTrace();
                //ErrorDialog.displayException(e);
            }
        });
        homePageButton.setOnAction(event -> {
            try{
                getHostServices().showDocument(chooseCityBox.getValue().getMainPageHtml());
            } catch (Exception e){
                e.printStackTrace();
                ErrorDialog.displayException(e);
            }
        });

        chooseCityBox.setMinWidth(leftPane.getPrefWidth());
        homePageButton.setMinWidth(leftPane.getPrefWidth());
        leftPane.getChildren().addAll(chooseCityBox, homePageButton);


        // center pane
        centerPane.setPadding(new Insets(10,10,10,10));
        centerPane.setVgap(8);
        centerPane.setHgap(10);
        searchField.setPromptText("Szukaj");
        searchField.textProperty().addListener((observableValue, oldVal, newVal) -> {
            if (oldVal != null && (newVal.length() < oldVal.length())) {
                busStopList.setItems(displayedStops);
            }
            String value = newVal.toUpperCase();
            ObservableList<BusStop> subentries = FXCollections.observableArrayList();
            for (BusStop entry : busStopList.getItems()) {
                boolean match = true;
                String entryText = entry.getStreetName();
                if (!entryText.toUpperCase().contains(value)) {
                    match = false;
                    //break;
                }
                if (match) {
                    subentries.add(entry);
                }
            }
            busStopList.setItems(subentries.sorted());
        });
        // busStopList of  bus stops
        try {
            displayedStops = FXCollections.observableArrayList(JSONHandler.fetchBusStopArray(JSON_SOURCE, this.cities.get(0).toString()));
        } catch(Exception e){
            System.out.println("Empty file");
        }
        //busStopList.setMinWidth(350);
        try {
            busStopList.setItems(displayedStops.sorted());
        } catch(Exception e){
            busStopList.setItems(null);
        }
        busStopList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // setting the form, in which the list object will be displayed
        busStopList.setCellFactory(param -> new ListCell<BusStop>(){
            @Override
            protected void updateItem(BusStop item, boolean empty){
                super.updateItem(item, empty);
                if (empty || item == null || item.getStreetName() == null) {
                    setText(null);
                } else {
                    // this string will be displayed on the list
                    setText(item.getStreetName());
                }
            }
        });

        addButton.setOnAction(event -> addSelectedBusStops());

        GridPane.setConstraints(searchField, 0, 0);
        GridPane.setConstraints(busStopList, 0, 1);
        GridPane.setConstraints(addButton, 0, 2);
        centerPane.getChildren().addAll(searchField, addButton, busStopList);

        // right pane
        startButton.setOnAction(event -> {
            try {
                runAnalysesAndSave();
            } catch (IOException e) {
                ErrorDialog.displayException(e);
                e.printStackTrace();
            }
        });
        clearButton.setOnAction(event -> {
            selectedBusStops.clear();
            updateSelectedStops();
        });
        for(CertificationMethod method : CertificationMethod.values()){
            if(method.isImplemented()) chooseMethodBox.getItems().add(method);
        }
        chooseMethodBox.setPromptText("Metoda obliczeń");

        rightPane.setPadding(new Insets(10, 10, 10, 10));
        rightPane.setVgap(8);
        GridPane.setConstraints(chooseMethodBox, 0, 0);
        GridPane.setConstraints(startButton, 0, 1);
        GridPane.setConstraints(clearButton, 0,2);
        GridPane.setConstraints(stopsLabel, 0, 3);
        GridPane.setConstraints(stopsList,0,4);
        rightPane.getChildren().addAll(chooseMethodBox, stopsLabel, startButton, clearButton, stopsList);

        // adding everything to BorderPane
        borderPane.setTop(topMenu);
        borderPane.setLeft(leftPane);
        borderPane.setCenter(centerPane);
        borderPane.setRight(rightPane);

        window.setOnCloseRequest(e-> {
           e.consume();
           closeProgram();
       });

        Scene scene = new Scene(borderPane, 600, 500);
        scene.getStylesheets().add(this.STYLESHEET_DIR);
        window.setScene(scene);
        window.show();
        checkJSON(City.getImplementedNames(), JSON_SOURCE);
    }

    private void closeProgram(){
        boolean answer = ConfirmBox.display("Wyjście", "Czy na pewno chcesz wyjść z programu?");
        if(answer)
            window.close();
    }

    // UTITLITY
    /**
     * Check center bus stop list for selected items and add them to the right pane
     * (also update selected bus stops)
     */
    private void addSelectedBusStops(){
        ObservableList<BusStop> selectedItems = busStopList.getSelectionModel().getSelectedItems();
        selectedBusStops.addAll(selectedItems);
        updateSelectedStops();
        System.out.println(busStopList.getSelectionModel().getSelectedItems());
    }

    /**
     * Update label displaying currently selected bus stops with values stored in set: selectedBusStops
     */
    private void updateSelectedStops(){
        StringBuilder result = new StringBuilder();
        for(BusStop stop : selectedBusStops){
            result.append(stop.getStreetName()).append("\n");
        }
        stopsList.setText(result.toString());
        if(selectedBusStops.isEmpty()){
            startButton.setDisable(true);
        } else {
            startButton.setDisable(false);
        }
    }

    /**
     * checks if JSON file exists, if not creates blank file
     * @return absolute path to created (or found) JSON file
     */
    private String checkJsonExistence() throws IOException {
        File folder = new File(BASE_DIR);
        File[] files = folder.listFiles();
        String[] date = new String[3];
        Pattern jsonNamePattern = Pattern.compile("crawler_(\\d{1,2})_(\\d{1,2})_(\\d{1,2})\\.json");
        String filepath;
        assert files != null;
        for (File file : files){
            Matcher matcher = jsonNamePattern.matcher(file.getName().replace(BASE_DIR, ""));
            //System.out.println(matcher);
            if(matcher.matches()) {
                // if file was found - return its absolute path
                date[0] = matcher.group(1);
                date[1] = matcher.group(2);
                date[2] = matcher.group(3);

                filepath = file.getAbsolutePath();
                return filepath;
            }
        }

        // else - return path to newly created file
        String fileName = generateNewJsonFileName();
        filepath = BASE_DIR + File.separator + fileName;
        File file = new File(filepath);
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write("{}"); // required for JSON library to work properly
        bw.close();
        return file.getAbsolutePath();
    }

    /**
     * Check program directory for existence of JSON file. JSON file name has to be formatted following way:
     * crawler_dd_mm_yy.json where the date is indicating last update time
     * @param supportedCities if any of those cities was missing from the JSON file it should be updated if user desires it
     */
    private void checkJSON(ArrayList<String> supportedCities, String filepath) throws Exception {
        // 1. check json file existence - functionality moved to function checkJSONexistance()
        ArrayList supCities;
        supCities = new ArrayList(supportedCities);

        // 2. check for supported cities
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filepath));
        JSONObject jsonObject = (JSONObject) obj;
        ArrayList<String> citiesInJson = new ArrayList<>();
        for(Object cityObj : jsonObject.keySet()){
            String city = (String) cityObj;
            citiesInJson.add(city);
        }
        ArrayList<String> missingCities = supCities;
        missingCities.removeAll(citiesInJson);
        System.out.println("Missing cities: " + missingCities);

        // 3. ask if user wants to update missing cities
        if(missingCities.size() > 0){
            StringBuilder listOfCities = new StringBuilder();
            for(String city : missingCities){
                listOfCities.append(city).append("\n");
            }

            // 4. update missing cities if positive answer
            if(ConfirmBox.display(
                    "Brakujące miasta",
                    "Brakuje danych dla następujących miast:\n" + listOfCities +
                            "Czy chcesz pobrać dane teraz?\n" +
                            "UWAGA! Może zająć to od kilku do kilkunastu minut (w zależności od szybkości twojego łącza)"
            )) {
                for(String city : missingCities){
                    CityUpdate.updateHandler(city, filepath);
                    System.out.println("Updated city: " + city);
                }
                try {
                    displayedStops = FXCollections.observableArrayList(JSONHandler.fetchBusStopArray(JSON_SOURCE, this.cities.get(0).toString()));
                } catch(Exception e){
                    System.out.println("Empty file");
                }
                try {
                    busStopList.setItems(displayedStops.sorted());
                } catch(Exception e){
                    busStopList.setItems(null);
                }
            }
        }

        // 5. check date of the last update
    }

    /**
     * generate json file name based on current date
     * @return jason file name (with .json extension included)
     */
    private String generateNewJsonFileName(){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return "crawler_" + day + "_" + month + "_" + year % 100 + ".json";
    }

    // adding GUI elements
    /**
     * Init MenuBar object to use in main program as a topbar menu
     * @return MenuBar to put on the top of the window
     */
    private MenuBar initMenuBar(){
        Menu actionsMenu = new Menu("_Akcje");
        Menu optionsMenu = new Menu("_Opcje");
        Menu helpMenu = new Menu("_Pomoc");

        // actions
        MenuItem runOption = new MenuItem("U_ruchom analizę");
        MenuItem exitOption = new MenuItem("Wyjdź");
        Menu updateOption = new Menu("Aktualizuj listy");
        for(City city : this.cities){
            MenuItem menuItem = new MenuItem(city.toString());
            menuItem.setOnAction(actionEvent -> {
                if(ConfirmBox.display("Aktualizacja", "Czy na pewno chcesz aktualizować " + city + "?")) {
                    try {
                        CityUpdate.updateHandler(city.toString(), this.JSON_SOURCE);
                        // override json file name
                        Path source = Paths.get(this.JSON_SOURCE);
                        Files.move(source, source.resolveSibling(this.BASE_DIR + File.separator +generateNewJsonFileName()), REPLACE_EXISTING);
                        this.JSON_SOURCE = this.checkJsonExistence();
                        AlertBox.display("Sukces", "Udało się zaktualizować " + city);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ErrorDialog.displayException(e);
                    }
                }
            });
            updateOption.getItems().add(menuItem);
        }

        {
            // Action listeners
            runOption.setOnAction(actionEvent -> {
                try {
                    runAnalysesAndSave();
                } catch (IOException e) {
                    ErrorDialog.displayException(e);
                    e.printStackTrace();
                }
            });
            exitOption.setOnAction(event -> closeProgram());
        }

        // options
        MenuItem outputFolder = new MenuItem("Miejsce zapisu...");
        {
            // action listeners
            outputFolder.setOnAction(event ->{
                this.chooseSaveDirectory();
            });
        }

        // information (help)
        MenuItem instructionsOption = new MenuItem("Instrukcje");
        MenuItem contactOption = new MenuItem("Kontakt");
        {
            // action listeners
        }

        // add all submenus to menu
        actionsMenu.getItems().addAll(
                runOption,
                updateOption,
                new SeparatorMenuItem(),
                exitOption
        );
        optionsMenu.getItems().addAll(
                outputFolder
        );
        helpMenu.getItems().addAll(
                instructionsOption,
                contactOption
        );

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(
                actionsMenu,
                optionsMenu,
                helpMenu
        );
        return menuBar;
    }

    private void chooseSaveDirectory(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Wybierz folder do zapisu");
        File defaultDir = new File(this.BASE_DIR);
        chooser.setInitialDirectory(defaultDir);
        this.SELECTED_DIRECTORY = chooser.showDialog(window).toString();
        System.out.println(this.SELECTED_DIRECTORY);
    }

    private void runAnalysesAndSave() throws IOException {
        CertificationMethod selectedMethod = chooseMethodBox.getSelectionModel().getSelectedItem();
        if(selectedMethod == null) {
            AlertBox.display("Uwaga", "Przed rozpoczęciem analizy musisz wybrać metodę obliczeń");
            return ;
        }
        if(SELECTED_DIRECTORY == null){
            chooseSaveDirectory();
        }
        if(SELECTED_DIRECTORY == null) return ;
        try {
            City selectedCity = chooseCityBox.getValue();
            ArrayList<BusStop> list = new ArrayList<>();
            list.addAll(this.selectedBusStops);
            Saver.saveAll(SELECTED_DIRECTORY, businfov2.BusStop.convertBusStops(selectedCity, list), selectedMethod);
        } catch (Exception e) {
            ErrorDialog.displayException(e);
            e.printStackTrace();
        }
    }
}
