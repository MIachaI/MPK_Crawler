package window_interface;

import businfo.lists.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import save.SaveHandler;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static jdk.nashorn.internal.objects.NativeString.substring;


public class WindowInterface extends Application implements EventHandler<ActionEvent> {
    Button Execute_Button;
    Stage window;
   public StringProperty labelText = new SimpleStringProperty("poczatek");
    public StringProperty labelText1 = labelText;


    /**
     * This method was created to allow program modify label which shows status
     * @param label input which will be shown to user
     * @return label turnet into StringProperty
     */
    public StringProperty setLabelText(String label){

        StringProperty labelText1 = new SimpleStringProperty(label);
        labelText=labelText1;
        return labelText1;
    }

    public static void openWindow() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final String[] pathToFile = new String[1];
        window = primaryStage;
        window.setTitle("MPK Crawler");

        //GridPane with 10px padding around edge
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        ToggleGroup Location_Group_RadioButtons = new ToggleGroup();

        RadioButton cracowBox = new RadioButton("Kraków");
        GridPane.setConstraints(cracowBox, 2, 0);
        cracowBox.setToggleGroup(Location_Group_RadioButtons);
        cracowBox.setSelected(true);

        RadioButton warsawBox = new RadioButton("Warszawa");
        GridPane.setConstraints(warsawBox, 3, 0);
        warsawBox.setToggleGroup(Location_Group_RadioButtons);


        //linkLabel - constrains use (child, column, row)
        Label linkLabel = new Label("Wprowadź nazwę:\nprzystanku");
        GridPane.setConstraints(linkLabel, 0, 1);

        //addedLinksLabel
        Label addedLinksLabel = new Label("Dodane przystanki:");
        GridPane.setConstraints(addedLinksLabel, 0, 2);

        //pathLabel
        Label pathLabel = new Label("Podaj ścieżkę zapisu:");
        GridPane.setConstraints(pathLabel, 0, 3);

        //statusLabel
        Label statusLabel = new Label();
        //WindowInterface d = new WindowInterface();
        statusLabel.textProperty().bind(labelText1);
        GridPane.setConstraints(statusLabel, 0, 4);

        //Menu
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Opcje robota");

/**
 * Crawler description to rewrite, following lines are just to check if it`s showing messages correctly.
 *
 */
        //Menu items
        MenuItem About = new MenuItem("O programie");
        About.setOnAction(event -> {
            Label secondLabel = new Label("about");
            secondLabel.setWrapText(true);

            StackPane secondaryLayout = new StackPane();
            secondaryLayout.getChildren().add(secondLabel);
            Scene secondScene = new Scene(secondaryLayout, 400, 300);
            Stage secondStage = new Stage();
            secondStage.setTitle("O programie");
            secondStage.setScene(secondScene);
            secondStage.setX(primaryStage.getX() + 250);
            secondStage.setY(primaryStage.getY() + 100);

            secondStage.show();
        });
        fileMenu.getItems().add(About);


        MenuItem Tutorial = new MenuItem("Tutorial");
        Tutorial.setOnAction(event -> {
            Label secondLabel = new Label("\tNa początek należy zrozumieć w jaki sposób cały program jest skonstruowany. Potrzeny jest tylko jeden zewnętrzny link do przystanku w danym mieście, czy to Krakowie czy Warszawie.\n\tFormat tego linku powinien być następujący: \nhttp://rozklady.mpk.krakow.pl/?lang=PL&akcja=przystanek&rozklad=20170429&przystanek=S3Jvd29kcnphIEfDs3JrYQeEeeEe\n\tProgram jest wówczas w stanie znaleźć linie przejężdżające przy tym przystanku w dane dni, zliczyć ich częstość, a następnie jako plik zwrotny otrzymujecie Państwo arkusz xls.");
            secondLabel.setWrapText(true);

            StackPane secondaryLayout = new StackPane();
            secondaryLayout.getChildren().add(secondLabel);

            Scene secondScene = new Scene(secondaryLayout, 400, 300);
            Stage secondStage = new Stage();
            secondStage.setTitle("Tutorial");
            secondStage.setScene(secondScene);
            secondStage.setX(primaryStage.getX() + 250);
            secondStage.setY(primaryStage.getY() + 100);
            secondStage.show();

        });
        fileMenu.getItems().add(Tutorial);

        MenuItem Authors = new MenuItem("Autorzy");
        Authors.setOnAction(event -> {
            Label secondLabel = new Label("\t");
            secondLabel.setWrapText(true);

            StackPane secondaryLayout = new StackPane();
            secondaryLayout.getChildren().add(secondLabel);
            Scene secondScene = new Scene(secondaryLayout, 400, 250);
            Stage secondStage = new Stage();
            secondStage.setTitle("Autorzy");
            secondStage.setScene(secondScene);
            secondStage.setX(primaryStage.getX() + 250);
            secondStage.setY(primaryStage.getY() + 100);

            secondStage.show();
        });
        fileMenu.getItems().add(Authors);

