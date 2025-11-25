package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class AddFarmerPopup {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField addressField;
    @FXML private Label errorLabel;
    @FXML private ImageView photoView;
    @FXML private Label photoPlaceholder;

    private AnchorPane popupOverlay;
    private FarmerController farmerController;

    public void setPopupOverlay(AnchorPane overlay) {
        this.popupOverlay = overlay;
    }

    public void setFarmerController(FarmerController controller) {
        this.farmerController = controller;
    }

    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    @FXML
    private void onUploadPhoto() {
        // Logic not implemented as requested
        System.out.println("Upload photo clicked");
    }

    @FXML
    private void onCancel() {
        closePopup();
    }

    @FXML
    private void onAdd() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String address = addressField.getText();

        if (email == null || email.trim().isEmpty()) {
            errorLabel.setText("Email is required.");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            errorLabel.setText("Password is required.");
            return;
        }
        if (address == null || address.trim().isEmpty()) {
            errorLabel.setText("Address is required.");
            return;
        }

        // Basic mock creation since we don't have full access to the main CheECSEManager instance structure yet
        // Ideally, FarmerController should expose the manager or a method to add a farmer.
        // For now, we will try to use the FarmerController's create method if it exists, or just mock it.
        
        if (farmerController != null) {
             try {
                 CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
                 
                 Farmer newFarmer = new Farmer(email, password, address, manager);
                 if (name != null && !name.trim().isEmpty()) {
                     newFarmer.setName(name);
                 }
                 
                 CheECSEManagerPersistence.save();
                 
                 // Pass back to controller to display
                 farmerController.addNewFarmerToList(newFarmer);
                 closePopup();
             } catch (RuntimeException e) {
                 errorLabel.setText(e.getMessage());
             }
        } else {
            errorLabel.setText("Internal Error: Controller not connected.");
        }
    }

    private void closePopup() {
        if (farmerController != null && popupOverlay != null) {
            farmerController.removePopup(popupOverlay);
        }
    }
}
