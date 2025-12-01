package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PopupController {
  @FXML private VBox popupContainer;
  @FXML private StyledButton closeButton;
  @FXML private VBox popup;
  @FXML private HBox popupHeader;

  @FXML
  private void initialize() {
    popupContainer.setOnMouseClicked(e -> e.consume());
    popup.setOnMouseClicked(e -> popup.fireEvent(new HidePopupEvent()));
  }

  public void setContent(String fxml) {
    loadContent(fxml);

    closeButton.setText("Close");
  }

  public void setContent(String fxmlPath, String title) {
    loadContent(fxmlPath);

    Label titleLabel = new Label(title);
    titleLabel.getStyleClass().add("text-fg");
    popupHeader.getChildren().add(titleLabel);
  }

  private void loadContent(String fxmlPath) {
    FXMLLoader loader =
        new FXMLLoader(CheECSEManagerApplication.getResource(fxmlPath));

    try {
      Node content = loader.load();
      popupContainer.getChildren().add(content);
      popupContainer.setMaxWidth(Region.USE_PREF_SIZE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setStyleClasses(String... styleClasses) {
    popup.getStyleClass().addAll(styleClasses);
  }

  @FXML
  private void handleClose() {
    // Fire hide event that bubbles up to root
    popup.fireEvent(new HidePopupEvent());
  }

  public StackPane createOverlay() {
    StackPane overlay = new StackPane();
    overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");

    AnchorPane.setTopAnchor(overlay, 0.0);
    AnchorPane.setBottomAnchor(overlay, 0.0);
    AnchorPane.setLeftAnchor(overlay, 0.0);
    AnchorPane.setRightAnchor(overlay, 0.0);

    return overlay;
  }

  // Remove popup and clear blur
  public void removePopup(StackPane overlay, AnchorPane root) {
    root.setEffect(null);
    root.getChildren().remove(overlay);
  }
}
