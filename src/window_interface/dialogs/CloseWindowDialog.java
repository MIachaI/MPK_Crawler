package window_interface.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by umat on 22.06.17.
 * //TODO
 */
public class CloseWindowDialog {
    public static void closeProgram(Stage window){
        Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Czy na pewno chcesz wyjść?");
        Button exitButton = new Button("Wyjdź");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        window.close();
    }
}
