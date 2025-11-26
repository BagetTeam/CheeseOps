package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

public class DeleteFarmerPopup {
    @FXML private Button yesBtn;
    @FXML private Button noBtn;
    @FXML private Label errorLabel;
    @FXML private FlowPane cardsContainer;

    private StackPane popupOverlay;
    private FarmerController farmerController;
    private ViewFarmerController viewFarmerController;

    private Farmer farmer;
    private FarmerCard farmerCard;

    public void setPopupOverlay(StackPane overlay) {
        this.popupOverlay = overlay;
    }

    public void setFarmerController(FarmerController controller) {
        this.farmerController = controller;
    }

    public void setViewFarmerController(ViewFarmerController controller) {
        this.viewFarmerController = controller;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }
    
    public void setFarmerCard(FarmerCard card) {
        this.farmerCard = card;
        this.farmer = card.getFarmer();
    }

    @FXML
    public void initialize() {
        errorLabel.setText("");
        yesBtn.setOnAction(e -> confirmDelete());
        noBtn.setOnAction(e -> closePopup());
    }

    private void closePopup() {
        if (farmerController != null && popupOverlay != null) {
            farmerController.removePopup(popupOverlay);
        }
        if (viewFarmerController != null && popupOverlay != null) {
            viewFarmerController.removePopup(popupOverlay);
        }
    }

    private void confirmDelete() {
        if (this.farmer != null) {
            // Try to delete the farmer
            if (farmerController != null) {
                String error = farmerController.deleteFarmerCard(farmer, farmerCard, popupOverlay);
                if (error != null && !error.isEmpty()) {
                    errorLabel.setText(error);
                }
            }
            if (viewFarmerController != null) {
                String error = viewFarmerController.deleteFarmer(farmer, popupOverlay);
                if (error != null && !error.isEmpty()) {
                    errorLabel.setText(error);
                }
            }
        } else {
            closePopup();
        }
    }
}
