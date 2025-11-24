package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class DeleteFarmerPopup {
    @FXML private Button yesBtn;
    @FXML private Button noBtn;
    @FXML private FlowPane cardsContainer;

    private AnchorPane popupOverlay;
    private FarmerController farmerController;

    private FarmerCard farmerCard;

    public void setPopupOverlay(AnchorPane overlay) {
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
        System.out.println("Confirmed delete!");
        if (this.farmerCard != null) {
            // TODO: Add deletion logic here
            System.out.println("Delete clicked for: " + farmerCard.getFarmer().getName());
            // Remove from cards container
            farmerController.deleteFarmerCard(farmerCard, popupOverlay);
        }
        closePopup();
    }
}
