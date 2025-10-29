package ca.mcgill.ecse.cheecsemanager.fxml.layout;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

public class ToastFactory {

    private enum ToastType {
        ERROR, SUCCESS, CONFIRM
    }

    private static void show(Node node, String title, String message, ToastType type) {
        // Positioning
        Window window = node.getScene().getWindow();
        double popupWidth = 320; // approximate width of your popup
        double popupHeight = 80; // approximate height of your popup
        double margin = 30; // margin from the edges
        double x = window.getX() + window.getWidth() - popupWidth - margin;
        double y = window.getY() + window.getHeight() - popupHeight - margin;

        // Create and show popup
        Popup popup = new Popup();
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("title");
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("text");
        VBox content = new VBox(titleLabel, messageLabel);
        content.getStyleClass().add("notification");
        content.getStyleClass().add(type.name().toLowerCase());
        content.setMinWidth(popupWidth);
        content.setMinHeight(popupHeight);
        content.setMaxWidth(popupWidth);
        content.setMaxHeight(popupHeight);
        popup.getContent().add(content);
        popup.show(window, x, y);

        // Optional fade out
        FadeTransition fade = new FadeTransition(Duration.seconds(2), content);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.seconds(2));
        fade.setOnFinished(e -> popup.hide());
        fade.play();
    }

    public static void createError(Node node, String message) {
        show(node, "Error", message, ToastType.ERROR);
    }

    public static void createSuccess(Node node, String message) {
        show(node, "Success", message, ToastType.SUCCESS);
    }

    public static void createConfirm(Node node, String message) {
        show(node, "Confirmation", message, ToastType.CONFIRM);
    }
}
