package window_interface;

import businfo.lists.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


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

        RadioButton Cracow_Box = new RadioButton("Kraków");
        GridPane.setConstraints(Cracow_Box, 2, 1);
        Cracow_Box.setToggleGroup(Location_Group_RadioButtons);
        Cracow_Box.setSelected(true);

        RadioButton Warsaw_Box = new RadioButton("Warszawa");
        GridPane.setConstraints(Warsaw_Box, 3, 1);
        Warsaw_Box.setToggleGroup(Location_Group_RadioButtons);


        //Link_Label - constrains use (child, column, row)
        Label Link_Label = new Label("Wprowadź link:");
        GridPane.setConstraints(Link_Label, 0, 1);

        //Path_Label
        Label Path_Label = new Label("Podaj ścieżkę zapisu:");
        GridPane.setConstraints(Path_Label, 0, 2);

        //Status_Label
        Label Status_Label = new Label("Status: w gotowości");
        GridPane.setConstraints(Status_Label, 0, 4);

        //OutputName_Label
        Label OutputName_Label = new Label("Wprowadź nazwę:");
        GridPane.setConstraints(OutputName_Label, 0, 3);

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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("O programie");
            alert.setHeaderText("Public Transport Crawler");
            alert.setContentText("Program ten powstał przede wszystkim po to, aby ułatwić pracę audytorom z firmy JW_A. \nPoliczenie siły komunikacji dla budynku nigdy nie było tak proste!");
            alert.showAndWait();
        });
        fileMenu.getItems().add(About);

        MenuItem Tutorial = new MenuItem("Tutorial");
        Tutorial.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tutorial");
            alert.setHeaderText("Jak używać?");
            alert.setContentText("Na początek należy zrozumieć w jaki sposób cały program jest skonstruowany. Potrzeny jest tylko jeden zewnętrzny link do przystanku w danym mieście, czy to Krakowie czy Warszawie. Format tego linku powinien być następujący: \nhttp://rozklady.mpk.krakow.pl/?lang=PL&akcja=przystanek&rozklad=20170429&przystanek=S3Jvd29kcnphIEfDs3JrYQeEeeEe\nProgram jest wówczas w stanie znaleźć linie przejężdżające przy tym przystanku w dane dni, zliczyć ich częstość, a następnie jako plik zwrotny otrzymujecie Państwo arkusz xls.");
            alert.showAndWait();
        });
        fileMenu.getItems().add(Tutorial);

        MenuItem Authors = new MenuItem("Autorzy");
        Authors.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Autorzy");
            alert.setHeaderText("Umat & MIachaI");
            alert.setContentText("Kontakt możliwy za pomocą emaili \nMIachaI: mprawda@wp.pl \nUmat: Urbańczyk@wp.pl");
            alert.showAndWait();
        });
        fileMenu.getItems().add(Authors);




        menuBar.getMenus().addAll(fileMenu);


        //Link_TextField
        TextField Link_TextField = new TextField("");
        GridPane.setConstraints(Link_TextField, 1, 1);

        //Path_TextField
        TextField Path_TextField = new TextField("");
        GridPane.setConstraints(Path_TextField, 1, 2);
        final String[] html = new String[1];

        //OutputName_TextField
        TextField OutputName_TextField = new TextField("");
        GridPane.setConstraints(OutputName_TextField, 1, 3);
        OutputName_TextField.setText("Output");

        //Execute_Button
        Button Execute_Button = new Button("Wykonaj");
        GridPane.setConstraints(Execute_Button, 1, 4);
        Execute_Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (Cracow_Box.isSelected()) {
                    String html = Link_TextField.getText();
                    MPKList mpkList = null;
                    try {
                        mpkList = new MPKList(html);
                    } catch (IOException e) {
                        Status_Label.setText("Status: błąd!");
                    }
                    String zmienna = Path_TextField.getText();
                    //
                    try {
                        String output = OutputName_TextField.getText();
                        File file = new File(zmienna + "/" + output + ".xls");
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write(mpkList.excelFormattedText());
                        fileWriter.flush();
                        fileWriter.close();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Sukces");
                        alert.setHeaderText("Gotowe!");
                        alert.showAndWait();
                    } catch (IOException e) {
                        Status_Label.setText("Status: błąd!");
                    }

                } else if (Warsaw_Box.isSelected()) {
                    String html = Link_TextField.getText();
                    ZTMList ztmList = null;
                    try {
                        ztmList = new ZTMList(html);
                    } catch (IOException e) {
                        Status_Label.setText("Status: błąd!");
                    }
                    String zmienna = Path_TextField.getText();
                    try {
                        String output = OutputName_TextField.getText();
                        File file = new File(zmienna + "/" + output + ".xls");
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write(ztmList.excelFormattedText());
                        fileWriter.flush();
                        fileWriter.close();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Sukces");
                        alert.setHeaderText("Gotowe!");

                        alert.showAndWait();
                    } catch (IOException e) {
                        Status_Label.setText("Status: błąd!");
                    }
                }
            }
        });


        final Button Browse_Button = new Button("...");
        GridPane.setConstraints(Browse_Button, 2, 2);
        Browse_Button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        final DirectoryChooser directoryChooser =
                                new DirectoryChooser();
                        final File selectedDirectory =
                                directoryChooser.showDialog(window);
                        if (selectedDirectory != null) {
                            selectedDirectory.getAbsolutePath();
                            Path_TextField.setText(selectedDirectory.getAbsolutePath());
                        }
                    }
                }
        );


        //Add everything to grid
        grid.getChildren().addAll(menuBar, OutputName_TextField, OutputName_Label, Link_Label, Link_TextField, Path_Label, Path_TextField, Execute_Button, Browse_Button, Cracow_Box, Warsaw_Box, Status_Label);


        Scene scene = new Scene(grid, 600, 250);
        window.setScene(scene);
        window.show();

    }

    @Override
    public void handle(ActionEvent event) {
    }

}