package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PopupController {
  @FXML private VBox popupContainer;
  @FXML private StyledButton closeButton;
  @FXML private VBox popup;
  @FXML private HBox popupHeader;

  public void setContent(String fxml) {
    loadContent(fxml);

    closeButton.setText("Close");
  }

  public void setContent(String fxml, String title) {
    loadContent(fxml);

    Label titleLabel = new Label(title);
    titleLabel.getStyleClass().add("text-fg");
    popupHeader.getChildren().add(titleLabel);
  }

  private void loadContent(String fxml) {
    FXMLLoader loader =
        new FXMLLoader(CheECSEManagerApplication.getResource(fxml));

    try {
      Node content = loader.load();
      popupContainer.getChildren().add(content);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleClose() {
    // Fire hide event that bubbles up to root
    popup.fireEvent(new HidePopupEvent());
  }
}