        menuBar.getMenus().addAll(fileMenu);

        //Link_TextField
        TextArea linkTextField = new TextArea();
        linkTextField.setMaxWidth(250.0);
        linkTextField.setMaxHeight(30.0);
        GridPane.setConstraints(linkTextField, 1, 1);

        //loadedLinksTextField
        TextArea loadedLinksTextField = new TextArea();
        loadedLinksTextField.setMaxWidth(250.0);
        loadedLinksTextField.setMaxHeight(70.0);
        GridPane.setConstraints(loadedLinksTextField, 1, 2);
        loadedLinksTextField.setWrapText(true);

        //Path_TextField
        TextField pathTextField = new TextField("");
        GridPane.setConstraints(pathTextField, 1, 3);
        final String[] html = new String[1];

        ListContainer linkContainer = new ListContainer();
        //Add_Button
        Button addButton = new Button("Dodaj");
        GridPane.setConstraints(addButton, 2,1);
        addButton.setOnAction((ActionEvent event) -> {
            String busStopName = linkTextField.getText();
            String firstLetterOfBusName = busStopName.substring(0,1);
            try {
                if(cracowBox.isSelected()){

                for (busStop busStopNameFromList : BusStopList.MPKBusStopLinksGetter()){
                    if(firstLetterOfBusName.equalsIgnoreCase(busStopNameFromList.toName().substring(0,1))){
                        if(busStopName.equalsIgnoreCase(busStopNameFromList.toName())){
                                String busStopNameToThreadUsage= busStopNameFromList.toLink();
                                BusStopAddMPK secondThread = new BusStopAddMPK();
                                secondThread.addLinkToListCointainer(linkContainer, busStopNameToThreadUsage);
                                secondThread.start();
                                String loadedLinks = loadedLinksTextField.getText();
                                loadedLinksTextField.setText(loadedLinks + busStopNameFromList.toName()+", ");
                                linkTextField.setText("");
                                break;
                        }
                    }
                }
            }
            else if(warsawBox.isSelected()) {
                    for (busStop busStopNameFromList : BusStopList.ZTMBusStopLinksGetter()) {
                        if (firstLetterOfBusName.equalsIgnoreCase(busStopNameFromList.toName().substring(0, 1))) {
                            if (busStopName.equalsIgnoreCase(busStopNameFromList.toName())) {
                                String busStopNameToThreadUsage= busStopNameFromList.toLink();
                                BusStopAddZTM secondThread = new BusStopAddZTM();
                                secondThread.addLinkToListCointainer(linkContainer, busStopNameToThreadUsage);
                                secondThread.start();
                                String loadedLinks = loadedLinksTextField.getText();
                                loadedLinksTextField.setText(loadedLinks + busStopNameFromList.toName()+", ");
                                linkTextField.setText("");
                                break;
                            }
                        }
                    }
                }
            }
            catch (IOException e) {
                loadedLinksTextField.setText("");

                e.printStackTrace();
            }
        });

        //deleteButton
        Button deleteButton = new Button("Usuń");
        GridPane.setConstraints(deleteButton, 2,2);
        deleteButton.setOnAction((ActionEvent event) -> {
            loadedLinksTextField.setText("");
            linkContainer.deleteListHandler();
        });

        //Execute_Button
        Button executeButton = new Button("Wykonaj");
        GridPane.setConstraints(executeButton, 1, 4);
        executeButton.setOnAction(event -> {
            // store all links provided by user in linkList
                String path = pathTextField.getText();
                SaveHandler executeThread = new SaveHandler();
                executeThread.injectListAndPath(linkContainer, path);
                executeThread.start();
                displaySuccessSaveAlert(path);
        });

        final Button browseButton = new Button("...");
        GridPane.setConstraints(browseButton, 2, 3);
        browseButton.setOnAction(e -> {
                    FileChooser fileChooser = new FileChooser();

                    //Show save file dialog
                    File file = fileChooser.showSaveDialog(primaryStage);
                    if(file != null) {
                        pathTextField.setText(file.getAbsolutePath());
                        pathToFile[0] = file.getAbsolutePath();
                    }
                    else {
                        // TODO handle cancel button pressed
                    }
                });

        //Add everything to grid
        grid.getChildren().addAll(
                loadedLinksTextField,
                pathTextField,
                linkTextField,
                menuBar,
                addedLinksLabel,
                linkLabel,
                pathLabel,
                statusLabel,
                addButton,
                executeButton,
                deleteButton,
                browseButton,
                cracowBox,
                warsawBox);

        Scene scene = new Scene(grid, 650, 250);
        window.setScene(scene);
        window.show();
    }

    @Override
    public void handle(ActionEvent event) {
    }

    private void displaySuccessSaveAlert(String path){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukces");
        alert.setHeaderText("Gotowe!");
        alert.setContentText("Zapisano do pliku:\n" + path);

        alert.showAndWait();
    }
}