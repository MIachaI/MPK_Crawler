import businfo.busstop.streets.BusStop;
import businfo.busstop.streets.BusStopNameComparator;
import businfo.lists.*;
import businfo.site_scanner.CityUpdate;
import businfo.site_scanner.WarsawScanner;
import businfo.site_scanner.WroclawScanner;
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
import javafx.scene.layout.*;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import save.SaveHandler;
import save.json.JSONHandler;
import window_interface.dialogs.ConfirmBox;
import window_interface.dialogs.ErrorDialog;

public class Main extends Application{
    private Stage window;

    // city list (only cities mentioned below will be in the program)
    private ArrayList<String> cities = new ArrayList<>(Arrays.asList(
            "Kraków",
            "Warszawa"
    ));
    private String jsonSource = "crawler_10_05_17.json";

    // top menu items
    private MenuBar topMenu = new MenuBar();

    // left pane items
    private VBox leftPane = new VBox();
    private ChoiceBox<String> chooseCityBox = new ChoiceBox<>();
    private Button homePageButton = new Button("Strona przewoźnika");

    // center pane items
    private GridPane centerPane = new GridPane();
    private TextField searchField = new TextField();
    private ListView<BusStop> busStopList = new ListView<>();
    private ObservableList<BusStop> displayedStops;
    private Button addButton = new Button("Dodaj");

    // right pane items
    private GridPane rightPane = new GridPane();
    private Label stopsLabel = new Label("Lista przystanków");
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
        updateSelectedStops();
        window = primaryStage;
        window.setTitle("Crawler");

        // top menu
        topMenu = initMenuBar();

        // left pane
        chooseCityBox.getItems().addAll(cities);
        leftPane.setSpacing(10);

        // ChoiceBox default value
        chooseCityBox.setValue(cities.get(0));

