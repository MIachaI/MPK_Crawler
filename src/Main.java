import businfo.lists.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import window_interface.dialogs.ConfirmBox;
import window_interface.dialogs.ErrorDialog;

public class Main extends Application{
    private Stage window;

    // top menu items
    private HBox topMenu = new HBox();
    private Button infoButton = new Button("Info");
    private Button helpButton = new Button("Pomoc");

    // left pane items
    private VBox leftPane = new VBox();
    private ChoiceBox<String> chooseCityBox = new ChoiceBox<>();

    // center pane items
    private GridPane centerPane = new GridPane();
    private TextArea textArea = new TextArea();
    private ListView<String> busStopList = new ListView<>();
    private ObservableList<String> mpkStops = FXCollections.observableArrayList("MPK1", "MPK2", "MPK3", "MPK4", "MPK5", "MPK6");
    private ObservableList<String> ztmStops = FXCollections.observableArrayList("ZTM1", "ZTM2", "ZTM3", "ZTM4", "ZTM5", "ZTM6");
    private Button addButton = new Button("Dodaj");

    // right pane items
    private GridPane rightPane = new GridPane();
    private Label stopsLabel = new Label("Lista przystanków");
    private Button startButton = new Button("Start");

    // border pane
    private BorderPane borderPane = new BorderPane();

    public static void main(String[] args) throws IOException{
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("MPK Crawler");

        // top menu
        topMenu.getChildren().addAll(infoButton, helpButton);

        // left pane
        leftPane.setPadding(new Insets(10, 10, 10, 10));
        chooseCityBox.getItems().addAll("Kraków", "Warszawa");
        // ChoiceBox default value
        chooseCityBox.setValue("Kraków");

        // listen for selection changes in chooseCityBox
        chooseCityBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            // TODO if mode was changed, remember to remove all already selected bus stops
            if (Objects.equals(newValue, "Kraków")){
                busStopList.setItems(mpkStops);
            }
            else if (Objects.equals(newValue, "Warszawa")){
                busStopList.setItems(ztmStops);
            }
        });

        leftPane.getChildren().addAll(chooseCityBox);

        // center pane
        centerPane.setPadding(new Insets(10,10,10,10));
        centerPane.setVgap(8);
        centerPane.setHgap(10);
        textArea.setPromptText("Podaj linki do stron z rozkładem");
        // busStopList of  bus stops
        busStopList.setItems(mpkStops);
        busStopList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        addButton.setOnAction(event -> {
            System.out.println(busStopList.getSelectionModel().getSelectedItems());
        });

        GridPane.setConstraints(textArea, 0, 0);
        GridPane.setConstraints(busStopList, 0, 1);
        GridPane.setConstraints(addButton, 0, 2);
        centerPane.getChildren().addAll(textArea, addButton, busStopList);

        // right pane
        startButton.setOnAction(event -> {
            try {
                handleCityChoiceBox(chooseCityBox);
            } catch (Exception e) {
                // TODO delete following line in prod
                e.printStackTrace();
                ErrorDialog.displayException(e);
            }
        });

        rightPane.setPadding(new Insets(10, 10, 10, 10));
        GridPane.setConstraints(stopsLabel, 0, 0);
        GridPane.setConstraints(startButton, 0, 2);
        rightPane.getChildren().addAll(stopsLabel, startButton);

        // adding everything to BorderPane
        borderPane.setTop(topMenu);
        borderPane.setLeft(leftPane);
        borderPane.setCenter(centerPane);
        borderPane.setRight(rightPane);

        window.setOnCloseRequest(e-> {
           e.consume();
           closeProgram();
       });

        Scene scene = new Scene(borderPane, 700, 250);
        window.setScene(scene);
        window.show();
    }

    private void closeProgram(){
        boolean answer = ConfirmBox.display("Wyjście", "Czy na pewno chcesz wyjść z programu?");
        if(answer)
            window.close();
    }

    /**
     * Handle reading links from proper site by checking state of radio buttons
     * @param mpk RadioButton
     * @param ztm RadioButton
     * @throws IOException
     */
    @Deprecated
    private void handleCityRadioButtons(RadioButton mpk, RadioButton ztm) throws IOException {
        ListContainer list = new ListContainer();
        ArrayList<String> links = new ArrayList<>(Arrays.asList(textArea.getText().split("\n")));
        if(mpk.isSelected()){
            for (String link : links){
                list.addListHandler(new MPKList(link));
            }
        }
        else if(ztm.isSelected()){
            for (String link : links){
                list.addListHandler(new ZTMList(link));
            }
        }
    }

    /**
     * Choose city from ChoiceBox and perform actions // TODO implement those actions
     * @param choiceBox from which to choose city
     */
    private void handleCityChoiceBox(ChoiceBox<String> choiceBox) throws IOException {
        String city = choiceBox.getValue();
        ListContainer list = new ListContainer();
        ArrayList<String> links = new ArrayList<>(Arrays.asList(textArea.getText().split("\n")));
        if (Objects.equals(city, "Kraków")){
            for (String link : links){
                list.addListHandler(new MPKList(link));
            }
        }
        else if (Objects.equals(city, "Warszawa")){
            for (String link : links){
                list.addListHandler(new ZTMList(link));
            }
        }
        textArea.setText(list.toString());
    }
}
