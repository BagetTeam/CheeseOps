package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;

public class ShelfController {

    @FXML
    private AnchorPane root;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private Button showPopupBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    public void initialize() {
        showPopupBtn.setOnAction(e -> showAddShelfPopup());
        deleteBtn.setOnAction(e -> showDeleteConfirmPopup());
    }

    // Add Shelf popup
    private void showAddShelfPopup() {
        contentPane.setEffect(new BoxBlur(5, 5, 3));
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/components/Shelf/AddShelfPopUp.fxml"
            ));
            AnchorPane popup = loader.load();

            centerPopup(popup);

            AnchorPane overlay = createOverlay(popup);

            root.getChildren().add(overlay);

            AddShelfPopUpController controller = loader.getController();
            controller.setPopupOverlay(overlay);
            controller.setMainController(this);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Delete confirmation popup
    private void showDeleteConfirmPopup() {
        contentPane.setEffect(new BoxBlur(5, 5, 3));
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/components/Shelf/ConfirmDelete.fxml"
            ));
            AnchorPane popup = loader.load();

            centerPopup(popup);

            AnchorPane overlay = createOverlay(popup);

            root.getChildren().add(overlay);

            ConfirmDeletePopUpController controller = loader.getController();
            controller.setPopupOverlay(overlay);
            controller.setMainController(this);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Center the popup in the root AnchorPane
    private void centerPopup(AnchorPane popup) {
        popup.setLayoutX((root.getWidth() - popup.getPrefWidth()) / 2);
        popup.setLayoutY((root.getHeight() - popup.getPrefHeight()) / 2);
    }

    // Create a semi-transparent overlay behind the popup
    private AnchorPane createOverlay(AnchorPane popup) {
        AnchorPane overlay = new AnchorPane();
        overlay.setPrefSize(root.getWidth(), root.getHeight());
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
        overlay.getChildren().add(popup);
        return overlay;
    }

    // Remove popup and clear blur
    public void removePopup(AnchorPane overlay) {
        contentPane.setEffect(null);
        root.getChildren().remove(overlay);
    }
}