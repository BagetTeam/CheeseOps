/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.sql.Date;
import java.util.*;

// line 10 "model.ump"
// line 180 "model.ump"
public class Purchase
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Purchase Attributes
  private Date purchaseDate;

  //Purchase Associations
  private List<Cheese> cheeseWheels;
  private Farmer farmer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Purchase(Date aPurchaseDate, Farmer aFarmer)
  {
    purchaseDate = aPurchaseDate;
    cheeseWheels = new ArrayList<Cheese>();
    boolean didAddFarmer = setFarmer(aFarmer);
    if (!didAddFarmer)
    {
      throw new RuntimeException("Unable to create sale due to farmer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPurchaseDate(Date aPurchaseDate)
  {
    boolean wasSet = false;
    purchaseDate = aPurchaseDate;
    wasSet = true;
    return wasSet;
  }

  public Date getPurchaseDate()
  {
    return purchaseDate;
  }
  /* Code from template association_GetMany */
  public Cheese getCheeseWheel(int index)
  {
    Cheese aCheeseWheel = cheeseWheels.get(index);
    return aCheeseWheel;
  }

  public List<Cheese> getCheeseWheels()
  {
    List<Cheese> newCheeseWheels = Collections.unmodifiableList(cheeseWheels);
    return newCheeseWheels;
  }

  public int numberOfCheeseWheels()
  {
    int number = cheeseWheels.size();
    return number;
  }

  public boolean hasCheeseWheels()
  {
    boolean has = cheeseWheels.size() > 0;
    return has;
  }

  public int indexOfCheeseWheel(Cheese aCheeseWheel)
  {
    int index = cheeseWheels.indexOf(aCheeseWheel);
    return index;
  }
  /* Code from template association_GetOne */
  public Farmer getFarmer()
  {
    return farmer;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfCheeseWheelsValid()
  {
    boolean isValid = numberOfCheeseWheels() >= minimumNumberOfCheeseWheels();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCheeseWheels()
  {
    return 1;
  }
  /* Code from template association_AddMandatoryManyToOne */
  public Cheese addCheeseWheel(int aAge, int aMonthsToAge, boolean aIsSpoiled)
  {
    Cheese aNewCheeseWheel = new Cheese(aAge, aMonthsToAge, aIsSpoiled, this);
    return aNewCheeseWheel;
  }

  public boolean addCheeseWheel(Cheese aCheeseWheel)
  {
    boolean wasAdded = false;
    if (cheeseWheels.contains(aCheeseWheel)) { return false; }
    Purchase existingPurchase = aCheeseWheel.getPurchase();
    boolean isNewPurchase = existingPurchase != null && !this.equals(existingPurchase);

    if (isNewPurchase && existingPurchase.numberOfCheeseWheels() <= minimumNumberOfCheeseWheels())
    {
      return wasAdded;
    }
    if (isNewPurchase)
    {
      aCheeseWheel.setPurchase(this);
    }
    else
    {
      cheeseWheels.add(aCheeseWheel);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCheeseWheel(Cheese aCheeseWheel)
  {
    boolean wasRemoved = false;
    //Unable to remove aCheeseWheel, as it must always have a purchase
    if (this.equals(aCheeseWheel.getPurchase()))
    {
      return wasRemoved;
    }

    //purchase already at minimum (1)
    if (numberOfCheeseWheels() <= minimumNumberOfCheeseWheels())
    {
      return wasRemoved;
    }

    cheeseWheels.remove(aCheeseWheel);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCheeseWheelAt(Cheese aCheeseWheel, int index)
  {  
    boolean wasAdded = false;
    if(addCheeseWheel(aCheeseWheel))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCheeseWheels()) { index = numberOfCheeseWheels() - 1; }
      cheeseWheels.remove(aCheeseWheel);
      cheeseWheels.add(index, aCheeseWheel);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCheeseWheelAt(Cheese aCheeseWheel, int index)
  {
    boolean wasAdded = false;
    if(cheeseWheels.contains(aCheeseWheel))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCheeseWheels()) { index = numberOfCheeseWheels() - 1; }
      cheeseWheels.remove(aCheeseWheel);
      cheeseWheels.add(index, aCheeseWheel);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCheeseWheelAt(aCheeseWheel, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setFarmer(Farmer aFarmer)
  {
    boolean wasSet = false;
    if (aFarmer == null)
    {
      return wasSet;
    }

    Farmer existingFarmer = farmer;
    farmer = aFarmer;
    if (existingFarmer != null && !existingFarmer.equals(aFarmer))
    {
      existingFarmer.removeSale(this);
    }
    farmer.addSale(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    for(int i=cheeseWheels.size(); i > 0; i--)
    {
      Cheese aCheeseWheel = cheeseWheels.get(i - 1);
      aCheeseWheel.delete();
    }
    Farmer placeholderFarmer = farmer;
    this.farmer = null;
    if(placeholderFarmer != null)
    {
      placeholderFarmer.removeSale(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "purchaseDate" + "=" + (getPurchaseDate() != null ? !getPurchaseDate().equals(this)  ? getPurchaseDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "farmer = "+(getFarmer()!=null?Integer.toHexString(System.identityHashCode(getFarmer())):"null");
  }
}