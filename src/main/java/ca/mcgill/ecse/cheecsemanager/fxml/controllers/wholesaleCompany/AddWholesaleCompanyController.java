package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet5Controller;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class AddWholesaleCompanyController {

  @FXML
  private TextField nameField;

  @FXML 
  private TextField addressField;

  @FXML
  private HBox errorContainer;

  @FXML
  private Label errorLabel;
  
  private Runnable onCloseCallback;
  private MainController mainController;

  public void setOnClose(Runnable callback) {
  this.onCloseCallback = callback;
  }

  public void setMainController(MainController controller) {
    this.mainController = controller;
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

    String name = nameField.getText();
    String address = addressField.getText();

    String error = CheECSEManagerFeatureSet5Controller.addWholesaleCompany(name, address);

    if (error.isEmpty()) {
      if (mainController != null) {
        mainController.showSuccessToast("✓ Company \"" + name + "\" added successfully!");
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
