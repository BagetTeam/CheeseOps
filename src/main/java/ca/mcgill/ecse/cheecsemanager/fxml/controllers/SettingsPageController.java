package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.fxml.components.*;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Controller that manages facility manager password changes from the settings
 * page.
 */
public class SettingsPageController {
  @FXML private StackPane rootPane;

  @FXML private Input oldPasswordInput;
  @FXML private Input newPasswordInput;
  @FXML private Input confirmPasswordInput;
  @FXML private Label errorLabel;

  /** Disables the old password field if no password is currently set. */
  @FXML
  void initialize() {
    boolean hasPassword =
        CheECSEManagerFeatureSet1Controller.facilityManagerHasPassword();

    if (!hasPassword) {
      this.oldPasswordInput.setDisable(true);
    }
  }

  /** Validates the password update form and persists the new credential. */
  @FXML
  private void handleSubmit() {
    boolean hasPassword =
        CheECSEManagerFeatureSet1Controller.facilityManagerHasPassword();

    String oldPassword = oldPasswordInput.getText();
    String newPassword = newPasswordInput.getText();
    String confirmPassword = confirmPasswordInput.getText();

    if ((hasPassword && oldPassword.isEmpty()) || newPassword.isEmpty() ||
        confirmPassword.isEmpty()) {
      this.showError("All fields are required.");
      return;
    }

    String storedOldPassword =
        CheECSEManagerFeatureSet1Controller.getFacilityManagerPassword();

    if (storedOldPassword != null && !storedOldPassword.equals(oldPassword)) {
      this.showError("Old password does not match.");
      return;
    }

    if (!newPassword.equals(confirmPassword)) {
      this.showError("Confirm password must match.");
      return;
    }

    if (newPassword.equals(storedOldPassword)) {
      this.showError("New password must be different from old password.");
      return;
    }

    String error =
        CheECSEManagerFeatureSet1Controller.updateFacilityManager(newPassword);

    if (error != null && !error.isEmpty()) {
      this.showError(error);
      return;
    }

    this.hideError();
    this.resetFields();

    rootPane.fireEvent(new ToastEvent("Password updated successfully",
                                      ToastEvent.ToastType.SUCCESS));
  }

  /**
   * Displays a validation or controller error directly on the form.
   * @param message error text for the user
   */
  private void showError(String message) {
    errorLabel.setText(message);
    errorLabel.setVisible(true);
    errorLabel.setManaged(true);
  }

  /** Clears the error label from the view. */
  private void hideError() {
    errorLabel.setVisible(false);
    errorLabel.setManaged(false);
  }

  /** Resets every password field back to an empty state. */
  private void resetFields() {
    oldPasswordInput.setText("");
    newPasswordInput.setText("");
    confirmPasswordInput.setText("");
  }
}
