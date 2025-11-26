package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.persistence.CheECSEManagerPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for managing FeatureSet7 operations including updating
 * farmer, deleting farmer, displaying farmer, and displaying all farmers.
 *
 * @author Ewen Gueguen
 * */

public class CheECSEManagerFeatureSet7Controller {
  /**
   * Updates the details of a farmer identified by email.
   *
   * @param email       The email of the farmer to update.
   * @param newPassword The new password to set.
   * @param newName     The new name to set.
   * @param newAddress  The new address to set.
   * @return An empty string on success, or an error message otherwise.
   *
   * @author Ewen Gueguen
   */
  public static String updateFarmer(
      String email, String newPassword, String newName, String newAddress) {
    // make sure the new password and address is not empty
    if (isNullOrEmpty(newPassword)) {
      return "Password must not be empty.";
    }
    if (isNullOrEmpty(newAddress)) {
      return "Address must not be empty.";
    }

    // make sure farmer with email exists
    Optional<Farmer> farmerOpt = findFarmerWithEmail(email);
    if (farmerOpt.isEmpty()) {
      return "The farmer with email " + email + " does not exist.";
    }

    // update farmer
    Farmer farmer = farmerOpt.get();
    farmer.setPassword(newPassword);
    farmer.setName(newName);
    farmer.setAddress(newAddress);

    try {
      CheECSEManagerPersistence.save();
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return "";
  }

  /**
   * Delete a farmer by email.
   *
   * @param email       The email of the farmer to delete.
   * @return An empty string on success, or an error message otherwise.
   *
   * @author Ewen Gueguen
   */
  public static String deleteFarmer(String email) {
    // make sure farmer with email exists
    Optional<Farmer> farmerOpt = findFarmerWithEmail(email);
    if (farmerOpt.isEmpty()) {
      return "The farmer with email " + email + " does not exist.";
    }

    Farmer farmer = farmerOpt.get();

    // make sure farmer has no purchases
    List<Purchase> purchases = farmer.getPurchases();
    for (Purchase purchase : purchases) {
      if (purchase.hasCheeseWheels()) {
        return "Cannot delete farmer who has supplied cheese.";
      }
    }

    farmer.delete();

    try {
      CheECSEManagerPersistence.save();
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return "";
  }

  /**
   * Retrieve a farmer's data transfer object by email for display.
   *
   * @param email       The email of the farmer.
   * @return A TOFarmer instance if found, otherwise null.
   *
   * @author Ewen Gueguen
   */
  public static TOFarmer getFarmer(String email) {
    Optional<Farmer> optFarmer = findFarmerWithEmail(email);
    if (optFarmer.isPresent()) {
      Farmer farmer = optFarmer.get();
      TOFarmer toFarmer = new TOFarmer(farmer.getEmail(), farmer.getPassword(), farmer.getName(), farmer.getAddress());
      for (Purchase purchase : farmer.getPurchases()) {
        for (CheeseWheel cheeseWheel : purchase.getCheeseWheels()) {
          toFarmer.addCheeseWheelID(cheeseWheel.getId());
          toFarmer.addPurchaseDate(purchase.getTransactionDate());
          toFarmer.addMonthsAged(cheeseWheel.getMonthsAged().toString());
          toFarmer.addIsSpoiled(cheeseWheel.getIsSpoiled() ? "Yes" : "No");
        }
      }
      return toFarmer;
    }
    return null;
  }

  /**
   * Retrieve all farmers as data transfer object for display.
   *
   * @return A list of all farmers as TOFarmer instances.
   *
   * @author Ewen Gueguen
   */
  public static List<TOFarmer> getFarmers() {
    var app = CheECSEManagerApplication.getCheecseManager();
    List<Farmer> farmers = app.getFarmers();
    List<TOFarmer> toFarmers = new ArrayList<>();
    for (Farmer farmer : farmers) {
      TOFarmer toFarmer = getFarmer(farmer.getEmail());
      if (toFarmer != null) {
        toFarmers.add(toFarmer);
      }
    }
    return toFarmers;
  }

  /**
   * Helper method to find a farmer by email.
   *
   * @param email The email to search for.
   * @return An Optional containing the farmer if found, or empty otherwise.
   *
   * @author Ewen Gueguen
   */
  private static Optional<Farmer> findFarmerWithEmail(String email) {
    var app = CheECSEManagerApplication.getCheecseManager();
    List<Farmer> farmers = app.getFarmers();
    for (Farmer farmer : farmers) {
      if (email.equals(farmer.getEmail())) {
        return Optional.of(farmer);
      }
    }
    Optional<Farmer> empty = Optional.empty();
    return empty;
  }

  /**
   * Utility method to check if a string is null or empty.
   *
   * @param str The string to check.
   * @return true if the string is null or empty; false otherwise.
   *
   * @author Ewen Gueguen
   */
  private static boolean isNullOrEmpty(String str) {
    return str == null || str.isEmpty();
  }
}
