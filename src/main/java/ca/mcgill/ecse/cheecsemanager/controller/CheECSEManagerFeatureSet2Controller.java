package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;

import java.util.List;
import java.util.Optional;

/**
 * @author Ayush Patel
 * */
public class CheECSEManagerFeatureSet2Controller {

  /**
   * Add shelf locations to a new shelf
   * @param id new shelf
   * @param nrColumns nr Columns in the shelf
   * @param nrRows nr rows in the shelf
   *
   * **/
  public static String addShelf(String id, Integer nrColumns, Integer nrRows) {
    var cheecseManager = CheECSEManagerApplication.getCheecseManager();
    Shelf newShelf = new Shelf(id, cheecseManager);
    addShelfLocations(newShelf, nrColumns, nrRows);
    return id;
  }
  /**
   * Add shelf locations to a new shelf
   * @param aShelf new shelf
   * @param nrColumns nr Columns in the shelf
   * @param nrRows nr rows in the shelf
   *
   * **/
  private static void addShelfLocations(Shelf aShelf, Integer nrColumns, Integer nrRows) {
    for(int i=0; i<nrColumns; i++) {
      for(int j=0; j<nrRows; j++) {
        ShelfLocation newShelfLocation = new ShelfLocation(i, j, aShelf);
        aShelf.addLocation(newShelfLocation);
      }
    }
  }

  public static String deleteShelf(String id){
    var app = CheECSEManagerApplication.getCheecseManager();
    if(app.getShelves().removeIf(shelf -> id.equals(shelf.getId()))) {
      return id;
    } else {
      throw new IllegalArgumentException(String.format("Could not find a shelf with this id: %s", id));
    }
  }

}
