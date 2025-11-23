package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import java.io.IOException;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer.FarmerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ViewFarmerPopup {

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
    private FarmerController farmerController;

    public ViewFarmerPopup() {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/cheecsemanager/view/components/Farmer/ViewFarmerPopup.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ViewFarmerPopup.fxml", e);
        }
    }

    public void setPopupOverlay(AnchorPane popupOverlay) {
        this.popupOverlay = popupOverlay;
    }

    public void setMainController(FarmerController controller) {
        this.farmerController = controller;
    }

    @FXML
    public void initialize() {
        cancelBtn.setOnAction(e -> closePopup());
        addBtn.setOnAction(e -> submit());
    }

    private void closePopup() {
        if (farmerController != null && popupOverlay != null) {
            farmerController.removePopup(popupOverlay);
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