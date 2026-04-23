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

/**
 * Generic popup shell that loads arbitrary FXML content and handles dismissal.
 * @author Ming Li Liu
 */
public class PopupController {
  @FXML private VBox popupContainer;
  @FXML private StyledButton closeButton;
  @FXML private VBox popup;
  @FXML private HBox popupHeader;

  /** Prevents popup clicks from propagating and wires up close behavior. */
  @FXML
  private void initialize() {
    popupContainer.setOnMouseClicked(e -> e.consume());
    popup.setOnMouseClicked(e -> popup.fireEvent(new HidePopupEvent()));
  }

  /**
   * Loads the provided FXML into the popup without a custom header title.
   * @param fxml resource path relative to the application
   */
  public void setContent(String fxml) {
    loadContent(fxml);

    closeButton.setText("Close");
  }

  /**
   * Loads FXML content and renders a header title.
   * @param fxmlPath popup body resource
   * @param title header title to display
   */
  public void setContent(String fxmlPath, String title) {
    loadContent(fxmlPath);

    Label titleLabel = new Label(title);
    titleLabel.getStyleClass().add("text-fg");
    popupHeader.getChildren().add(titleLabel);
  }

  /**
   * Loads and mounts arbitrary FXML into the popup container.
   * @param fxmlPath resource path under {@code view/}
   */
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

  /**
   * Adds custom style classes to the popup root for contextual theming.
   */
  public void setStyleClasses(String... styleClasses) {
    popup.getStyleClass().addAll(styleClasses);
  }

  /** Handles the close button press by emitting a hide event. */
  @FXML
  private void handleClose() {
    // Fire hide event that bubbles up to root
    popup.fireEvent(new HidePopupEvent());
  }

  /**
   * Creates a semi-transparent overlay that sits behind the popup.
   * @return overlay stack pane ready to be added to the scene
   */
  public StackPane createOverlay() {
    StackPane overlay = new StackPane();
    overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");

    AnchorPane.setTopAnchor(overlay, 0.0);
    AnchorPane.setBottomAnchor(overlay, 0.0);
    AnchorPane.setLeftAnchor(overlay, 0.0);
    AnchorPane.setRightAnchor(overlay, 0.0);

    return overlay;
  }

  /**
   * Removes the popup overlay and clears any effects on the root container.
   */
  public void removePopup(StackPane overlay, AnchorPane root) {
    root.setEffect(null);
    root.getChildren().remove(overlay);
  }
}
