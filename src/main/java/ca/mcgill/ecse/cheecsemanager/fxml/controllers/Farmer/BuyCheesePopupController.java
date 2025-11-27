package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet4Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.fxml.store.FarmerDataProvider;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import java.sql.Date;

/**
 * Controller for the buy cheese popup UI
 * Handles loading farmer data, validating user input, buying cheese wheels from a selected farmer
 * @author Eun-jun Chang
 */
public class BuyCheesePopupController {

  @FXML private ComboBox<String> farmerDropdown;
  @FXML private TextField        wheelCountField;
  @FXML private ComboBox<String> maturationDropdown;
  @FXML private Label            errorLabel;

  
  private StackPane popupOverlay;
  private ViewFarmerController farmerViewController;

  public void setPopupOverlay(StackPane overlay) {
      this.popupOverlay = overlay;
  }

  public void setViewFarmerController(ViewFarmerController controller) {
      this.farmerViewController = controller;
  }

  @FXML
  private void initialize() {

    farmerDropdown.getItems().clear();
    CheECSEManagerFeatureSet7Controller.getFarmers().forEach(f ->
        farmerDropdown.getItems().add(f.getEmail())
    );
    
    maturationDropdown.getItems().setAll(
        "Six", "Twelve", "TwentyFour", "ThirtySix"
    );
  }

  @FXML
  private void handleBuy() {
    errorLabel.setText("");

    String email   = farmerDropdown.getValue();
    String countStr = wheelCountField.getText();
    String months  = maturationDropdown.getValue();
    
    if (email == null || email.isBlank()) {
      errorLabel.setText("Select a farmer.");
      return;
    }
    if (months == null || months.isBlank()) {
      errorLabel.setText("Select maturation period.");
      return;
    }

    int count;

    try {
      count = Integer.parseInt(countStr);
    } catch (NumberFormatException e) {
      errorLabel.setText("Wheel count must be a number.");
      return;
    }

    Date today = new Date(System.currentTimeMillis());
    String result =
        CheECSEManagerFeatureSet4Controller.buyCheeseWheels(email, today, count, months);

    if (!result.isEmpty()) {
      errorLabel.setText(result);
      return;
    }
    FarmerDataProvider.getInstance().refresh();
    if (farmerViewController != null) {
        farmerViewController.reloadFarmerDetails();
    }
    errorLabel.setText("Success!");
  }

  @FXML
  private void goBack() {
    closePopup();
  }

  private void closePopup() {
        if (farmerViewController != null && popupOverlay != null) {
            farmerViewController.removePopup(popupOverlay);
        }
    }
}
