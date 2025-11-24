package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet5Controller;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

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
  private Runnable onCloseCallback;
  private MainController mainController;

  public void setOnClose(Runnable callback) {
  this.onCloseCallback = callback;
  }

  public void setMainController(MainController controller) {
    this.mainController = controller;
  }

    /**
   * Set the company to be updated
   * @param companyName The current name of the company
   */
  public void setCompany(String companyName) {
    this.currentCompanyName = companyName;
    this.currentCompanyLabel.setText(companyName);
  }

  @FXML
  private void handleClose() {
    if (onCloseCallback != null) {
      onCloseCallback.run();
    }
  }

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