        // listen for selection changes in chooseCityBox
        chooseCityBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            try {
                displayedStops = FXCollections.observableArrayList(JSONHandler.fetchBusStopArray(jsonSource, newValue));
                busStopList.setItems(displayedStops.sorted());
                selectedBusStops.clear();
                updateSelectedStops();
            } catch (IOException | NullPointerException | ParseException e) {
                e.printStackTrace();
                ErrorDialog.displayException(e);
            }
        });
        homePageButton.setOnAction(event -> {
            if (chooseCityBox.getValue()=="Kraków"){
                try
                {
                    Desktop.getDesktop().browse(new URL("http://rozklady.mpk.krakow.pl/").toURI());
                }
                catch (Exception e) {}
        }
        else if(chooseCityBox.getValue()=="Warszawa"){
                try
                {
                    Desktop.getDesktop().browse(new URL("http://www.ztm.waw.pl/rozklad_nowy.php?c=183&l=1").toURI());
                }
                catch (Exception e) {}
            }
        });

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
        displayedStops = FXCollections.observableArrayList(JSONHandler.fetchBusStopArray(jsonSource, cities.get(0)));
        //busStopList.setMinWidth(350);
        busStopList.setItems(displayedStops.sorted());
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

        rightPane.setPadding(new Insets(10, 10, 10, 10));
        rightPane.setVgap(8);
        GridPane.setConstraints(startButton, 0, 0);
        GridPane.setConstraints(clearButton, 0,1);
        GridPane.setConstraints(stopsLabel, 0, 2);
        GridPane.setConstraints(stopsList,0,3);
        rightPane.getChildren().addAll(stopsLabel, startButton, clearButton, stopsList);

        // adding everything to BorderPane
        borderPane.setTop(topMenu);
        borderPane.setLeft(leftPane);
        borderPane.setCenter(centerPane);
        borderPane.setRight(rightPane);

        window.setOnCloseRequest(e-> {
           e.consume();
           closeProgram();
       });

        Scene scene = new Scene(borderPane, 700, 500);
        window.setScene(scene);
        window.show();
    }

    private void closeProgram(){
        boolean answer = ConfirmBox.display("Wyjście", "Czy na pewno chcesz wyjść z programu?");
        if(answer)
            window.close();
    }

    // UTITLITY
    /**
     * Choose city from ChoiceBox and perform actions // TODO implement those actions
     * @param choiceBox from which to choose city
     */
    private void handleCityChoiceBox(ChoiceBox<String> choiceBox) throws IOException {
        String city = choiceBox.getValue();
        ListContainer list = new ListContainer();
        ArrayList<String> links = new ArrayList<>(Arrays.asList(searchField.getText().split("\n")));
        if (Objects.equals(city, "Kraków")){
            for (String link : links){
                list.addListHandler(new KrakowSelectedBusStops(link));
            }
        }
        else if (Objects.equals(city, "Warszawa")){
            for (String link : links){
                list.addListHandler(new WarszawaSelectedBusStops(link));
            }
        }
        searchField.setText(list.toString());
    }

    /**
     * Add BusStops button handler // TODO implement
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
     * Check program directory for existence of JSON file. JSON file name has to be formatted following way:
     * crawler_dd_mm_yy.json where the date is indicating last update time
     * @param supportedCities if any of those cities was missing from the JSON file it should be updated if user desires it
     */
    private static void checkJSON(ArrayList<String> supportedCities) throws IOException, ParseException {
        // 1. check if json file exists
        final String CURRENT_DIR = System.getProperty("user.dir"); // current dir
        // list all files in directory to find json files
        File folder = new File(CURRENT_DIR);
        File[] files = folder.listFiles();
        // find json file matching regex
        String[] date = new String[3];
        Pattern jsonNamePattern = Pattern.compile("crawler_(\\d{2})_(\\d{2})_(\\d{2})\\.json");
        boolean jsonFound = false;
        String filepath = "";
        for (File file : files){
            Matcher matcher = jsonNamePattern.matcher(file.getName());
            //System.out.println(matcher);
            if(matcher.matches()) {
                date[0] = matcher.group(1);
                date[1] = matcher.group(2);
                date[2] = matcher.group(3);
                System.out.println(file.getName());
                for (String d : date) {
                    System.out.println(d);
                }
                jsonFound = true;
                filepath = file.getAbsolutePath();
                break;
            }
        }

        if(!jsonFound){
            // TODO popup window?
            System.out.println("Update or download json file");
            return ;
        }

        // 2. check for supported cities
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filepath));
        JSONObject jsonObject = (JSONObject) obj;
        ArrayList<String> citiesInJson = new ArrayList<>();
        for(Object cityObj : jsonObject.keySet()){
            String city = (String) cityObj;
            citiesInJson.add(city);
        }
        ArrayList<String> missingCities = supportedCities;
        missingCities.removeAll(citiesInJson);
        System.out.println(missingCities);

        // 3. ask if user wants to update missing cities
        if(missingCities.size() > 0){
            if(ConfirmBox.display("Hej","klej")){
                // update
                System.out.println("UPDATE");
            }
        }

        // 4. update missing cities if positive answer

        // 5. check date of the last update
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
        for(String city : this.cities){
            MenuItem menuItem = new MenuItem(city);
            updateOption.getItems().add(menuItem);
        }

        {
            // Action listeners
            exitOption.setOnAction(event -> closeProgram());
        }

        // options
        MenuItem outputFolder = new MenuItem("Miejsce zapisu...");
        {
            // action listeners
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

    private void runAnalysesAndSave() throws IOException {
        String selectedCity = chooseCityBox.getValue();
        ArrayList<BusStop> list = new ArrayList<>();
        list.addAll(this.selectedBusStops);
        SelectedBusStopsHandler handler = null;
        try {
            handler = new SelectedBusStopsHandler(selectedCity, list);
        } catch (Exception e) {
            ErrorDialog.displayException(e);
            e.printStackTrace();
        }
        SaveHandler.saveAll(handler,"Crawl");
    }
}
