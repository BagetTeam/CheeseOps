package ca.mcgill.ecse.cheecsemanager.fxml.components;

import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import javafx.scene.layout.VBox;

public class FarmerCard extends VBox {
    @FXML private Label nameLabel;
    @FXML private Label cheeseLabel;
    
    private Farmer farmerData; 
    
    public FarmerCard() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FarmerCard.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FarmerCard.fxml", e);
        }
    }
    
    // set info for farmer
    public void setFarmer(String name, double cheeseKg) {
        nameLabel.setText(name);
        cheeseLabel.setText("Cheese: " + cheeseKg + " kg");
    }
    
    @FXML
    private void handleView() {
        System.out.println("View clicked for: " + nameLabel.getText());
        // TODO: Implement view logic
    }
    
    @FXML
    private void handleDelete() {
        System.out.println("Delete clicked for: " + nameLabel.getText());
        // TODO: Implement delete logic (e.g., remove from parent)
        if (getParent() instanceof Pane) {
            ((Pane) getParent()).getChildren().remove(this);
        }
    }
}
