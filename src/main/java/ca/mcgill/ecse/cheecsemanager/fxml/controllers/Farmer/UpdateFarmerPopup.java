package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/**
 * Controller for the update farmer popup
 * Handles updating a farmer's data
 * @author Ewen Gueguen
 */
public class UpdateFarmerPopup {
  @FXML private TextField nameField;
  @FXML private TextField emailField;
  @FXML private TextField passwordField;
  @FXML private TextField addressField;
  @FXML private Label errorLabel;

  private StackPane popupOverlay;
  private ViewFarmerController farmerViewController;

  private TOFarmer farmerData;

  /**
   * Loads the selected farmer data into the popup fields.
   * @param farmer farmer transfer object to edit
   */
  public void setFarmerData(TOFarmer farmer) {
    this.farmerData = farmer;
    nameField.setText(farmerData.getName());
    emailField.setText(farmerData.getEmail());
    passwordField.setText(farmerData.getPassword());
    addressField.setText(farmerData.getAddress());
  }

  /** Saves the overlay reference so it can be removed later. */
  public void setPopupOverlay(StackPane overlay) {
    this.popupOverlay = overlay;
  }

  /** Connects the popup to the parent view controller for callbacks. */
  public void setViewFarmerController(ViewFarmerController controller) {
    this.farmerViewController = controller;
  }

  /** Clears error text when the popup is initialized. */
  @FXML
  public void initialize() {
    errorLabel.setText("");
  }

  /** Handles cancel button presses. */
  @FXML
  private void onCancel() {
    closePopup();
  }

  /** Validates the form and submits the farmer update request. */
  @FXML
  private void onSave() {
    String name = nameField.getText();
    String password = passwordField.getText();
    String address = addressField.getText();

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
    if (farmerViewController != null) {
      try {
        // Note: Email cannot be changed as it's immutable in the User model
        String error = CheECSEManagerFeatureSet7Controller.updateFarmer(
            farmerData.getEmail(), password,
            name != null && !name.trim().isEmpty() ? name : null, address);
        if (error != null && !error.isEmpty()) {
          farmerViewController.getViewFarmerRoot().fireEvent(
              new ToastEvent("Failed updating farmer: " + error + ".",
                             ToastEvent.ToastType.ERROR));
          return;
        }
        TOFarmer updatedFarmer = CheECSEManagerFeatureSet7Controller.getFarmer(
            farmerData.getEmail());
        if (updatedFarmer == null) {
          farmerViewController.getViewFarmerRoot().fireEvent(new ToastEvent(
              "Failed to reload farmer data.", ToastEvent.ToastType.ERROR));
          return;
        }
        this.farmerData = updatedFarmer;
        farmerViewController.refreshFarmerCard(updatedFarmer);
        closePopup();
        farmerViewController.getViewFarmerRoot().fireEvent(new ToastEvent(
            "Farmer updated successfully.", ToastEvent.ToastType.SUCCESS));
      } catch (RuntimeException e) {
        farmerViewController.getViewFarmerRoot().fireEvent(
            new ToastEvent("Failed updating farmer: " + e.getMessage() + ".",
                           ToastEvent.ToastType.ERROR));
        return;
      }
    } else {
      farmerViewController.getViewFarmerRoot().fireEvent(
          new ToastEvent("Internal Error: Controller not connected.",
                         ToastEvent.ToastType.ERROR));
      return;
    }
  }

  /** Removes the popup overlay via the parent controller. */
  private void closePopup() {
    if (farmerViewController != null && popupOverlay != null) {
      farmerViewController.removePopup(popupOverlay);
    }
  }
}
