package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import java.util.List;

public class CheECSEManagerFeatureSet1Controller {

  public static String updateFacilityManagerPassword(String password) {

    if (password.length() < 4) {
      return "password must be at least four characters long";
    } else if (!password.matches("[#!$]")) {
      return "password must contain a special character out of !#$";
    } else if (!password.matches("[A-Z]")) {
      return "password must contain an upper case character";
    } else if (!password.matches("[a-z]")) {
      return "password must contain a lower case character";
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
