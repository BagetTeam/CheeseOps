package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Robot;

import ca.mcgill.ecse.cheecsemanager.controller.RobotController;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Dropdown;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class TreatmentPopUpController {

  @FXML private VBox root;
  @FXML private TextField purchaseIdField;
  @FXML private Dropdown maturationDropdown;
  @FXML private Label errorLabel;
  @FXML private Button startBtn;

  

  @FXML
  public void initialize() {
    maturationDropdown.setItems(java.util.Arrays.asList("Six", "Twelve", "TwentyFour", "ThirtySix"));

    // Disable Start until fields are filled
    startBtn.disableProperty().bind(
        purchaseIdField.textProperty().isEmpty()
            .or(maturationDropdown.getComboBox().valueProperty().isNull()));

    startBtn.setOnAction(e -> submit());
  }

  private void closePopup() {
    root.fireEvent(new HidePopupEvent());
  }

  private void submit() {
    if (errorLabel != null) {
      errorLabel.setText("");
      errorLabel.setVisible(false);
      errorLabel.setManaged(false);
    }

    String pidText = purchaseIdField.getText();
    String period = maturationDropdown.getSelectedValue();

    if (pidText == null || pidText.isBlank() || period == null) {
      showError("Please fill in all fields.");
      return;
    }

    int purchaseId;
    try {
      purchaseId = Integer.parseInt(pidText.trim());
    } catch (NumberFormatException e) {
      showError("Purchase ID must be an integer.");
      return;
    }

    try {
      RobotController.initializeTreatment(purchaseId, period);
      closePopup();
    } catch (RuntimeException ex) {
      showError(ex.getMessage() == null ? "Failed to start treatment." : ex.getMessage());
    }
  }

  private void showError(String message) {
    if (errorLabel != null) {
      errorLabel.setText(message);
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);
    } else {
      System.err.println("Treatment Popup Error: " + message);
    }
  }
}
