package window_interface;

import businfo.lists.*;
import excel.ExcelHandler;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import javax.xml.soap.Text;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class WindowInterface extends Application implements EventHandler<ActionEvent> {
    Button Execute_Button;
    Stage window;

    public static void openWindow() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("MPK Crawler");

        //GridPane with 10px padding around edge
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        ToggleGroup Location_Group_RadioButtons = new ToggleGroup();

        RadioButton cracowBox = new RadioButton("Kraków");
        GridPane.setConstraints(cracowBox, 2, 1);
        cracowBox.setToggleGroup(Location_Group_RadioButtons);
        cracowBox.setSelected(true);

        RadioButton warsawBox = new RadioButton("Warszawa");
        GridPane.setConstraints(warsawBox, 3, 1);
        warsawBox.setToggleGroup(Location_Group_RadioButtons);


        //linkLabel - constrains use (child, column, row)
        Label linkLabel = new Label("Wprowadź linki:\n(jeden na linię)");
        GridPane.setConstraints(linkLabel, 0, 1);

        //pathLabel
        Label pathLabel = new Label("Podaj ścieżkę zapisu:");
        GridPane.setConstraints(pathLabel, 0, 2);

        //statusLabel
        Label statusLabel = new Label("Status: w gotowości");
        GridPane.setConstraints(statusLabel, 0, 3);

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
        GridPane.setConstraints(linkTextField, 1, 1);

        //Path_TextField
        TextField pathTextField = new TextField("");
        GridPane.setConstraints(pathTextField, 1, 2);
        final String[] html = new String[1];

        //Execute_Button
        Button executeButton = new Button("Wykonaj");
        GridPane.setConstraints(executeButton, 1, 3);
        executeButton.setOnAction(event -> {
            // store all links provided by user in linkList
            ArrayList<String> linkList = new ArrayList<>(Arrays.asList(linkTextField.getText().split("\n")));
            if (cracowBox.isSelected()) {
                ListContainer mpkContainer = new ListContainer(); // create ListContainer for MPKList objects
                try {
                    for(String link : linkList){
                        mpkContainer.addListHandler(new MPKList(link)); // add MPKList object for each link on the list
                    }
                } catch (IOException e) {
                    statusLabel.setText("Status: błąd!");
                }
                try {
                    String path = pathTextField.getText();

                    ExcelHandler.saveExcel(mpkContainer, path);
                    displaySuccessSaveAlert(path);
                } catch (IOException e) {
                    statusLabel.setText("Status: błąd!");
                }

            } else if (warsawBox.isSelected()) {
                ListContainer ztmContainer = new ListContainer();
                try {
                    for(String link : linkList){
                        ztmContainer.addListHandler(new ZTMList(link));
                    }
                } catch (IOException e) {
                    statusLabel.setText("Status: błąd!");
                }
                try {
                    String path = pathTextField.getText();
                    ExcelHandler.saveExcel(ztmContainer, path);
                    displaySuccessSaveAlert(path);
                } catch (IOException e) {
                    statusLabel.setText("Status: błąd!");
                }
            }
        });

        final Button browseButton = new Button("...");
        GridPane.setConstraints(browseButton, 2, 2);
        browseButton.setOnAction(e -> {
                    FileChooser fileChooser = new FileChooser();

                    //Set extension filter
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLS File (*.xls)", "*.xls");
                    fileChooser.getExtensionFilters().add(extFilter);

                    //Show save file dialog
                    File file = fileChooser.showSaveDialog(primaryStage);
                    if(file != null)
                        pathTextField.setText(file.getAbsolutePath());
                    else {
                        // TODO handle cancel button pressed
                    }
                });

        //Add everything to grid
        grid.getChildren().addAll(menuBar, linkLabel, linkTextField, pathLabel, pathTextField, executeButton, browseButton, cracowBox, warsawBox, statusLabel);

        Scene scene = new Scene(grid, 600, 200);
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