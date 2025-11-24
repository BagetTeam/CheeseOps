package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;

public class FarmerController {

    @FXML
    private AnchorPane farmerRoot;

    @FXML
    private FlowPane cardsContainer;


    @FXML
    public void addFarmer(javafx.event.ActionEvent event) {
        cardsContainer.setEffect(new BoxBlur(5, 5, 3));
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/components/Farmer/AddFarmer.fxml"
            ));
            AnchorPane popup = loader.load();
            centerPopup(popup);
            AnchorPane overlay = createOverlay(popup);
            farmerRoot.getChildren().add(overlay);

            AddFarmerPopup controller = loader.getController();
            controller.setFarmerController(this);
            controller.setPopupOverlay(overlay);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewFarmerToList(Farmer farmer) {
        addFarmerCard(farmer);
    }

    @FXML
    public void initialize() {
        CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
        for (Farmer farmer : manager.getFarmers()) {
            addFarmerCard(farmer);
        }
    }

    private void addFarmerCard(Farmer farmer) {
        FarmerCard card = new FarmerCard();
        card.setFarmer(farmer);
        card.setFarmerController(this);
        cardsContainer.getChildren().add(card);
    }

    public void deleteFarmerPopup(FarmerCard card) {
        cardsContainer.setEffect(new BoxBlur(5, 5, 3));
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/components/Farmer/DeleteFarmerPopup.fxml"
            ));
            AnchorPane popup = loader.load();
            centerPopup(popup);
            AnchorPane overlay = createOverlay(popup);
            farmerRoot.getChildren().add(overlay);

            DeleteFarmerPopup controller = loader.getController();
            controller.setFarmerCard(card);
            controller.setFarmerController(this);
            controller.setPopupOverlay(overlay);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void centerPopup(AnchorPane popup) {
        popup.setLayoutX((farmerRoot.getWidth() - popup.getPrefWidth()) / 2);
        popup.setLayoutY((farmerRoot.getHeight() - popup.getPrefHeight()) / 2);
    }

    // Create a semi-transparent overlay behind the popup
    private AnchorPane createOverlay(AnchorPane popup) {
        AnchorPane overlay = new AnchorPane();
        overlay.setPrefSize(farmerRoot.getWidth(), farmerRoot.getHeight());
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
        overlay.getChildren().add(popup);
        return overlay;
    }

    // Remove popup and clear blur
    public void removePopup(AnchorPane overlay) {
        cardsContainer.setEffect(null);
        farmerRoot.getChildren().remove(overlay);
    }

    public void deleteFarmerCard(FarmerCard card, AnchorPane overlay) {
        removePopup(overlay);
        cardsContainer.getChildren().remove(card);
        
        card.getFarmer().delete();
        CheECSEManagerPersistence.save();
    }
}