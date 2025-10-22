/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.model;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import java.sql.Date;
import java.util.*;

// line 23 "../../../../../CheECSEManager.ump"
public class Farmer extends User {
  //------------------------
  // MEMBER VARIABLES
  //------------------------

  // Farmer Attributes
  private String name;
  private String address;

  // Farmer Associations
  private List<Purchase> purchases;
  private CheECSEManager cheECSEManager;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Farmer(String aEmail, String aPassword, String aAddress, CheECSEManager aCheECSEManager) {
    super(aEmail, aPassword);
    name = null;
    address = aAddress;
    purchases = new ArrayList<Purchase>();
    boolean didAddCheECSEManager = setCheECSEManager(aCheECSEManager);
    if (!didAddCheECSEManager) {
      throw new RuntimeException(
          "Unable to create farmer due to cheECSEManager. See "
          + "https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName) {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setAddress(String aAddress) {
    boolean wasSet = false;
    address = aAddress;
    wasSet = true;
    return wasSet;
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }
  /* Code from template association_GetMany */
  public Purchase getPurchase(int index) {
    Purchase aPurchase = purchases.get(index);
    return aPurchase;
  }

  public List<Purchase> getPurchases() {
    List<Purchase> newPurchases = Collections.unmodifiableList(purchases);
    return newPurchases;
  }

  public int numberOfPurchases() {
    int number = purchases.size();
    return number;
  }

  public boolean hasPurchases() {
    boolean has = purchases.size() > 0;
    return has;
  }

  public int indexOfPurchase(Purchase aPurchase) {
    int index = purchases.indexOf(aPurchase);
    return index;
  }
  /* Code from template association_GetOne */
  public CheECSEManager getCheECSEManager() {
    return cheECSEManager;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPurchases() {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Purchase addPurchase(Date aTransactionDate, CheECSEManager aCheECSEManager) {
    return new Purchase(aTransactionDate, aCheECSEManager, this);
  }

  public boolean addPurchase(Purchase aPurchase) {
    boolean wasAdded = false;
    if (purchases.contains(aPurchase)) {
      return false;
    }
    Farmer existingFarmer = aPurchase.getFarmer();
    boolean isNewFarmer = existingFarmer != null && !this.equals(existingFarmer);
    if (isNewFarmer) {
      aPurchase.setFarmer(this);
    } else {
      purchases.add(aPurchase);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePurchase(Purchase aPurchase) {
    boolean wasRemoved = false;
    // Unable to remove aPurchase, as it must always have a farmer
    if (!this.equals(aPurchase.getFarmer())) {
      purchases.remove(aPurchase);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPurchaseAt(Purchase aPurchase, int index) {
    boolean wasAdded = false;
    if (addPurchase(aPurchase)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfPurchases()) {
        index = numberOfPurchases() - 1;
      }
      purchases.remove(aPurchase);
      purchases.add(index, aPurchase);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePurchaseAt(Purchase aPurchase, int index) {
    boolean wasAdded = false;
    if (purchases.contains(aPurchase)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfPurchases()) {
        index = numberOfPurchases() - 1;
      }
      purchases.remove(aPurchase);
      purchases.add(index, aPurchase);
      wasAdded = true;
    } else {
      wasAdded = addPurchaseAt(aPurchase, index);
    }
    return wasAdded;
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
      existingCheECSEManager.removeFarmer(this);
    }
    cheECSEManager.addFarmer(this);
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    for (int i = purchases.size(); i > 0; i--) {
      Purchase aPurchase = purchases.get(i - 1);
      aPurchase.delete();
    }
    CheECSEManager placeholderCheECSEManager = cheECSEManager;
    this.cheECSEManager = null;
    if (placeholderCheECSEManager != null) {
      placeholderCheECSEManager.removeFarmer(this);
    }
    super.delete();
  }

  public String toString() {
    return super.toString() + "["
        + "name"
        + ":" + getName() + ","
        + "address"
        + ":" + getAddress() + "]" + System.getProperties().getProperty("line.separator") + "  "
        + "cheECSEManager = "
        + (getCheECSEManager() != null
                ? Integer.toHexString(System.identityHashCode(getCheECSEManager()))
                : "null");
  }
}