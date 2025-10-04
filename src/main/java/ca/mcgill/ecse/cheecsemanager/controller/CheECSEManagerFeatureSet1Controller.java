package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
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
    } else if (!password.contains("!") && !password.contains("#") &&
               !password.contains("$")) {
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
    var app = CheECSEManagerApplication.getCheecseManager();

    for (var s : app.getShelves()) {
      if (s.getId().equals(id)) {
        return new TOShelf(s.getId());
      }
    }

    throw new IllegalArgumentException("No shelf with id " + id);
  }

  /**
   * Get all the shelves in the system
   * @return list of {@link TOShelf}
   * */
  public static List<TOShelf> getShelves() {
    var app = CheECSEManagerApplication.getCheecseManager();

    return app.getShelves()
        .stream()
        .map(shelf -> new TOShelf(shelf.getId()))
        .toList();
  }
}
