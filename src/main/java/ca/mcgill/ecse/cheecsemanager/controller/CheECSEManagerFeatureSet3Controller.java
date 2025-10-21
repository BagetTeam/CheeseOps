package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import java.util.List;
/**
 * @author David Tang
 * */
public class CheECSEManagerFeatureSet3Controller {
  private static CheECSEManager app =
      CheECSEManagerApplication.getCheecseManager();

  public static String registerFarmer(String email, String password,
                                      String name, String address) {
    // checks for existing farmer
    if (Farmer.hasWithEmail(email)) {
      return "The farmer email already exists.";
    }

    // ensures that all the error messages shown in the feature file for
    // register farmer are thrown
    if (!email.contains("@"))
      return "Email must contain @ symbol.";
    int atLocation = email.indexOf("@");
    if (atLocation - 1 <= 0)
      return "Email must have characters before @.";
    else if (!email.substring(atLocation).contains("."))
      return "Email must contain a dot after @.";
    else if (email.substring(atLocation).indexOf(".") + 1 >=
             email.substring(atLocation).length())
      return "Email must have characters after dot.";
    else if (email.contains(" "))
      return "Email must not contain spaces.";
    else if (email.equals("manager@cheecse.fr"))
      return "Email cannot be manager@cheecse.fr.";
    else if (password == null || password.isBlank())
      return "Password must not be empty.";
    else if (address == null || address.isBlank())
      return "Address must not be empty.";

    // if no issues with registration, create a new farmer and add it to the
    // farmer List in the application
    Farmer farmer = new Farmer(email, password, address, app);
    farmer.setName(name);
    app.addFarmer(farmer);

    // return no error message
    return "";
  }

  public static String updateCheeseWheel(Integer cheeseWheelID,
                                         String newMonthsAged,
                                         Boolean newIsSpoiled) {
    var cheeseWheel = _getCheeseWheelFromId(cheeseWheelID.intValue());

    if (cheeseWheel == null) {
      return "The cheese wheel with id " + cheeseWheelID + " does not exist.";
    }

    CheeseWheel.MaturationPeriod updatedPeriod;

    try {
      updatedPeriod = CheeseWheel.MaturationPeriod.valueOf(newMonthsAged);
    } catch (Exception e) {
      return "The monthsAged must be Six, Twelve, TwentyFour, or "
          + "ThirtySix.";
    }

    if (updatedPeriod.ordinal() < cheeseWheel.getMonthsAged().ordinal()) {
      return "Cannot decrease the monthsAged of a cheese wheel.";
    }

    cheeseWheel.setMonthsAged(updatedPeriod);
    cheeseWheel.setIsSpoiled(newIsSpoiled);

    if (newIsSpoiled) {
      cheeseWheel.setLocation(null);
      cheeseWheel.setOrder(null);
    }

    var order = cheeseWheel.getOrder();

    if (order != null && !order.getMonthsAged().equals(updatedPeriod)) {
      cheeseWheel.setOrder(null);
    }

    return "";
  }

  private static CheeseWheel _getCheeseWheelFromId(int cheeseWheelId) {
    for (var cw : app.getCheeseWheels()) {
      if (cw.getId() == cheeseWheelId) {
        return cw;
      }
    }

    return null;
  }

  public static TOCheeseWheel getCheeseWheel(Integer cheeseWheelID) {
    var app = CheECSEManagerApplication.getCheecseManager();
    for (var cheeseWheel : app.getCheeseWheels()) {
      // if the ID can be found in the List of cheeseWheels, then create a
      // TOCheeseWheel instance with its constructor.
      if (cheeseWheelID.equals(cheeseWheel.getId())) {
        /* use this if location can be null to avoid exceptions
          var location = cheeseWheel.getLocation();
          String shelfId = location != null ? location.getShelf().getId() :
        null;

        this may cause null pointer since collumn and row are int, which cannot
        be set to null in Java... Integer column = location != null ?
        location.getColumn() : null; Integer row = location != null ?
        location.getRow() : null; return new TOCheeseWheel(cheeseWheelID,
        cheeseWheel.getMonthsAged().name(), cheeseWheel.isIsSpoiled(),
        cheeseWheel.getPurchase().getTransactionDate(), shelfId, column, row,
        cheeseWheel.hasOrder());
        */
        var location = cheeseWheel.getLocation();

        return new TOCheeseWheel(
            cheeseWheel.getId(), cheeseWheel.getMonthsAged().name(),
            cheeseWheel.isIsSpoiled(),
            cheeseWheel.getPurchase().getTransactionDate(),
            location == null ? null : location.getShelf().getId(),
            location == null ? -1 : location.getColumn(),
            location == null ? -1 : location.getRow(), cheeseWheel.hasOrder());
      }
    }
    return null;
  }

  // returns all cheese wheels
  public static List<TOCheeseWheel> getCheeseWheels() {
    var app = CheECSEManagerApplication.getCheecseManager();

    // returns a List of TOCheeseWheels created from the CheeseWheel List stored
    // in the application.
    return app.getCheeseWheels()
        .stream()
        .map(cheeseWheel -> {
          var location = cheeseWheel.getLocation();
          return new TOCheeseWheel(
              cheeseWheel.getId(), cheeseWheel.getMonthsAged().name(),
              cheeseWheel.isIsSpoiled(),
              cheeseWheel.getPurchase().getTransactionDate(),
              location == null ? null : location.getShelf().getId(),
              location == null ? -1 : location.getColumn(),
              location == null ? -1 : location.getRow(),
              cheeseWheel.hasOrder());
        })
        .toList();
  }
}
