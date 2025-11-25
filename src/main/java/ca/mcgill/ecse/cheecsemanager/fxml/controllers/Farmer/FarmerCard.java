package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class FarmerCard extends VBox {
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label addressLabel;
    @FXML private Label cheeseLabel;

    @FXML private Button viewFarmerBtn;
    @FXML private Button deleteFarmerBtn;
    
    private Farmer farmerData; 
    private FarmerController farmerController;

    
    public FarmerCard() {
        // Load the FXML. 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/cheecsemanager/view/components/Farmer/FarmerCard.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FarmerCard.fxml", e);
        }
    }

    public void setFarmerController(FarmerController farmerController) {
        this.farmerController = farmerController;
    }
    
    @FXML
    public void initialize() {
        viewFarmerBtn.setOnAction(e -> handleView());
        deleteFarmerBtn.setOnAction(e -> handleDelete());
    }

    // set info for farmer
    public void setFarmer(Farmer farmer) {
        this.farmerData = farmer;
        if (farmer != null) {
            nameLabel.setText("Name: " + farmer.getName());
            emailLabel.setText("Email: " + farmer.getEmail());
            addressLabel.setText("Address: " + farmer.getAddress());
            
            // Calculate cheese wheels
            int cheeseCount = 0;
            for (Purchase p : farmer.getPurchases()) {
                cheeseCount += p.numberOfCheeseWheels();
            }
            cheeseLabel.setText(String.valueOf(cheeseCount));
        }
    }

    public Farmer getFarmer() {
        return farmerData;
    }
    
    private void handleView() {
        if (farmerData != null) {
            ca.mcgill.ecse.cheecsemanager.fxml.controllers.PageNavigator.getInstance()
                .navigateTo("/ca/mcgill/ecse/cheecsemanager/view/page/farmers/viewFarmerPage.fxml", farmerData);
        }
    }
    
    private void handleDelete() {
        if (farmerController != null) {
            farmerController.deleteFarmerPopup(this);
        }
    }
}
