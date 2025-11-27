package ca.mcgill.ecse.cheecsemanager.fxml.components;

import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import java.util.function.Consumer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class Toast extends HBox {
  private final SequentialTransition animation;
  private final Consumer<Toast> onFinished;

  public Toast(String message, ToastEvent.ToastType type,
               Consumer<Toast> onFinished) {
    this.onFinished = onFinished;

    // Styling
    getStyleClass().add("toast");
    getStyleClass().add("toast-" + type.name().toLowerCase());
    setAlignment(Pos.CENTER_LEFT);
    setSpacing(10);
    setPadding(new Insets(12, 16, 12, 16));
    setMaxWidth(Region.USE_PREF_SIZE);

    // icon
    Icon icon;

    switch (type) {
    case SUCCESS:
      icon = new Icon("CircleCheck");
      break;
    case WARNING:
      icon = new Icon("Warning");
      break;
    case ERROR:
      icon = new Icon("CircleX");
      break;
    default:
      icon = new Icon("Info");
      break;
    }

    // Message label
    Label messageLabel = new Label(message);
    messageLabel.getStyleClass().add("toast-message");
    HBox.setHgrow(messageLabel, Priority.ALWAYS);

    getChildren().addAll(icon, messageLabel);

    // Animation setup (fade in, pause, fade out)
    FadeTransition fadeIn = new FadeTransition(Duration.millis(300), this);
    fadeIn.setFromValue(0);
    fadeIn.setToValue(1);

    PauseTransition pause = new PauseTransition(Duration.seconds(2));

    FadeTransition fadeOut = new FadeTransition(Duration.millis(300), this);
    fadeOut.setFromValue(1);
    fadeOut.setToValue(0);

    animation = new SequentialTransition(fadeIn, pause, fadeOut);
    animation.setOnFinished(e -> onFinished.accept(this));
  }

  public void show() { animation.play(); }

  public void hide() {
    animation.stop();
    onFinished.accept(this);
  }
}
