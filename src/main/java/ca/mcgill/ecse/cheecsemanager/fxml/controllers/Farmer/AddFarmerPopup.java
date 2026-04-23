package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.FarmerDataProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Controller for the add farmer popup
 * Handles adding a new farmer to the system
 * @author Ewen Gueguen
 */
public class AddFarmerPopup {

  @FXML private TextField nameField;
  @FXML private TextField emailField;
  @FXML private PasswordField passwordField;
  @FXML private TextField addressField;
  @FXML private Label errorLabel;
  @FXML private ImageView photoView;
  @FXML private Label photoPlaceholder;

  private StackPane popupOverlay;
  private FarmerController farmerController;

  /** Stores the overlay reference so the popup can dismiss itself. */
  public void setPopupOverlay(StackPane overlay) {
    this.popupOverlay = overlay;
  }

  /** Connects the popup to the farmer controller for callbacks and toasts. */
  public void setFarmerController(FarmerController controller) {
    this.farmerController = controller;
  }

  /** Clears the error label when the popup loads. */
  @FXML
  public void initialize() {
    errorLabel.setText("");
  }

  /** Handles cancel button presses. */
  @FXML
  private void onCancel() {
    closePopup();
  }

  /** Validates inputs and registers a new farmer. */
  @FXML
  private void onAdd() {
    String name = nameField.getText();
    String email = emailField.getText();
    String password = passwordField.getText();
    String address = addressField.getText();

    if (email == null || email.trim().isEmpty()) {
      errorLabel.setText("Email is required.");
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);
      return;
    }
    if (password == null || password.trim().isEmpty()) {
      errorLabel.setText("Password is required.");
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);
      return;
    }
    if (address == null || address.trim().isEmpty()) {
      errorLabel.setText("Address is required.");
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);
      return;
    }

    errorLabel.setText("");
    errorLabel.setVisible(false);
    errorLabel.setManaged(false);
    if (farmerController != null) {
      try {
        String error = CheECSEManagerFeatureSet3Controller.registerFarmer(
            email, password, name, address);
        if (error != null && !error.isEmpty()) {
          farmerController.getFarmerRoot().fireEvent(
              new ToastEvent("Failed adding farmer: " + error + ".",
                             ToastEvent.ToastType.ERROR));
          return;
        }
        FarmerDataProvider.getInstance().refresh();
        closePopup();
        farmerController.getFarmerRoot().fireEvent(new ToastEvent(
            "Farmer added successfully.", ToastEvent.ToastType.SUCCESS));
      } catch (RuntimeException e) {
        farmerController.getFarmerRoot().fireEvent(
            new ToastEvent("Failed adding farmer: " + e.getMessage() + ".",
                           ToastEvent.ToastType.ERROR));
        return;
      }
    } else {
      farmerController.getFarmerRoot().fireEvent(
          new ToastEvent("Internal Error: Controller not connected.",
                         ToastEvent.ToastType.ERROR));
      return;
    }
  }

  /** Removes the popup overlay from the farmer controller. */
  private void closePopup() {
    if (farmerController != null && popupOverlay != null) {
      farmerController.removePopup(popupOverlay);
    }
  }
}
