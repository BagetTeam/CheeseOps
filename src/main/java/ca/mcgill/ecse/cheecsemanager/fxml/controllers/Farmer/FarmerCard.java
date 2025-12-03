package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Controller for the farmer card UI
 * Handles displaying farmer data and actions for the view and delete farmer
 * buttons
 * @author Ewen Gueguen
 */
public class FarmerCard extends VBox {
  @FXML private Label nameLabel;
  @FXML private Label emailLabel;
  @FXML private Label addressLabel;
  @FXML private Label cheeseLabel;

  @FXML private Button viewFarmerBtn;
  @FXML private Button deleteFarmerBtn;

  private TOFarmer farmerData;
  private FarmerController farmerController;

  public FarmerCard() {
    // Load the FXML.
    FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/ca/mcgill/ecse/cheecsemanager/view/" +
                               "components/Farmer/FarmerCard.fxml"));
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
  public void setFarmer(TOFarmer farmer) {
    this.farmerData = farmer;
    if (farmer != null) {
      nameLabel.setText(farmer.getName());
      emailLabel.setText(farmer.getEmail());
      addressLabel.setText(farmer.getAddress());

      cheeseLabel.setText(String.valueOf(farmer.numberOfCheeseWheelIDs()));
    }
  }

  public TOFarmer getFarmer() { return farmerData; }

  // Refresh the card's displayed data from the current farmer object
  public void refresh() {
    if (farmerData != null) {
      nameLabel.setText("Name: " + farmerData.getName());
      emailLabel.setText("Email: " + farmerData.getEmail());
      addressLabel.setText("Address: " + farmerData.getAddress());

      cheeseLabel.setText(String.valueOf(farmerData.numberOfCheeseWheelIDs()));
    }
  }

  private void handleView() {
    if (farmerData != null) {
      ca.mcgill.ecse.cheecsemanager.fxml.controllers.PageNavigator.getInstance()
          .navigateTo("/ca/mcgill/ecse/cheecsemanager/view/page/farmers/" +
                      "viewFarmerPage.fxml",
                      farmerData);
    }
  }

  private void handleDelete() {
    if (farmerController != null) {
      farmerController.deleteFarmerPopup(this);
    }
  }
}
