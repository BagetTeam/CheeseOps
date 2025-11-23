package ca.mcgill.ecse.cheecsemanager.fxml.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class FarmerCard extends VBox {
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label addressLabel;
    @FXML private Label cheeseLabel;
    
    private Farmer farmerData; 
    
    public FarmerCard() {
        // Load the FXML. 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/cheecsemanager/view/components/FarmerCard/FarmerCard.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FarmerCard.fxml", e);
        }
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
    
    @FXML
    private void handleView() {
        if (farmerData != null) {
            System.out.println("View clicked for: " + farmerData.getName());
            // TODO: Implement view logic redirect to Farmer View
        }
    }
    
    @FXML
    private void handleDelete() {
        if (farmerData != null) {
            System.out.println("Delete clicked for: " + farmerData.getName());
            // Remove from parent container
            if (getParent() instanceof Pane) {
                ((Pane) getParent()).getChildren().remove(this);
            }
        }
    }
}
