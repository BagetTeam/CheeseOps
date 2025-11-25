package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet4Controller;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

  @FXML
  private void initialize() {
    var manager = CheECSEManagerApplication.getCheecseManager();

    farmerDropdown.getItems().clear();
    manager.getFarmers().forEach(f ->
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
    errorLabel.setText("Success!");
  }

  @FXML
  private void goBack() {
    // TODO: Ewen
  }
}
