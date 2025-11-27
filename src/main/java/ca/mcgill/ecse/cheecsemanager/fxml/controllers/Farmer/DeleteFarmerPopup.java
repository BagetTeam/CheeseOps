package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

/**
 * Controller for the detele farmer confirmation popup
 * Handles deleting a farmer from the system
 * @author Ewen Gueguen
 */
public class DeleteFarmerPopup {
    @FXML private Button yesBtn;
    @FXML private Button noBtn;
    @FXML private Label warningLabel;
    @FXML private Label errorLabel;
    @FXML private FlowPane cardsContainer;

    private StackPane popupOverlay;
    private FarmerController farmerController;
    private ViewFarmerController viewFarmerController;

    private TOFarmer farmer;
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

    public void setFarmer(TOFarmer farmer) {
        this.farmer = farmer;
    
        warningLabel.setText("Are you sure you want to delete farmer " +
                         farmer.getEmail() +
                         " (" + farmer.getName() + ")? This action cannot be undone.");
    }
    
    public void setFarmerCard(FarmerCard card) {
        this.farmerCard = card;
        setFarmer(card.getFarmer());
    }

    @FXML
    public void initialize() {
        if (farmer != null) {
            warningLabel.setText("Are you sure you want to delete farmer " +
                             farmer.getEmail() +
                            " (" + farmer.getName() + ")? This action cannot be undone.");
        }
        else {
            warningLabel.setText("");
        }
        yesBtn.setOnAction(e -> confirmDelete());
        noBtn.setOnAction(e -> closePopup());
    }

    @FXML
    public void handleClose() {
        closePopup();
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
                    errorLabel.setVisible(true);
                    errorLabel.setManaged(true);
                    return;
                }
            }
            if (viewFarmerController != null) {
                String error = viewFarmerController.deleteFarmer(farmer, popupOverlay);
                if (error != null && !error.isEmpty()) {
                    errorLabel.setText(error);
                    errorLabel.setVisible(true);
                    errorLabel.setManaged(true);
                    return;
                }
            }
        } else {
            errorLabel.setText("Error: Farmer not found.");
        }
    }
}
