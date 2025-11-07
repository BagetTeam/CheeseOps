/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.model;

import java.util.List;

// line 28 "../../../../../CheECSEManagerPersistence.ump"
// line 57 "../../../../../CheECSEManager.ump"
public class CheeseWheel {
  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum MaturationPeriod { Six, Twelve, TwentyFour, ThirtySix }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  // CheeseWheel Attributes
  private MaturationPeriod monthsAged;
  private boolean isSpoiled;

  // Autounique Attributes
  private int id;

  // CheeseWheel Associations
  private Purchase purchase;
  private ShelfLocation location;
  private Order order;
  private CheECSEManager cheECSEManager;
  private Robot robot;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CheeseWheel(MaturationPeriod aMonthsAged, boolean aIsSpoiled, Purchase aPurchase,
      CheECSEManager aCheECSEManager) {
    monthsAged = aMonthsAged;
    isSpoiled = aIsSpoiled;
    id = nextId++;
    boolean didAddPurchase = setPurchase(aPurchase);
    if (!didAddPurchase) {
      throw new RuntimeException(
          "Unable to create cheeseWheel due to purchase. See "
          + "https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddCheECSEManager = setCheECSEManager(aCheECSEManager);
    if (!didAddCheECSEManager) {
      throw new RuntimeException(
          "Unable to create cheeseWheel due to cheECSEManager. See "
          + "https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setMonthsAged(MaturationPeriod aMonthsAged) {
    boolean wasSet = false;
    monthsAged = aMonthsAged;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsSpoiled(boolean aIsSpoiled) {
    boolean wasSet = false;
    isSpoiled = aIsSpoiled;
    wasSet = true;
    return wasSet;
  }

  public MaturationPeriod getMonthsAged() {
    return monthsAged;
  }

  public boolean getIsSpoiled() {
    return isSpoiled;
  }

  public int getId() {
    return id;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsSpoiled() {
    return isSpoiled;
  }
  /* Code from template association_GetOne */
  public Purchase getPurchase() {
    return purchase;
  }
  /* Code from template association_GetOne */
  public ShelfLocation getLocation() {
    return location;
  }

  public boolean hasLocation() {
    boolean has = location != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Order getOrder() {
    return order;
  }

  public boolean hasOrder() {
    boolean has = order != null;
    return has;
  }
  /* Code from template association_GetOne */
  public CheECSEManager getCheECSEManager() {
    return cheECSEManager;
  }
  /* Code from template association_GetOne */
  public Robot getRobot() {
    return robot;
  }

  public boolean hasRobot() {
    boolean has = robot != null;
    return has;
  }
  /* Code from template association_SetOneToMany */
  public boolean setPurchase(Purchase aPurchase) {
    boolean wasSet = false;
    if (aPurchase == null) {
      return wasSet;
    }

    Purchase existingPurchase = purchase;
    purchase = aPurchase;
    if (existingPurchase != null && !existingPurchase.equals(aPurchase)) {
      existingPurchase.removeCheeseWheel(this);
    }
    purchase.addCheeseWheel(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setLocation(ShelfLocation aNewLocation) {
    boolean wasSet = false;
    if (aNewLocation == null) {
      ShelfLocation existingLocation = location;
      location = null;

      if (existingLocation != null && existingLocation.getCheeseWheel() != null) {
        existingLocation.setCheeseWheel(null);
      }
      wasSet = true;
      return wasSet;
    }

    ShelfLocation currentLocation = getLocation();
    if (currentLocation != null && !currentLocation.equals(aNewLocation)) {
      currentLocation.setCheeseWheel(null);
    }

    location = aNewLocation;
    CheeseWheel existingCheeseWheel = aNewLocation.getCheeseWheel();

    if (!equals(existingCheeseWheel)) {
      aNewLocation.setCheeseWheel(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setOrder(Order aOrder) {
    boolean wasSet = false;
    Order existingOrder = order;
    order = aOrder;
    if (existingOrder != null && !existingOrder.equals(aOrder)) {
      existingOrder.removeCheeseWheel(this);
    }
    if (aOrder != null) {
      aOrder.addCheeseWheel(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCheECSEManager(CheECSEManager aCheECSEManager) {
    boolean wasSet = false;
    if (aCheECSEManager == null) {
      return wasSet;
    }

    CheECSEManager existingCheECSEManager = cheECSEManager;
    cheECSEManager = aCheECSEManager;
    if (existingCheECSEManager != null && !existingCheECSEManager.equals(aCheECSEManager)) {
      existingCheECSEManager.removeCheeseWheel(this);
    }
    cheECSEManager.addCheeseWheel(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setRobot(Robot aNewRobot) {
    boolean wasSet = false;
    if (aNewRobot == null) {
      Robot existingRobot = robot;
      robot = null;

      if (existingRobot != null && existingRobot.getCurrentCheeseWheel() != null) {
        existingRobot.setCurrentCheeseWheel(null);
      }
      wasSet = true;
      return wasSet;
    }

    Robot currentRobot = getRobot();
    if (currentRobot != null && !currentRobot.equals(aNewRobot)) {
      currentRobot.setCurrentCheeseWheel(null);
    }

    robot = aNewRobot;
    CheeseWheel existingCurrentCheeseWheel = aNewRobot.getCurrentCheeseWheel();

    if (!equals(existingCurrentCheeseWheel)) {
      aNewRobot.setCurrentCheeseWheel(this);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    Purchase placeholderPurchase = purchase;
    this.purchase = null;
    if (placeholderPurchase != null) {
      placeholderPurchase.removeCheeseWheel(this);
    }
    if (location != null) {
      location.setCheeseWheel(null);
    }
    if (order != null) {
      Order placeholderOrder = order;
      this.order = null;
      placeholderOrder.removeCheeseWheel(this);
    }
    CheECSEManager placeholderCheECSEManager = cheECSEManager;
    this.cheECSEManager = null;
    if (placeholderCheECSEManager != null) {
      placeholderCheECSEManager.removeCheeseWheel(this);
    }
    if (robot != null) {
      robot.setCurrentCheeseWheel(null);
    }
  }

  /**
   * private static int nextId = 1;
   */
  // line 31 "../../../../../CheECSEManagerPersistence.ump"
  public static void reinitializeId(List<CheeseWheel> wheels) {
    int maxId = 0;
    for (CheeseWheel w : wheels) {
      if (w.getId() > maxId) {
        maxId = w.getId();
      }
    }
    nextId = maxId + 1;
  }

  // line 67 "../../../../../CheECSEManager.ump"
  public static void resetId() {
    nextId = 1;
  }

  public String toString() {
    return super.toString() + "["
        + "id"
        + ":" + getId() + ","
        + "isSpoiled"
        + ":" + getIsSpoiled() + "]" + System.getProperties().getProperty("line.separator") + "  "
        + "monthsAged"
        + "="
        + (getMonthsAged() != null ? !getMonthsAged().equals(this)
                    ? getMonthsAged().toString().replaceAll("  ", "    ")
                    : "this"
                                   : "null")
        + System.getProperties().getProperty("line.separator") + "  "
        + "purchase = "
        + (getPurchase() != null ? Integer.toHexString(System.identityHashCode(getPurchase()))
                                 : "null")
        + System.getProperties().getProperty("line.separator") + "  "
        + "location = "
        + (getLocation() != null ? Integer.toHexString(System.identityHashCode(getLocation()))
                                 : "null")
        + System.getProperties().getProperty("line.separator") + "  "
        + "order = "
        + (getOrder() != null ? Integer.toHexString(System.identityHashCode(getOrder())) : "null")
        + System.getProperties().getProperty("line.separator") + "  "
        + "cheECSEManager = "
        + (getCheECSEManager() != null
                ? Integer.toHexString(System.identityHashCode(getCheECSEManager()))
                : "null")
        + System.getProperties().getProperty("line.separator") + "  "
        + "robot = "
        + (getRobot() != null ? Integer.toHexString(System.identityHashCode(getRobot())) : "null");
  }
}