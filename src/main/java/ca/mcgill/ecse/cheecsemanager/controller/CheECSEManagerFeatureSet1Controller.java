package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import java.util.List;

/**
 * @author Ming Li Liu
 * */
public class CheECSEManagerFeatureSet1Controller {
  /**
   * Updates the manager's password.
   * @param password the new password.
   * @return the error message. Empty string if there is no error.
   * */
  public static String updateFacilityManager(String password) {
    if (password.length() < 4) {
      return "Password must be at least 4 characters long.";
    } else if (!password.contains("!") && !password.contains("#") && !password.contains("$")) {
      return "Password must contain a special character from !, #, or $.";
    } else if (!password.chars().anyMatch((c) -> (c >= 'A' && c <= 'Z'))) {
      return "Password must contain an uppercase character.";
    } else if (!password.chars().anyMatch((c) -> (c >= 'a' && c <= 'z'))) {
      return "Password must contain a lowercase character.";
    }

    var app = CheECSEManagerApplication.getCheecseManager();
    var manager = app.getManager();
    manager.setPassword(password);

    return "";
  }

  /**
   * Get the shelf from shelf Id
   * @param id shelf Id
   * @return instance of {@link TOShelf} associated with the shelf Id
   * @throws if shelf Id doesn't exist
   * */
  public static TOShelf getShelf(String id) {
    var shelf = Shelf.getWithId(id);

    if (shelf == null) {
      return null;
    }

    return _toShelf(shelf);
  }

  /**
   * Get all the shelves in the system
   * @return list of {@link TOShelf}
   * */
  public static List<TOShelf> getShelves() {
    var app = CheECSEManagerApplication.getCheecseManager();

    return app.getShelves().stream().map(shelf -> _toShelf(shelf)).toList();
  }

  /**
   * helper function
   * */
  private static TOShelf _toShelf(Shelf shelf) {
    var toShelf = new TOShelf(shelf.getId(), 0, 0);

    var locations = shelf.getLocations();

    int maxCols = 0;
    int maxRows = 0;

    for (var location : locations) {
      maxCols = Math.max(location.getColumn(), maxCols);
      maxRows = Math.max(location.getRow(), maxRows);

      var cw = location.getCheeseWheel();
      if (cw != null) {
        toShelf.addCheeseWheelID(cw.getId());
        toShelf.addColumnNr(location.getColumn());
        toShelf.addRowNr(location.getRow());
        toShelf.addMonthsAged(cw.getMonthsAged().toString());
      }
    }

    toShelf.setMaxColumns(maxCols);
    toShelf.setMaxRows(maxRows);

    return toShelf;
  }
}
