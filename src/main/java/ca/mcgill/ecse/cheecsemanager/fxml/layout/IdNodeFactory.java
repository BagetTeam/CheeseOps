package ca.mcgill.ecse.cheecsemanager.fxml.layout;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class IdNodeFactory {

    public static Node createIdNode(String fullText, Runnable onClick) {
        Node idNode = null;
        if (onClick != null) {
            Hyperlink link = new Hyperlink();
            link.setText(fullText);
            link.setOnAction(e -> onClick.run());
            StackPane pane = new StackPane();
            pane.getStyleClass().add("hyperlink-container");
            pane.getChildren().add(link);
            idNode = pane;
        } else {
            idNode = new StackPane(new Text(fullText));
        }
        return idNode;
    }
}
