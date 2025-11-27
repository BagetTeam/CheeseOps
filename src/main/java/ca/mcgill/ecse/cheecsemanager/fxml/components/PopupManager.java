package ca.mcgill.ecse.cheecsemanager.fxml.components;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

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

    // animate
    this.animateIn(popupContent, veil);
  }

  private void animateIn(Node popupContent, Node veil) {
    FadeTransition fadeIn =
        new FadeTransition(Duration.millis(100), popupContent);
    fadeIn.setFromValue(0);
    fadeIn.setToValue(1);

    FadeTransition fadeInViel = new FadeTransition(Duration.millis(100), veil);
    fadeIn.setFromValue(0);
    fadeIn.setToValue(1);

    ScaleTransition scaleIn =
        new ScaleTransition(Duration.millis(100), popupContent);
    scaleIn.setFromX(0.9);
    scaleIn.setFromY(0.9);
    scaleIn.setToX(1);
    scaleIn.setToY(1);

    var animation = new ParallelTransition(fadeIn, scaleIn, fadeInViel);
    animation.play();
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
