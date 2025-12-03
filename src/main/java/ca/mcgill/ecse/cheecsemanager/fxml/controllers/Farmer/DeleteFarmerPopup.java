package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
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

    /** Stores the overlay container to remove when closing the popup. */
    public void setPopupOverlay(StackPane overlay) {
        this.popupOverlay = overlay;
    }

    /** Registers the farmer list controller to delegate deletion to. */
    public void setFarmerController(FarmerController controller) {
        this.farmerController = controller;
    }

    /** Registers the farmer detail controller to delegate deletion to. */
    public void setViewFarmerController(ViewFarmerController controller) {
        this.viewFarmerController = controller;
    }

    /** Injects the farmer whose deletion is being confirmed. */
    public void setFarmer(TOFarmer farmer) {
        this.farmer = farmer;
    
        warningLabel.setText("Are you sure you want to delete farmer " +
                         farmer.getEmail() +
                         " (" + farmer.getName() + ")? This action cannot be undone.");
    }
    
    /** Associates the popup with the originating farmer card. */
    public void setFarmerCard(FarmerCard card) {
        this.farmerCard = card;
        setFarmer(card.getFarmer());
    }

    /** Sets up button handlers and default warning text. */
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

    /** Handles the close button press. */
    @FXML
    public void handleClose() {
        closePopup();
    }

    /** Closes the popup for whichever controller opened it. */
    private void closePopup() {
        if (farmerController != null && popupOverlay != null) {
            farmerController.removePopup(popupOverlay);
        }
        if (viewFarmerController != null && popupOverlay != null) {
            viewFarmerController.removePopup(popupOverlay);
        }
    }

    /** Attempts to delete the farmer using whichever controller is present. */
    private void confirmDelete() {
        if (this.farmer != null) {
            // Try to delete the farmer
            if (farmerController != null) {
                String error = farmerController.deleteFarmerCard(farmer, farmerCard, popupOverlay);
                if (error != null && !error.isEmpty()) {
                    farmerController.getFarmerRoot().fireEvent(new ToastEvent("Failed deleting farmer: " + error + ".", ToastEvent.ToastType.ERROR));
                    return;
                }
            }
            if (viewFarmerController != null) {
                String error = viewFarmerController.deleteFarmer(farmer, popupOverlay);
                if (error != null && !error.isEmpty()) {
                    viewFarmerController.getViewFarmerRoot().fireEvent(new ToastEvent("Failed deleting farmer: " + error + ".", ToastEvent.ToastType.ERROR));
                    return;
                }
            }
        } else {
            farmerController.getFarmerRoot().fireEvent(new ToastEvent("Error: Farmer not found.", ToastEvent.ToastType.ERROR));
            return;
        }
    }
}
