package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Robot;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Dropdown;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.List;
import ca.mcgill.ecse.cheecsemanager.controller.RobotController;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;

import javafx.fxml.FXML;

/** Popup controller for initializing the robot at a specific shelf. */
public class InitializeRobotController {

  @FXML private VBox root;
  @FXML private Dropdown ShelfIdDropdown;
  @FXML private StyledButton startBtn;
  @FXML private Label errorLabel;

  /** Loads dropdown options and wires button bindings. */
  @FXML
  public void initialize() {
    bindings();

    List<String> items = CheECSEManagerFeatureSet1Controller.getAllShelfIds();
    ShelfIdDropdown.setItems(items);
  }

  /** Closes the popup once initialization succeeds. */
  private void closePopup() {
    root.fireEvent(new HidePopupEvent());
  }

  /** Sets up basic validation bindings and handlers for the action button. */
  private void bindings() {
    // Disable Start until fields are filled
    startBtn.disableProperty().bind(
        ShelfIdDropdown.getComboBox().valueProperty().isNull());

    startBtn.setOnAction(e -> submit());
  }

  /** Validates input and calls the robot controller to initialize. */
  private void submit() {
    String shelfIdVal = ShelfIdDropdown.getSelectedValue();

    // this is pretty useless cuz there
    if (shelfIdVal == null || shelfIdVal.isBlank()) {
      showError("Please fill in all fields.");
    }

    try {
      RobotController.initializeRobot(shelfIdVal);
      root.fireEvent(new ToastEvent("Robot initialized succsessfully.",
                            ToastEvent.ToastType.SUCCESS));
      closePopup();
    } catch (RuntimeException e) {
      errorLabel.setText(e.getMessage());
    }
  }

    /** Shows validation or controller errors within the popup. */
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
