package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

public class PopupController {
    // Create a semi-transparent overlay behind the popup
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
