package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet4Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.FarmerDataProvider;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller for the buy cheese popup UI
 * Handles loading farmer data, validating user input, buying cheese wheels from a selected farmer
 * @author Eun-jun Chang
 */
public class BuyCheesePopupController {

  @FXML private ComboBox<String> farmerDropdown;
  @FXML private TextField        wheelCountField;
  @FXML private ComboBox<String> maturationDropdown;
  @FXML private CheckBox         autoAssignCheckbox;
  @FXML private Label            errorLabel;

  private TOFarmer farmer;
  private StackPane popupOverlay;
  private ViewFarmerController farmerViewController;

  public void setPopupOverlay(StackPane overlay) {
      this.popupOverlay = overlay;
  }

  public void setViewFarmerController(ViewFarmerController controller) {
      this.farmerViewController = controller;
  }

  public void setFarmer(TOFarmer farmer) {
    this.farmer = farmer;
    
    if (farmer != null && farmerDropdown.getItems().contains(farmer.getEmail())) {
      farmerDropdown.setValue(farmer.getEmail());
    }
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

    // Get current max cheese wheel ID to identify newly created ones
    List<TOCheeseWheel> existingWheels = CheECSEManagerFeatureSet3Controller.getCheeseWheels();
    int maxExistingId = existingWheels.stream()
        .mapToInt(TOCheeseWheel::getId)
        .max()
        .orElse(0);
    
    Date today = new Date(System.currentTimeMillis());
    String result =
        CheECSEManagerFeatureSet4Controller.buyCheeseWheels(email, today, count, months);
    if (!result.isEmpty()) {
      errorLabel.setText(result);
      return;
    }
    
    // Auto-assign cheese wheels if checkbox is selected
    int remaining = 0;
    if (autoAssignCheckbox.isSelected()) {
      // Get newly created cheese wheels
      List<TOCheeseWheel> allWheels = CheECSEManagerFeatureSet3Controller.getCheeseWheels();
      List<TOCheeseWheel> newlyBoughtWheels = new ArrayList<>();
      for (TOCheeseWheel wheel : allWheels) {
        if (wheel.getId() > maxExistingId) {
          newlyBoughtWheels.add(wheel);
        }
      }
      
      // Auto-assign only the newly bought cheese wheels
      remaining = autoAssignCheeseWheels(newlyBoughtWheels);
    }
    
    FarmerDataProvider.getInstance().refresh();
    if (farmerViewController != null) {
        farmerViewController.reloadFarmerDetails();
    }

    closePopup();
    farmerViewController.getViewFarmerRoot().fireEvent(new ToastEvent("Success!", ToastEvent.ToastType.SUCCESS));
    if (autoAssignCheckbox.isSelected() && remaining > 0) {
      farmerViewController.getViewFarmerRoot().fireEvent(new ToastEvent(remaining + " could not be auto assigned.", ToastEvent.ToastType.WARNING));
    } 
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

  /**
   * Auto assigns the given cheese wheels to empty shelf locations.
   * @param cheeseWheelsToAssign List of cheese wheels to assign to shelves
   * @return the number of cheese wheels remaining to be assigned.
   * @author Ewen Gueguen
   */
  private int autoAssignCheeseWheels(List<TOCheeseWheel> cheeseWheelsToAssign) {
    int assignedCount = 0;
    List<TOShelf> shelves = CheECSEManagerFeatureSet1Controller.getShelves();
    
    outerLoop:
    for (TOShelf shelf : shelves) {
      int rows = shelf.getMaxRows();
      int columns = shelf.getMaxColumns();
      
      // Build a set of occupied positions to skip them (much faster)
      Set<String> occupiedPositions = new HashSet<>();
      for (int i = 0; i < shelf.numberOfCheeseWheelIDs(); i++) {
        occupiedPositions.add(shelf.getColumnNr(i) + "," + shelf.getRowNr(i));
      }

      // Only check empty positions
      for (int col = 1; col <= columns; col++) {
        for (int row = 1; row <= rows; row++) {
          // Skip if position is already occupied
          if (occupiedPositions.contains(col + "," + row)) {
            continue;
          }
          
          // Skip already-assigned cheese wheels in our list
          while (assignedCount < cheeseWheelsToAssign.size() && 
                 (cheeseWheelsToAssign.get(assignedCount).getShelfID() != null || 
                  cheeseWheelsToAssign.get(assignedCount).getColumn() != -1 || 
                  cheeseWheelsToAssign.get(assignedCount).getRow() != -1)) {
            assignedCount++;
          }

          if (assignedCount >= cheeseWheelsToAssign.size()) {
            break outerLoop; // All cheese wheels assigned
          }
          
          String result = CheECSEManagerFeatureSet4Controller.assignCheeseWheelToShelf(
              cheeseWheelsToAssign.get(assignedCount).getId(), 
              shelf.getShelfID(), 
              col, 
              row
          );
          
          if (result.isEmpty()) {
            assignedCount++;
          }
        }
      }
    }
    
    return cheeseWheelsToAssign.size() - assignedCount;
  }
}
