package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class UpdateFarmerPopup {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private TextField addressField;
    @FXML private Label errorLabel;
    @FXML private ImageView photoView;
    @FXML private Label photoPlaceholder;

    private StackPane popupOverlay;
    private ViewFarmerController farmerViewController;

    private TOFarmer farmerData;

    public void setFarmerData(TOFarmer farmer) {
        this.farmerData = farmer;
        nameField.setText(farmerData.getName());
        emailField.setText(farmerData.getEmail());
        passwordField.setText(farmerData.getPassword());
        addressField.setText(farmerData.getAddress());
    }

    public void setPopupOverlay(StackPane overlay) {
        this.popupOverlay = overlay;
    }

    public void setViewFarmerController(ViewFarmerController controller) {
        this.farmerViewController = controller;
    }

    @FXML
    public void initialize() {
      errorLabel.setText("");
    }

    @FXML
    private void onUploadPhoto() {
        System.out.println("Upload photo clicked");
    }

    @FXML
    private void onCancel() {
        closePopup();
    }

    @FXML
    private void onSave() {
        String name = nameField.getText();
        String password = passwordField.getText();
        String address = addressField.getText();

        if (password == null || password.trim().isEmpty()) {
            errorLabel.setText("Password is required.");
            return;
        }
        if (address == null || address.trim().isEmpty()) {
            errorLabel.setText("Address is required.");
            return;
        }

        
        if (farmerViewController != null) {
             try {
                 // Note: Email cannot be changed as it's immutable in the User model
                 String error = CheECSEManagerFeatureSet7Controller.updateFarmer(farmerData.getEmail(), password, name != null && !name.trim().isEmpty() ? name : null, address);
                 if (error != null && !error.isEmpty()) {
                    errorLabel.setText(error);
                    return;
                 }
                 farmerViewController.refreshFarmerCard(farmerData);
                 closePopup();
             } catch (RuntimeException e) {
                 errorLabel.setText(e.getMessage());
             }
        } else {
            errorLabel.setText("Internal Error: Controller not connected.");
        }
    }

    private void closePopup() {
        if (farmerViewController != null && popupOverlay != null) {
            farmerViewController.removePopup(popupOverlay);
        }
    }
}
