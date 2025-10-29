package ca.mcgill.ecse.cheecsemanager.fxml.basecontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class DeleteDialogController<T> {

    Consumer<T> confirmAction;

    @FXML
    Button buttonCancel;

    public void setAction(Consumer<T> confirmAction) {
        this.confirmAction = confirmAction;
    }

    @FXML
    private void onDelete(ActionEvent event) {
        confirmAction.accept(null);
        onCancel(event);
    }

    @FXML
    private void onCancel(ActionEvent event) {
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }
}
