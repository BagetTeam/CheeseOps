package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import java.util.function.Consumer;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet5Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for updating an existing wholesale company's details.
 * Handles form validation and company modification through the backend controller.
 */
public class UpdateWholesaleCompanyController {

  @FXML
  private TextField nameField;

  @FXML 
  private TextField addressField;

  @FXML
  private HBox errorContainer;

  @FXML
  private Label errorLabel;

  @FXML
  private Label currentCompanyLabel;

  private String currentCompanyName;
  private Consumer<String> onCloseCallback;
  private ToastProvider mainController;

    /**
   * Registers a callback to execute when the dialog closes.
   * Callback receives the updated company name or null if unchanged.
   *
   * @param callback the action to run on dialog close, accepting updated name
   */
  public void setOnClose(Consumer<String> callback) {
  this.onCloseCallback = callback;
  }

    /**
   * Sets the main controller to enable toast notifications.
   *
   * @param controller the parent controller implementing ToastProvider
   */
  public void setMainController(ToastProvider controller) {
    this.mainController = controller;
  }

  /**
   * Set the company to be deleted and populate the dialog with company info
   * @param companyName The name of the company to delete
   */
  public void setCompany(String companyName) {
    this.currentCompanyName = companyName;
    
    // Fetch company details from backend
    TOWholesaleCompany company = CheECSEManagerFeatureSet6Controller.getWholesaleCompany(companyName);
    
    if (company != null) {
      currentCompanyLabel.setText(company.getName());
      nameField.setText(company.getName());
      addressField.setText(company.getAddress());
    }
  }

  @FXML
  private void handleClose() {
    if (onCloseCallback != null) {
      onCloseCallback.accept(null);
    }
  }

    /**
   * Validates form inputs and updates the wholesale company.
   * Shows error messages for validation failures or displays success toast.
   * Passes updated company name to callback for view refresh.
   */
  @FXML
  private void handleSave() {
    hideError();

    String newName = nameField.getText();
    String newAddress = addressField.getText();

    String error = CheECSEManagerFeatureSet5Controller.updateWholesaleCompany(currentCompanyName, newName, newAddress);

    if (error.isEmpty()) {
      if (mainController != null) {
        mainController.showSuccessToast("✓ Company \"" + currentCompanyName + "\" updated successfully!");
      }
      if (onCloseCallback != null) {
          onCloseCallback.accept(newName);
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
