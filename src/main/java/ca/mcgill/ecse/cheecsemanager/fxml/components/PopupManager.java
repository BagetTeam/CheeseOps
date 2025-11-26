package ca.mcgill.ecse.cheecsemanager.fxml.components;

import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * @author Ming Li Liu
 * */
public class PopupManager {
  private StackPane rootStackPane;
  private Region veil;
  private Node currentPopup;
  private BoxBlur blurEffect;

  public PopupManager() { blurEffect = new BoxBlur(5, 5, 3); }

  public void initialize(StackPane rootStackPane, Region veil) {
    this.rootStackPane = rootStackPane;
    this.veil = veil;
  }

  public void showPopup(Node popupContent) {
    if (rootStackPane == null)
      return;

    // Remove existing popup if any
    hidePopup();

    // Apply blur to main content (first child)
    Node mainContent = rootStackPane.getChildren().get(0);
    mainContent.setEffect(blurEffect);

    // Show veil
    veil.setVisible(true);
    veil.setManaged(true);

    // Add popup
    currentPopup = popupContent;
    StackPane.setAlignment(popupContent, javafx.geometry.Pos.CENTER);
    rootStackPane.getChildren().add(popupContent);
  }

  public void hidePopup() {
    if (rootStackPane == null)
      return;

    // Remove blur
    Node mainContent = rootStackPane.getChildren().get(0);
    mainContent.setEffect(null);

    // Hide veil
    veil.setVisible(false);
    veil.setManaged(false);

    // Remove popup
    if (currentPopup != null) {
      rootStackPane.getChildren().remove(currentPopup);
      currentPopup = null;
    }
  }
}
