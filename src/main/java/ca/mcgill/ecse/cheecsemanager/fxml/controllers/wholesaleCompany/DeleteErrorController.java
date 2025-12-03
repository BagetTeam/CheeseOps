package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for the popup that explains why a company delete failed.
 *
 * @author Oliver Mao
 * */
public class DeleteErrorController {
  @FXML private Label errorMessageLabel;

  private Runnable onCloseCallback;

  /**
   * Registers a callback invoked when the popup is dismissed.
   * @param callback code to run on close, typically hiding the popup
   */
  public void setOnClose(Runnable callback) { this.onCloseCallback = callback; }

  /**
   * Set the error message to display
   * @param message The error message
   */
  public void setErrorMessage(String message) {
    errorMessageLabel.setText(message);
  }

  /** Handles the close button press and runs the optional callback. */
  @FXML
  private void handleClose() {
    if (onCloseCallback != null) {
      onCloseCallback.run();
    }
  }
}
