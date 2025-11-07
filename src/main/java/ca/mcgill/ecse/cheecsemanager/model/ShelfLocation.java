/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.model;

import java.util.List;

// line 52 "../../../../../CheECSEManagerPersistence.ump"
// line 47 "../../../../../CheECSEManager.ump"
public class ShelfLocation {
  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  // ShelfLocation Attributes
  private int column;
  private int row;

  // Autounique Attributes
  private int id;

  // ShelfLocation Associations
  private Shelf shelf;
  private CheeseWheel cheeseWheel;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ShelfLocation(int aColumn, int aRow, Shelf aShelf) {
    column = aColumn;
    row = aRow;
    id = nextId++;
    boolean didAddShelf = setShelf(aShelf);
    if (!didAddShelf) {
      throw new RuntimeException(
          "Unable to create location due to shelf. See "
          + "https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setColumn(int aColumn) {
    boolean wasSet = false;
    column = aColumn;
    wasSet = true;
    return wasSet;
  }

  public boolean setRow(int aRow) {
    boolean wasSet = false;
    row = aRow;
    wasSet = true;
    return wasSet;
  }

  public int getColumn() {
    return column;
  }

  public int getRow() {
    return row;
  }

  public int getId() {
    return id;
  }
  /* Code from template association_GetOne */
  public Shelf getShelf() {
    return shelf;
  }
  /* Code from template association_GetOne */
  public CheeseWheel getCheeseWheel() {
    return cheeseWheel;
  }

  public boolean hasCheeseWheel() {
    boolean has = cheeseWheel != null;
    return has;
  }
  /* Code from template association_SetOneToMany */
  public boolean setShelf(Shelf aShelf) {
    boolean wasSet = false;
    if (aShelf == null) {
      return wasSet;
    }

    Shelf existingShelf = shelf;
    shelf = aShelf;
    if (existingShelf != null && !existingShelf.equals(aShelf)) {
      existingShelf.removeLocation(this);
    }
    shelf.addLocation(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setCheeseWheel(CheeseWheel aNewCheeseWheel) {
    boolean wasSet = false;
    if (aNewCheeseWheel == null) {
      CheeseWheel existingCheeseWheel = cheeseWheel;
      cheeseWheel = null;

      if (existingCheeseWheel != null && existingCheeseWheel.getLocation() != null) {
        existingCheeseWheel.setLocation(null);
      }
      wasSet = true;
      return wasSet;
    }

    CheeseWheel currentCheeseWheel = getCheeseWheel();
    if (currentCheeseWheel != null && !currentCheeseWheel.equals(aNewCheeseWheel)) {
      currentCheeseWheel.setLocation(null);
    }

    cheeseWheel = aNewCheeseWheel;
    ShelfLocation existingLocation = aNewCheeseWheel.getLocation();

    if (!equals(existingLocation)) {
      aNewCheeseWheel.setLocation(this);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    Shelf placeholderShelf = shelf;
    this.shelf = null;
    if (placeholderShelf != null) {
      placeholderShelf.removeLocation(this);
    }
    if (cheeseWheel != null) {
      cheeseWheel.setLocation(null);
    }
  }

  /**
   * private static int nextId = 1;
   */
  // line 55 "../../../../../CheECSEManagerPersistence.ump"
  public static void reinitializeNextId(List<ShelfLocation> locations) {
    int maxId = 0;
    for (var loc : locations) {
      if (loc.getId() > maxId) {
        maxId = loc.getId();
      }
    }
    nextId = maxId + 1;
  }

  // line 53 "../../../../../CheECSEManager.ump"
  public static void resetId() {
    nextId = 1;
  }

  public String toString() {
    return super.toString() + "["
        + "id"
        + ":" + getId() + ","
        + "column"
        + ":" + getColumn() + ","
        + "row"
        + ":" + getRow() + "]" + System.getProperties().getProperty("line.separator") + "  "
        + "shelf = "
        + (getShelf() != null ? Integer.toHexString(System.identityHashCode(getShelf())) : "null")
        + System.getProperties().getProperty("line.separator") + "  "
        + "cheeseWheel = "
        + (getCheeseWheel() != null ? Integer.toHexString(System.identityHashCode(getCheeseWheel()))
                                    : "null");
  }
}