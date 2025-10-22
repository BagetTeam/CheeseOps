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
   * @author Ayush Patel
   * @param id new shelf
   * @param nrColumns nr Columns in the shelf
   * @param nrRows nr rows in the shelf
   * @return the error message. Null if there is no error.
   * */
  public static String addShelf(String id, Integer nrColumns, Integer nrRows) {
    // check the validity of the id
    Optional<String> errorMessageWithId = Optional.ofNullable(checkErrorMessageWithId(id));
    if (errorMessageWithId.isPresent()) {
      return errorMessageWithId.get();
    }

    Optional<String> colsRowsErrorMessage =
        Optional.ofNullable(checkErrorMessageWithRowsCols(nrRows, nrColumns));
    if (colsRowsErrorMessage.isPresent()) {
      return colsRowsErrorMessage.get();
    }

    Optional<Shelf> existingShelf = Optional.ofNullable(Shelf.getWithId(id));

    if (existingShelf.isPresent()) {
      return String.format("The shelf %s already exists.", id);
    }

    var cheecseManager = CheECSEManagerApplication.getCheecseManager();

    Shelf newShelf = new Shelf(id, cheecseManager);
    addShelfLocations(newShelf, nrColumns, nrRows);
    return null;
  }

  /**
   * helper function to check the validity of the id
   * @author Ayush Patel
   * @param id the id to check
   * @return the error message. Null if there is no error.
   * */
  private static String checkErrorMessageWithId(String id) {
    if (id.length() != 3) {
      return "The id must be three characters long.";
    }
    if (!Character.isLetter(id.charAt(0))) {
      return "The first character must be a letter.";
    }
    if (!Character.isDigit(id.charAt(1)) || !Character.isDigit(id.charAt(2))) {
      return "The second and third characters must be digits.";
    }
    return null;
  }

  /**
   * helper function to check the validity of the number of rows and columns
   * @author Ayush Patel
   * @param nrRows the number of rows
   * @param nrColumns the number of columns
   * @return the error message. Null if there is no error.
   * */
  private static String checkErrorMessageWithRowsCols(Integer nrRows, Integer nrColumns) {
    if (nrRows > 10) {
      return "Number of rows must be at the most ten.";
    }

    if (nrRows <= 0) {
      return "Number of rows must be greater than zero.";
    }

    if (nrColumns <= 0) {
      return "Number of columns must be greater than zero.";
    }
    return null;
  }

  /**
   * Add shelf locations to a new shelf
   * @author Ayush Patel
   * @param aShelf new shelf
   * @param nrColumns nr Columns in the shelf
   * @param nrRows nr rows in the shelf
   * */
  private static void addShelfLocations(Shelf aShelf, Integer nrColumns, Integer nrRows) {
    for (int i = 1; i <= nrColumns; i++) {
      for (int j = 1; j <= nrRows; j++) {
        new ShelfLocation(i, j, aShelf);
      }
    }
  }

  /**
   * Delete some shelf and all the locations linked to it
   * @author Ayush Patel
   * @param id id of shelf
   * @return the error message. Null if there is no error.
   * */
  public static String deleteShelf(String id) {
    var app = CheECSEManagerApplication.getCheecseManager();
    Optional<Shelf> shelfToDelete =
        app.getShelves().stream().filter(s -> id.equals(s.getId())).findFirst();
    if (shelfToDelete.isPresent()) {
      if (checkIsEmpty(shelfToDelete.get())) {
        shelfToDelete.get().delete(); // this method takes care of deleting all
                                      // locations as well
        return null;
      } else {
        return "Cannot delete a shelf that contains cheese wheels.";
      }
    } else {
      return String.format("The shelf %s does not exist.", id);
    }
  }
  /**
   * when deleting a shelf, need to check if the shelf is empty first
   * @author Ayush Patel
   * @param aShelf shelf from which we want to remove the locations
   * @return true if the shelf is empty, false otherwise
   * */
  private static boolean checkIsEmpty(Shelf aShelf) {
    for (ShelfLocation shelfLocation : aShelf.getLocations()) {
      Optional<CheeseWheel> cheeseWheel = Optional.ofNullable(shelfLocation.getCheeseWheel());
      if (cheeseWheel.isPresent()) {
        return false;
      }
    }
    return true;
  }
}
