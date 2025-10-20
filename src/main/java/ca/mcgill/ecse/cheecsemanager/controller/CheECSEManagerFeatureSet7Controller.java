package ca.mcgill.ecse.cheecsemanager.controller;

import java.util.List;
import java.util.stream.Stream;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;

public class CheECSEManagerFeatureSet7Controller {

  public static String updateFarmer(String email, String newPassword, String newName,
      String newAddress) {

        

    throw new UnsupportedOperationException("Implement me!");
  }

  public static String deleteFarmer(String email) {
    throw new UnsupportedOperationException("Implement me!");
  }

  public static TOFarmer getFarmer(String email) {
    var app = CheECSEManagerApplication.getCheecseManager();
    List<Farmer> farmers = app.getFarmers();
    for (Farmer farmer : farmers) {
      if (email.equals(farmer.getEmail())) {
        return new TOFarmer(farmer.getEmail(), farmer.getPassword(), farmer.getName(), farmer.getAddress());
      }
    }

    throw new IllegalArgumentException("No farmer with email " + email + " found");
  }
  
  // returns all farmers
  public static List<TOFarmer> getFarmers() {
    var app = CheECSEManagerApplication.getCheecseManager();
    List<Farmer> farmers = app.getFarmers();
    List<TOFarmer> toFarmers = farmers.stream().map(farmer -> new TOFarmer(farmer.getEmail(), farmer.getPassword(), farmer.getName(), farmer.getAddress())).toList();
    
    return toFarmers;
  }
}
