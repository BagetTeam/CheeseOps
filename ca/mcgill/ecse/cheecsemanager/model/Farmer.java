/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;
import java.sql.Date;

// line 2 "model.ump"
// line 93 "model.ump"
public class Farmer extends User
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Farmer> farmersByName = new HashMap<String, Farmer>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Farmer Attributes
  private String postalCode;
  private String name;

  //Farmer Associations
  private List<Purchase> purchases;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Farmer(String aEmail, String aPassword, String aPostalCode, String aName)
  {
    super(aEmail, aPassword);
    postalCode = aPostalCode;
    if (!setName(aName))
    {
      throw new RuntimeException("Cannot create due to duplicate name. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    purchases = new ArrayList<Purchase>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPostalCode(String aPostalCode)
  {
    boolean wasSet = false;
    postalCode = aPostalCode;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    String anOldName = getName();
    if (anOldName != null && anOldName.equals(aName)) {
      return true;
    }
    if (hasWithName(aName)) {
      return wasSet;
    }
    name = aName;
    wasSet = true;
    if (anOldName != null) {
      farmersByName.remove(anOldName);
    }
    farmersByName.put(aName, this);
    return wasSet;
  }

  public String getPostalCode()
  {
    return postalCode;
  }

  public String getName()
  {
    return name;
  }
  /* Code from template attribute_GetUnique */
  public static Farmer getWithName(String aName)
  {
    return farmersByName.get(aName);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithName(String aName)
  {
    return getWithName(aName) != null;
  }
  /* Code from template association_GetMany */
  public Purchase getPurchase(int index)
  {
    Purchase aPurchase = purchases.get(index);
    return aPurchase;
  }

  public List<Purchase> getPurchases()
  {
    List<Purchase> newPurchases = Collections.unmodifiableList(purchases);
    return newPurchases;
  }

  public int numberOfPurchases()
  {
    int number = purchases.size();
    return number;
  }

  public boolean hasPurchases()
  {
    boolean has = purchases.size() > 0;
    return has;
  }

  public int indexOfPurchase(Purchase aPurchase)
  {
    int index = purchases.indexOf(aPurchase);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPurchases()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Purchase addPurchase(Date aPurchaseDate, Cheese aCheese)
  {
    return new Purchase(aPurchaseDate, aCheese, this);
  }

  public boolean addPurchase(Purchase aPurchase)
  {
    boolean wasAdded = false;
    if (purchases.contains(aPurchase)) { return false; }
    Farmer existingFarmer = aPurchase.getFarmer();
    boolean isNewFarmer = existingFarmer != null && !this.equals(existingFarmer);
    if (isNewFarmer)
    {
      aPurchase.setFarmer(this);
    }
    else
    {
      purchases.add(aPurchase);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePurchase(Purchase aPurchase)
  {
    boolean wasRemoved = false;
    //Unable to remove aPurchase, as it must always have a farmer
    if (!this.equals(aPurchase.getFarmer()))
    {
      purchases.remove(aPurchase);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPurchaseAt(Purchase aPurchase, int index)
  {  
    boolean wasAdded = false;
    if(addPurchase(aPurchase))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPurchases()) { index = numberOfPurchases() - 1; }
      purchases.remove(aPurchase);
      purchases.add(index, aPurchase);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePurchaseAt(Purchase aPurchase, int index)
  {
    boolean wasAdded = false;
    if(purchases.contains(aPurchase))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPurchases()) { index = numberOfPurchases() - 1; }
      purchases.remove(aPurchase);
      purchases.add(index, aPurchase);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPurchaseAt(aPurchase, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    farmersByName.remove(getName());
    for(int i=purchases.size(); i > 0; i--)
    {
      Purchase aPurchase = purchases.get(i - 1);
      aPurchase.delete();
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "postalCode" + ":" + getPostalCode()+ "," +
            "name" + ":" + getName()+ "]";
  }
}