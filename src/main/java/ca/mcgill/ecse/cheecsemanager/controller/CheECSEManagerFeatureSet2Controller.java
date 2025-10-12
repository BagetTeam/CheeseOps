package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;

import java.util.Optional;

/**
 * @author Ayush Patel
 * */
public class CheECSEManagerFeatureSet2Controller {
  /**
   * Add a new shelf
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
    if(nrRows > 10) {
      throw new IllegalArgumentException("Number of rows must be less than 10");
    }

    for(int i=1; i<=nrColumns; i++) {
      for(int j=1; j<=nrRows; j++) {
        ShelfLocation newShelfLocation = new ShelfLocation(i, j, aShelf);
        aShelf.addLocation(newShelfLocation);
      }
    }
  }

  /**
   * Delete some shelf and all the locations linked to it
   * @param id id of shelf
   * **/
  public static String deleteShelf(String id){
    var app = CheECSEManagerApplication.getCheecseManager();
    Optional<Shelf> shelfToDelete = app.getShelves().stream().filter(s -> id.equals(s.getId())).findFirst();
    if(shelfToDelete.isPresent()) {
        if(checkIsEmpty(shelfToDelete.get())){
          shelfToDelete.get().delete(); // this method takes care of deleting all locations as well
          return id;
      } else {
          throw new IllegalArgumentException("Shelf with id " + id + " exists but is not empty. Could not delete shelf.");
        }
    } else {
      throw new IllegalArgumentException("Shelf with id " + id + " not found.");
    }
  }
  /**
   * when deleting a shelf, need to check if the shelf is empty first
   * @param aShelf shelf from which we want to remove the locations
   * **/
  private static boolean checkIsEmpty(Shelf aShelf){
    for(ShelfLocation shelfLocation : aShelf.getLocations()) {
      Optional<CheeseWheel> cheeseWheel = Optional.ofNullable(shelfLocation.getCheeseWheel());
      if (cheeseWheel.isPresent()) {
        return false;
      }
    }
    return true;
  }
}
