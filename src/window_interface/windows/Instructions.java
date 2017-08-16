package window_interface.windows;

import javafx.scene.control.TreeView;
import javafx.stage.Stage;

public class Instructions {
    public final String STYLESHEET_DIR = "file:src/window_interface/css/Theme.css";

    public static Stage init(){
        Stage stage = new Stage();
        TreeView tree = new TreeView();

        return stage;
    }
}
