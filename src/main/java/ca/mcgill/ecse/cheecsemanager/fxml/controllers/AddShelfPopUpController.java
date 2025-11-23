package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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

    private AnchorPane popupOverlay; // reference to the overlay added by main controller
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
        String id = idField.getText();
        String rows = rowsField.getText();
        String cols = colsField.getText();

        System.out.println("ID: " + id + ", Rows: " + rows + ", Cols: " + cols);

        // TODO: Add logic to handle these values in main controller

        closePopup();
    }
}