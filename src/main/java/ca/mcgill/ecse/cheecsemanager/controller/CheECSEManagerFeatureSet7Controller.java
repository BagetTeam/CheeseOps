package ca.mcgill.ecse.cheecsemanager.controller;

import java.util.List;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;

public class CheECSEManagerFeatureSet7Controller {

  public static String updateFarmer(String email, String newPassword, String newName,String newAddress) {
    if (newPassword.isEmpty()) {
      return "Password must not be empty.";
    }
    if (newAddress.isEmpty()) {
      return "Address must not be empty.";
    }
    
    var app = CheECSEManagerApplication.getCheecseManager();
    List<Farmer> farmers = app.getFarmers();
    for (Farmer farmer : farmers) {
      if (email.equals(farmer.getEmail())) {
        farmer.setPassword(newPassword);
        farmer.setName(newName);
        farmer.setAddress(newAddress);
        return "";
      }
    }
    return "The farmer with email " + email + " does not exist.";
  }

  public static String deleteFarmer(String email) {
    var app = CheECSEManagerApplication.getCheecseManager();
    List<Farmer> farmers = app.getFarmers();
    for (Farmer farmer : farmers) {
      if (email.equals(farmer.getEmail())) {
        List<Purchase> purchases = farmer.getPurchases();

        for (Purchase purchase : purchases) {
          if (purchase.hasCheeseWheels()) {
            return "Cannot delete farmer who has supplied cheese.";
          }
        }

        farmer.delete();
        return "";
      }
    }
    return "The farmer with email " + email +" does not exist.";
  }

  public static TOFarmer getFarmer(String email) {
    var app = CheECSEManagerApplication.getCheecseManager();
    List<Farmer> farmers = app.getFarmers();
    for (Farmer farmer : farmers) {
      if (email.equals(farmer.getEmail())) {
        return new TOFarmer(farmer.getEmail(), farmer.getPassword(), farmer.getName(), farmer.getAddress());
      }
    }
    return null;
  }
  
  // returns all farmers
  public static List<TOFarmer> getFarmers() {
    var app = CheECSEManagerApplication.getCheecseManager();
    List<Farmer> farmers = app.getFarmers();
    List<TOFarmer> toFarmers = farmers.stream().map(farmer -> new TOFarmer(farmer.getEmail(), farmer.getPassword(), farmer.getName(), farmer.getAddress())).toList();
    
    return toFarmers;
  }
}
