package window_interface.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by umat on 22.06.17.
 */
public class ErrorDialog {
    /**
     * Display error dialog
     * @param title dialog title
     * @param headerText to be displayed (subtitle)
     * @param contentText to be displayed
     */
    public static void displayError(String title, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }
    /**
     * Display error dialog with default title
     * @param headerText to be displayed (subtitle)
     * @param contentText to be displayed
     */
    public static void displayError(String headerText, String contentText){
        displayError("Błąd", headerText, contentText);
    }
    /**
     * Display error dialog with default title and header
     * @param contentText to be displayed
     */
    public static void displayError(String contentText){
        displayError("Wystąpił błąd", contentText);
    }

    /**
     * Displays dialog with exception trace printed in a text field
     * code copied from http://code.makery.ch/blog/javafx-dialogs-official/
     * @param exception caught to be displayed
     */
    public static void displayException(Exception exception){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setContentText("Nastąpił nieoczekiwany błąd");

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Exception stacktrace:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
}
