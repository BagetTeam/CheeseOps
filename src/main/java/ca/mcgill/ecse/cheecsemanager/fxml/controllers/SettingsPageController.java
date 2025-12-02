package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.fxml.components.*;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SettingsPageController {
  @FXML private StackPane rootPane;

  @FXML private Input oldPasswordInput;
  @FXML private Input newPasswordInput;
  @FXML private Input confirmPasswordInput;
  @FXML private Label errorLabel;

  @FXML
  void initialize() {
    boolean hasPassword =
        CheECSEManagerFeatureSet1Controller.facilityManagerHasPassword();

    if (!hasPassword) {
      this.oldPasswordInput.setDisable(true);
    }
  }

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

  private void showError(String message) {
    errorLabel.setText(message);
    errorLabel.setVisible(true);
    errorLabel.setManaged(true);
  }

  private void hideError() {
    errorLabel.setVisible(false);
    errorLabel.setManaged(false);
  }

  private void resetFields() {
    oldPasswordInput.setText("");
    newPasswordInput.setText("");
    confirmPasswordInput.setText("");
  }
}
