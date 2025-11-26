package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PopupController {
  @FXML private Label messageLabel;

  public void setContent(String content) { messageLabel.setText(content); }

  @FXML
  private void handleClose() {
    // Fire hide event that bubbles up to root

    messageLabel.fireEvent(new HidePopupEvent());
  }
}
