/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.model;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import java.sql.Date;
import java.util.*;

// line 77 "../../../../../CheECSEManager.ump"
public class Purchase extends Transaction {
  //------------------------
  // MEMBER VARIABLES
  //------------------------

  // Purchase Associations
  private Farmer farmer;
  private List<CheeseWheel> cheeseWheels;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Purchase(Date aTransactionDate, CheECSEManager aCheECSEManager, Farmer aFarmer) {
    super(aTransactionDate, aCheECSEManager);
    boolean didAddFarmer = setFarmer(aFarmer);
    if (!didAddFarmer) {
      throw new RuntimeException("Unable to create purchase due to farmer. See "
          + "https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    cheeseWheels = new ArrayList<CheeseWheel>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Farmer getFarmer() {
    return farmer;
  }
  /* Code from template association_GetMany */
  public CheeseWheel getCheeseWheel(int index) {
    CheeseWheel aCheeseWheel = cheeseWheels.get(index);
    return aCheeseWheel;
  }

  public List<CheeseWheel> getCheeseWheels() {
    List<CheeseWheel> newCheeseWheels = Collections.unmodifiableList(cheeseWheels);
    return newCheeseWheels;
  }

  public int numberOfCheeseWheels() {
    int number = cheeseWheels.size();
    return number;
  }

  public boolean hasCheeseWheels() {
    boolean has = cheeseWheels.size() > 0;
    return has;
  }

  public int indexOfCheeseWheel(CheeseWheel aCheeseWheel) {
    int index = cheeseWheels.indexOf(aCheeseWheel);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setFarmer(Farmer aFarmer) {
    boolean wasSet = false;
    if (aFarmer == null) {
      return wasSet;
    }

    Farmer existingFarmer = farmer;
    farmer = aFarmer;
    if (existingFarmer != null && !existingFarmer.equals(aFarmer)) {
      existingFarmer.removePurchase(this);
    }
    farmer.addPurchase(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCheeseWheels() {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public CheeseWheel addCheeseWheel(CheeseWheel.MaturationPeriod aMonthsAged, boolean aIsSpoiled,
      CheECSEManager aCheECSEManager) {
    return new CheeseWheel(aMonthsAged, aIsSpoiled, this, aCheECSEManager);
  }

  public boolean addCheeseWheel(CheeseWheel aCheeseWheel) {
    boolean wasAdded = false;
    if (cheeseWheels.contains(aCheeseWheel)) {
      return false;
    }
    Purchase existingPurchase = aCheeseWheel.getPurchase();
    boolean isNewPurchase = existingPurchase != null && !this.equals(existingPurchase);
    if (isNewPurchase) {
      aCheeseWheel.setPurchase(this);
    } else {
      cheeseWheels.add(aCheeseWheel);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCheeseWheel(CheeseWheel aCheeseWheel) {
    boolean wasRemoved = false;
    // Unable to remove aCheeseWheel, as it must always have a purchase
    if (!this.equals(aCheeseWheel.getPurchase())) {
      cheeseWheels.remove(aCheeseWheel);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCheeseWheelAt(CheeseWheel aCheeseWheel, int index) {
    boolean wasAdded = false;
    if (addCheeseWheel(aCheeseWheel)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfCheeseWheels()) {
        index = numberOfCheeseWheels() - 1;
      }
      cheeseWheels.remove(aCheeseWheel);
      cheeseWheels.add(index, aCheeseWheel);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCheeseWheelAt(CheeseWheel aCheeseWheel, int index) {
    boolean wasAdded = false;
    if (cheeseWheels.contains(aCheeseWheel)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfCheeseWheels()) {
        index = numberOfCheeseWheels() - 1;
      }
      cheeseWheels.remove(aCheeseWheel);
      cheeseWheels.add(index, aCheeseWheel);
      wasAdded = true;
    } else {
      wasAdded = addCheeseWheelAt(aCheeseWheel, index);
    }
    return wasAdded;
  }

  public void delete() {
    Farmer placeholderFarmer = farmer;
    this.farmer = null;
    if (placeholderFarmer != null) {
      placeholderFarmer.removePurchase(this);
    }
    for (int i = cheeseWheels.size(); i > 0; i--) {
      CheeseWheel aCheeseWheel = cheeseWheels.get(i - 1);
      aCheeseWheel.delete();
    }
    super.delete();
  }
}