package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import java.util.List;

public class CheECSEManagerFeatureSet1Controller {

  public static String updateFacilityManagerPassword(String password) {
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

  public static TOShelf getShelf(String id) {
    var app = CheECSEManagerApplication.getCheecseManager();

    for (var s : app.getShelves()) {
      if (s.getId().equals(id)) {
        return new TOShelf(s.getId());
      }
    }

    throw new IllegalArgumentException("No shelf with id " + id);
  }

  // returns all shelves
  public static List<TOShelf> getShelves() {
    var app = CheECSEManagerApplication.getCheecseManager();

    return app.getShelves()
        .stream()
        .map(shelf -> new TOShelf(shelf.getId()))
        .toList();
  }
}
