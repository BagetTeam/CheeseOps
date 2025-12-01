package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet5Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for adding a new wholesale company to the system.
 * Handles form validation and company creation through the backend controller.
 */
public class AddWholesaleCompanyController {

  @FXML private TextField nameField;

  @FXML private TextField addressField;

  @FXML private HBox errorContainer;

  @FXML private Label errorLabel;

  private Runnable onCloseCallback;
  private WholesaleCompanyPageController mainController;

  /**
   * Registers a callback to execute when the dialog closes.
   *
   * @param callback the action to run on dialog close
   */
  public void setOnClose(Runnable callback) { this.onCloseCallback = callback; }

  /**
   * Sets the main controller to enable toast notifications.
   *
   * @param controller the parent controller implementing ToastProvider
   */
  public void setMainController(WholesaleCompanyPageController controller) {
    this.mainController = controller;
  }

  @FXML
  private void handleClose() {
    if (onCloseCallback != null) {
      onCloseCallback.run();
    }
  }

  /**
   * Validates form inputs and creates a new wholesale company.
   * Shows error messages for validation failures or displays success toast.
   */
  @FXML
  private void handleSave() {
    hideError();

    String name = nameField.getText();
    String address = addressField.getText();

    String error =
        CheECSEManagerFeatureSet5Controller.addWholesaleCompany(name, address);

    if (error.isEmpty()) {
      if (mainController != null) {
        // mainController.showSuccessToast("✓ Company \"" + name + "\" added
        // successfully!");
      }
      handleClose();
    } else {
      showError(error);
    }
  }

  private void showError(String message) {
    errorLabel.setText(message);
    errorContainer.setVisible(true);
    errorContainer.setManaged(true);
  }

  private void hideError() {
    errorContainer.setVisible(false);
    errorContainer.setManaged(false);
  }
}
