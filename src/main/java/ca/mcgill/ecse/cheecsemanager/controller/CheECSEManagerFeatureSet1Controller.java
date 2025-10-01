package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import java.util.List;

public class CheECSEManagerFeatureSet1Controller {

  public static String updateFacilityManager(String password) {
    var app = CheECSEManagerApplication.getCheecseManager();
    var manager = app.getManager();
    String oldPassword = manager.getPassword();
    manager.setPassword(password);
    return oldPassword;
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
