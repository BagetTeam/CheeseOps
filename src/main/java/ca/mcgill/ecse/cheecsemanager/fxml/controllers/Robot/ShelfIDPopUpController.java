

package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Robot;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ShelfIDPopUpController {

    public TextField userInput;
    public Button cancelButton;
    public Button confirmShelfID;

    private Stage stage;
    private String shelfIdEntered = null;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getShelfIdEntered() {
        return shelfIdEntered;
    }

    public void handleConfirm(ActionEvent event) {
        shelfIdEntered = userInput.getText();
        stage.close();
    }

    public void handleCancel(ActionEvent event) {
        shelfIdEntered = null; // user cancelled
        stage.close();
    }
}
