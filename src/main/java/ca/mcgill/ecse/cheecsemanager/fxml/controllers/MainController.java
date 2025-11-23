package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer.FarmerCard;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import java.sql.Date;

public class MainController {

    @FXML private FlowPane cardsContainer;

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
        cardsContainer.getChildren().add(card);
    }
}
