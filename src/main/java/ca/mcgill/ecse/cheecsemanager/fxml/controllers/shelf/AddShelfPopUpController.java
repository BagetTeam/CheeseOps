package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet2Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
/*
* @author Ayush
* */

public class AddShelfPopUpController {

    @FXML
    private TextField idField;

    @FXML
    private TextField rowsField;

    @FXML
    private TextField colsField;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Label errorLabel;

    private AnchorPane popupOverlay;
    private ShelfController mainController;

    public void setPopupOverlay(AnchorPane popupOverlay) {
        this.popupOverlay = popupOverlay;
    }

    public void setMainController(ShelfController controller) {
        this.mainController = controller;
    }

    @FXML
    public void initialize() {
        cancelBtn.setOnAction(e -> closePopup());
        addBtn.setOnAction(e -> submit());
    }

    private void closePopup() {
        if (mainController != null && popupOverlay != null) {
            mainController.removePopup(popupOverlay);
        }
    }

    private void submit() {
        if (errorLabel != null) {
            errorLabel.setText("");
        }

        String id = idField.getText();
        String rowsText = rowsField.getText();
        String colsText = colsField.getText();

        if (id == null || id.isBlank() || rowsText == null || colsText == null) {
            showError("Please fill in all fields.");
            return;
        }

        Integer rows;
        Integer cols;

        try {
            rows = Integer.parseInt(rowsText);
            cols = Integer.parseInt(colsText);
        } catch (NumberFormatException e) {
            showError("Rows and Columns must be integers.");
            return;
        }

        String result = CheECSEManagerFeatureSet2Controller.addShelf(id, cols, rows);

        if (result != null) {
            showError(result);
        } else {
            mainController.refreshTable();
            closePopup();
        }
    }

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
        } else {
            System.err.println("Add Shelf Error: " + message);
        }
    }
}