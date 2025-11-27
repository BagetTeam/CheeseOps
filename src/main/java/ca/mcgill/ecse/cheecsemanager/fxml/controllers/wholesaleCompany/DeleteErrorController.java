package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DeleteErrorController {
  @FXML
  private Label errorMessageLabel;

  private Runnable onCloseCallback;

  public void setOnClose(Runnable callback) {
    this.onCloseCallback = callback;
  } 
  
  /**
   * Set the error message to display
   * @param message The error message
   */
  public void setErrorMessage(String message) {
    errorMessageLabel.setText(message);
  }

  @FXML
  private void handleClose() {
    if (onCloseCallback != null) {
      onCloseCallback.run();
    }
  }
}
