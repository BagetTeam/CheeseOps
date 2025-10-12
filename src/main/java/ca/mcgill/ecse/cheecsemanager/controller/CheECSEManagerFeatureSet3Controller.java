package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;

import java.util.List;

public class CheECSEManagerFeatureSet3Controller {

  public static String registerFarmer(String email, String password, String name, String address) {

    var app = CheECSEManagerApplication.getCheecseManager();
    for (var farmer : app.getFarmers()) {
      if (email.equals(farmer.getEmail())) {
        return "The farmer email already exists.";
      }
    }

    if (!email.contains("@")) return "Email must contain @ symbol.";
    int atLocation = email.indexOf("@");
    if (atLocation-1 <=0 ) return "Email must have characters before @.";
    else if (!email.substring(atLocation).contains(".")) return "Email must contain a dot after @.";
    else if (email.substring(atLocation).indexOf(".")+1 >= email.substring(atLocation).length()) return "Email must have characters after dot.";
    else if (email.contains(" ")) return "Email must not contain spaces.";
    else if (email.equals("manager@cheecse.fr")) return "Email cannot be manager@cheecse.fr.";
    else if (password==null || password.isBlank()) return "Password must not be empty.";
    else if (address==null || address.isBlank()) return "Address must not be empty.";

    Farmer farmer = new Farmer(email, password, address, app.getManager().getCheECSEManager());
    farmer.setName(name);
    app.addFarmer(farmer);

    return "";
  }

  public static String updateCheeseWheel(Integer cheeseWheelID, String newMonthsAged,
      Boolean newIsSpoiled) {
    var app = CheECSEManagerApplication.getCheecseManager();
    for (var cheeseWheel : app.getCheeseWheels()) {
      //checks list for matching cheese ID to ensure it even exists
      if (cheeseWheelID.equals(cheeseWheel.getId())) {
        // Parse and validate monthsAged
        CheeseWheel.MaturationPeriod updatedPeriod;
        try {
          updatedPeriod = CheeseWheel.MaturationPeriod.valueOf(newMonthsAged);
        } catch (IllegalArgumentException e) {
          return "The monthsAged must be Six, Twelve, TwentyFour, or ThirtySix.";
        }

        if (updatedPeriod.ordinal() < cheeseWheel.getMonthsAged().ordinal()) {
          return "Cannot decrease the monthsAged of a cheese wheel.";
        }

        cheeseWheel.setMonthsAged(updatedPeriod);
        cheeseWheel.setIsSpoiled(newIsSpoiled);

        if (newIsSpoiled) {
          if (cheeseWheel.getLocation() != null) {
            //deletes the cheese wheel from the shelf location.
            cheeseWheel.getLocation().setCheeseWheel(null);
          }
          if (cheeseWheel.getOrder() != null) {
            //deletes the cheese wheel from the order.
            cheeseWheel.getOrder().removeCheeseWheel(cheeseWheel);
          }
        }

        return "";
      }
    }
    return "The cheese wheel with id " + cheeseWheelID + " does not exist.";
  }

  public static TOCheeseWheel getCheeseWheel(Integer cheeseWheelID) {
    var app = CheECSEManagerApplication.getCheecseManager();
    for (var cheeseWheel : app.getCheeseWheels()) {

      if (cheeseWheelID.equals(cheeseWheel.getId())) {
        /* use this if location can be null to avoid exceptions
          var location = cheeseWheel.getLocation();
          String shelfId = location != null ? location.getShelf().getId() : null;

        this may cause null pointer since collumn and row are int, which cannot be set to null in Java...
          Integer column = location != null ? location.getColumn() : null;
          Integer row = location != null ? location.getRow() : null;
        return new TOCheeseWheel(cheeseWheelID, cheeseWheel.getMonthsAged().name(), cheeseWheel.isIsSpoiled(), cheeseWheel.getPurchase().getTransactionDate(), shelfId, column, row, cheeseWheel.hasOrder());
        */
        return new TOCheeseWheel(cheeseWheel.getId(), cheeseWheel.getMonthsAged().name(), cheeseWheel.isIsSpoiled(), cheeseWheel.getPurchase().getTransactionDate(), cheeseWheel.getLocation().getShelf().getId(), cheeseWheel.getLocation().getColumn(), cheeseWheel.getLocation().getRow(), cheeseWheel.hasOrder());
      }
    }
    return null;
  }

  // returns all cheese wheels
  public static List<TOCheeseWheel> getCheeseWheels() {
    var app = CheECSEManagerApplication.getCheecseManager();

    return app.getCheeseWheels()
            .stream()
            .map(cheeseWheel -> new TOCheeseWheel(cheeseWheel.getId(), cheeseWheel.getMonthsAged().name(), cheeseWheel.isIsSpoiled(), cheeseWheel.getPurchase().getTransactionDate(), cheeseWheel.getLocation().getShelf().getId(), cheeseWheel.getLocation().getColumn(), cheeseWheel.getLocation().getRow(), cheeseWheel.hasOrder()))
            .toList();
  }
}
