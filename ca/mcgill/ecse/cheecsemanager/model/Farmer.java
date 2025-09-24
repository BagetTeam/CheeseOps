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
  private List<Purchase> sales;

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
    sales = new ArrayList<Purchase>();
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
  public Purchase getSale(int index)
  {
    Purchase aSale = sales.get(index);
    return aSale;
  }

  public List<Purchase> getSales()
  {
    List<Purchase> newSales = Collections.unmodifiableList(sales);
    return newSales;
  }

  public int numberOfSales()
  {
    int number = sales.size();
    return number;
  }

  public boolean hasSales()
  {
    boolean has = sales.size() > 0;
    return has;
  }

  public int indexOfSale(Purchase aSale)
  {
    int index = sales.indexOf(aSale);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSales()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Purchase addSale(Date aPurchaseDate)
  {
    return new Purchase(aPurchaseDate, this);
  }

  public boolean addSale(Purchase aSale)
  {
    boolean wasAdded = false;
    if (sales.contains(aSale)) { return false; }
    Farmer existingFarmer = aSale.getFarmer();
    boolean isNewFarmer = existingFarmer != null && !this.equals(existingFarmer);
    if (isNewFarmer)
    {
      aSale.setFarmer(this);
    }
    else
    {
      sales.add(aSale);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSale(Purchase aSale)
  {
    boolean wasRemoved = false;
    //Unable to remove aSale, as it must always have a farmer
    if (!this.equals(aSale.getFarmer()))
    {
      sales.remove(aSale);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addSaleAt(Purchase aSale, int index)
  {  
    boolean wasAdded = false;
    if(addSale(aSale))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSales()) { index = numberOfSales() - 1; }
      sales.remove(aSale);
      sales.add(index, aSale);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSaleAt(Purchase aSale, int index)
  {
    boolean wasAdded = false;
    if(sales.contains(aSale))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSales()) { index = numberOfSales() - 1; }
      sales.remove(aSale);
      sales.add(index, aSale);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSaleAt(aSale, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    farmersByName.remove(getName());
    for(int i=sales.size(); i > 0; i--)
    {
      Purchase aSale = sales.get(i - 1);
      aSale.delete();
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