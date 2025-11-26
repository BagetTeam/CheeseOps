package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

public class DeleteFarmerPopup {
    @FXML private Button yesBtn;
    @FXML private Button noBtn;
    @FXML private FlowPane cardsContainer;

    private StackPane popupOverlay;
    private FarmerController farmerController;

    private FarmerCard farmerCard;

    public void setPopupOverlay(StackPane overlay) {
        this.popupOverlay = overlay;
    }

    public void setFarmerController(FarmerController controller) {
        this.farmerController = controller;
    }

    public void setFarmerCard(FarmerCard farmerCard) {
        this.farmerCard = farmerCard;
    }

    @FXML
    public void initialize() {
        yesBtn.setOnAction(e -> confirmDelete());
        noBtn.setOnAction(e -> closePopup());
    }

    private void closePopup() {
        if (farmerController != null && popupOverlay != null) {
            farmerController.removePopup(popupOverlay);
        }
    }

    private void confirmDelete() {
        if (this.farmerCard != null) {
            // Remove from cards container and model
            farmerController.deleteFarmerCard(farmerCard, popupOverlay);
        } else {
            closePopup();
        }
    }
}
