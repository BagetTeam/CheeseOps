package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import java.io.IOException;
import java.sql.Date;

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
        // TODO: Implement add farmer functionality
    }

    @FXML
    public void initialize() {
        CheECSEManager tempManager = new CheECSEManager();

        // Create dummy farmer 1
        Farmer f1 = new Farmer("john@doe.com", "pass123", "123 Farm Lane", tempManager);
        f1.setName("John Doe");
        
        // Add 5 cheese wheels to f1
        Purchase p1 = new Purchase(new Date(System.currentTimeMillis()), tempManager, f1);
        for (int i = 0; i < 5; i++) {
            new CheeseWheel(MaturationPeriod.Six, false, p1, tempManager);
        }

        // Create dummy farmer 2
        Farmer f2 = new Farmer("jane@smith.com", "securepass", "456 Country Rd", tempManager);
        f2.setName("Jane Smith");
        // Add 2 cheese wheels to f2
        Purchase p2 = new Purchase(new Date(System.currentTimeMillis()), tempManager, f2);
        new CheeseWheel(MaturationPeriod.Twelve, false, p2, tempManager);
        new CheeseWheel(MaturationPeriod.TwentyFour, false, p2, tempManager);

        // Add cards
        addFarmerCard(f1);
        addFarmerCard(f2);
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

        // TODO: Add delete farmer logic (for later)
    }
}