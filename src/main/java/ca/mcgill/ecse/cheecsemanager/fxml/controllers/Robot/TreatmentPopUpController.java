package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Robot;

import ca.mcgill.ecse.cheecsemanager.controller.RobotController;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Dropdown;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.controller.PurchaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Controller for the Treatment Popup UI.
 * 
 * @author Benjamin Curis-Friedman
 */
public class TreatmentPopUpController {

  @FXML private VBox root;
  @FXML private Dropdown purchaseIdDropdown;
  @FXML private Dropdown maturationDropdown;
  @FXML private Label errorLabel;
  @FXML private StyledButton startBtn;

  /** Wires the dropdowns, loads options, and sets up validation bindings. */
  @FXML
  public void initialize() {
    maturationDropdown.setItems(java.util.Arrays.asList("Six", "Twelve", "TwentyFour", "ThirtySix"));
    purchaseIdDropdown.setItems(PurchaseController.getAllPurchaseIds());

    // Disable Start until fields are filled
    startBtn.disableProperty().bind(
        purchaseIdDropdown.getComboBox().valueProperty().isNull()
            .or(maturationDropdown.getComboBox().valueProperty().isNull()));

    startBtn.setOnAction(e -> submit());
  }

  /** Closes the popup once treatment has been triggered. */
  private void closePopup() {
    root.fireEvent(new HidePopupEvent());
  }

  /** Validates inputs and starts the robot treatment workflow. */
  private void submit() {
    if (errorLabel != null) {
      errorLabel.setText("");
      errorLabel.setVisible(false);
      errorLabel.setManaged(false);
    }

    String pidVal = purchaseIdDropdown.getSelectedValue();
    String period = maturationDropdown.getSelectedValue();

    if (pidVal == null || pidVal.isBlank() || period == null) {
      showError("Please fill in all fields.");
      return;
    }

    int purchaseId;
    try {
      purchaseId = Integer.parseInt(pidVal.trim());
    } catch (NumberFormatException e) {
      showError("Purchase ID must be an integer.");
      System.out.println(e.getMessage());
      return;
    }

    try {
      System.out.println("TREATMENT SUBMIT: PID=" + purchaseId + ", PERIOD=" + period);
      System.out.println("Initializing treatment...");
      // startBtn.setText("Loading...");
      RobotController.initializeTreatment(purchaseId, period);
      root.fireEvent(new ToastEvent("Treatment completed succsessfully.",
                                  ToastEvent.ToastType.SUCCESS));
      System.out.println("Treatment ended successfully.");
      closePopup();
    } catch (RuntimeException ex) {
      showError(ex.getMessage() == null ? "Failed to start treatment." : ex.getMessage());
      System.out.println(ex.getMessage());
    }
  }

  /** Displays an error message underneath the form. */
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
