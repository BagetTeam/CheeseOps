package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.*;
import java.sql.Date;

/**
 * @author Eun-jun Chang
 */
public class CheECSEManagerFeatureSet4Controller {
  /**
   * Buys multiple cheese wheels from a farmer and register in the system.
   * @author Eun-jun Chang
   * @param emailFarmer Email of the farmer selling the cheese.
   * @param purchaseDate Date of the purchase.
   * @param nrCheeseWheels Number of cheese wheels to buy.
   * @param monthsAged monthsAged maturation period.
   * @return error message, empty string if successful.
   */
  public static String buyCheeseWheels(String emailFarmer, Date purchaseDate,
                                       Integer nrCheeseWheels,
                                       String monthsAged) {
    var manager = CheECSEManagerApplication.getCheecseManager();

    if (emailFarmer == null || emailFarmer.trim().isEmpty()) {
      return "Farmer email cannot be empty.";
    }
    if (purchaseDate == null) {
      return "Purchase date cannot be empty.";
    }
    if (nrCheeseWheels == null || nrCheeseWheels <= 0) {
      return "nrCheeseWheels must be greater than zero.";
    }
    if (monthsAged == null || monthsAged.trim().isEmpty()) {
      return "The monthsAged must be Six, Twelve, TwentyFour, or ThirtySix.";
    }

    // Find the farmer by email
    Farmer farmer = null;
    for (Farmer f : manager.getFarmers()) {
      if (f.getEmail().equalsIgnoreCase(emailFarmer.trim())) {
        farmer = f;
        break;
      }
    }
    if (farmer == null) {
      return "The farmer with email " + emailFarmer + " does not exist.";
    }

    try {
      CheeseWheel.MaturationPeriod period;
      try {
        period = CheeseWheel.MaturationPeriod.valueOf(monthsAged);
      } catch (IllegalArgumentException e) {
        return "The monthsAged must be Six, Twelve, TwentyFour, or ThirtySix.";
      }
      var purchase =
          new Purchase(purchaseDate, manager, farmer); // Create purchase

      for (int i = 0; i < nrCheeseWheels; i++) { // Add cheese wheels to
                                                 // purchase
        purchase.addCheeseWheel(period, false, manager);
      }
    } catch (Exception e) {
      return "Error while purchasing: " + e.getMessage();
    }
    return "";
  }

  /**
   * Assigns cheese wheel to a specific shelf and position.
   * @author Eun-jun Chang
   * @param cheeseWheelID ID of the cheese wheel.
   * @param shelfID ID of the shelf.
   * @param columnNr Column number of the shelf.
   * @param rowNr Row number of the shelf.
   * @return error message, empty string if successful.
   */
  public static String assignCheeseWheelToShelf(Integer cheeseWheelID,
                                                String shelfID,
                                                Integer columnNr,
                                                Integer rowNr) {
    var manager = CheECSEManagerApplication.getCheecseManager();

    if (cheeseWheelID == null) {
      return "Cheese wheel ID cannot be null";
    }
    if (shelfID == null || shelfID.trim().isEmpty()) {
      return "Shelf ID cannot be empty.";
    }
    if (columnNr == null || rowNr == null) {
      return "Column and row numbers cannot be null.";
    }

    // Find cheese wheel
    CheeseWheel cheese = null;
    for (CheeseWheel c : manager.getCheeseWheels()) {
      if (c.getId() == cheeseWheelID) {
        cheese = c;
        break;
      }
    }
    if (cheese == null) {
      return "The cheese wheel with id " + cheeseWheelID + " does not exist.";
    }
    if (cheese.isIsSpoiled()) {
      return "Cannot place a spoiled cheese wheel on a shelf.";
    }

    // Find shelf
    Shelf shelf = null;
    for (Shelf s : manager.getShelves()) {
      if (s.getId().equals(shelfID)) {
        shelf = s;
        break;
      }
    }
    if (shelf == null) {
      return "Shelf with ID " + shelfID + " not found.";
    }

    // Finding shelf location
    ShelfLocation Location = null;
    for (ShelfLocation l : shelf.getLocations()) {
      if (l.getColumn() == columnNr && l.getRow() == rowNr) {
        Location = l;
        break;
      }
    }
    if (Location == null) {
      return "The shelf location does not exist.";
    }
    if (Location.hasCheeseWheel()) {
      return "The shelf location is already occupied.";
    }

    // Check if cheese is already assigned
    if (cheese.getLocation() != null) {
      ShelfLocation oldLocation = cheese.getLocation();
      oldLocation.setCheeseWheel(null);
      cheese.setLocation(null);
    }

    Location.setCheeseWheel(cheese);
    cheese.setLocation(Location);

    return "";
  }

  /**
   * Removes a cheese wheel from its assigned shelf location.
   * @author Eun-jun Chang
   * @param cheeseWheelID ID of the cheese wheel to remove.
   * @return error message, empty string if successful.
   */
  public static String removeCheeseWheelFromShelf(Integer cheeseWheelID) {
    var manager = CheECSEManagerApplication.getCheecseManager();

    if (cheeseWheelID == null) {
      return "Cheese wheel ID cannot be null.";
    }

    // Find cheese wheel
    CheeseWheel cheese = null;
    for (CheeseWheel c : manager.getCheeseWheels()) {
      if (c.getId() == cheeseWheelID) {
        cheese = c;
        break;
      }
    }
    if (cheese == null) {
      return "The cheese wheel with id " + cheeseWheelID + " does not exist.";
    }

    // Get location
    ShelfLocation location = cheese.getLocation();
    if (location == null) {
      return "The cheese wheel is not on any shelf.";
    }

    // Remove cheese link from shelf location
    boolean removed = location.setCheeseWheel(null);
    if (!removed) {
      return "Failed to remove cheese wheel from the shelf.";
    }

    return "";
  }
}
