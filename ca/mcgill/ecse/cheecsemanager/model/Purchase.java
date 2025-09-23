/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.sql.Date;

// line 87 "model.ump"
// line 171 "model.ump"
public class Purchase
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Purchase Attributes
  private Date purchaseDate;

  //Purchase Associations
  private Cheese cheese;
  private Farmer farmer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Purchase(Date aPurchaseDate, Cheese aCheese, Farmer aFarmer)
  {
    purchaseDate = aPurchaseDate;
    if (aCheese == null || aCheese.getPurchase() != null)
    {
      throw new RuntimeException("Unable to create Purchase due to aCheese. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    cheese = aCheese;
    boolean didAddFarmer = setFarmer(aFarmer);
    if (!didAddFarmer)
    {
      throw new RuntimeException("Unable to create purchase due to farmer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Purchase(Date aPurchaseDate, int aAgeForCheese, int aMonthsToAgeForCheese, boolean aIsSpoiledForCheese, Farmer aFarmer)
  {
    purchaseDate = aPurchaseDate;
    cheese = new Cheese(aAgeForCheese, aMonthsToAgeForCheese, aIsSpoiledForCheese, this);
    boolean didAddFarmer = setFarmer(aFarmer);
    if (!didAddFarmer)
    {
      throw new RuntimeException("Unable to create purchase due to farmer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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
  /* Code from template association_GetOne */
  public Cheese getCheese()
  {
    return cheese;
  }
  /* Code from template association_GetOne */
  public Farmer getFarmer()
  {
    return farmer;
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
      existingFarmer.removePurchase(this);
    }
    farmer.addPurchase(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Cheese existingCheese = cheese;
    cheese = null;
    if (existingCheese != null)
    {
      existingCheese.delete();
    }
    Farmer placeholderFarmer = farmer;
    this.farmer = null;
    if(placeholderFarmer != null)
    {
      placeholderFarmer.removePurchase(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "purchaseDate" + "=" + (getPurchaseDate() != null ? !getPurchaseDate().equals(this)  ? getPurchaseDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "cheese = "+(getCheese()!=null?Integer.toHexString(System.identityHashCode(getCheese())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "farmer = "+(getFarmer()!=null?Integer.toHexString(System.identityHashCode(getFarmer())):"null");
  }
}