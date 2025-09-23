/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.sql.Date;

// line 23 "model.ump"
// line 116 "model.ump"
public class Cheese
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Cheese Attributes
  private int age;
  private int monthsToAge;
  private boolean isSpoiled;

  //Cheese Associations
  private ShelfSlot shelfSlot;
  private Order order;
  private Purchase purchase;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Cheese(int aAge, int aMonthsToAge, boolean aIsSpoiled, Purchase aPurchase)
  {
    age = aAge;
    monthsToAge = aMonthsToAge;
    isSpoiled = aIsSpoiled;
    if (aPurchase == null || aPurchase.getCheese() != null)
    {
      throw new RuntimeException("Unable to create Cheese due to aPurchase. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    purchase = aPurchase;
  }

  public Cheese(int aAge, int aMonthsToAge, boolean aIsSpoiled, Date aPurchaseDateForPurchase, Farmer aFarmerForPurchase)
  {
    age = aAge;
    monthsToAge = aMonthsToAge;
    isSpoiled = aIsSpoiled;
    purchase = new Purchase(aPurchaseDateForPurchase, this, aFarmerForPurchase);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAge(int aAge)
  {
    boolean wasSet = false;
    age = aAge;
    wasSet = true;
    return wasSet;
  }

  public boolean setMonthsToAge(int aMonthsToAge)
  {
    boolean wasSet = false;
    monthsToAge = aMonthsToAge;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsSpoiled(boolean aIsSpoiled)
  {
    boolean wasSet = false;
    isSpoiled = aIsSpoiled;
    wasSet = true;
    return wasSet;
  }

  public int getAge()
  {
    return age;
  }

  public int getMonthsToAge()
  {
    return monthsToAge;
  }

  public boolean getIsSpoiled()
  {
    return isSpoiled;
  }
  /* Code from template association_GetOne */
  public ShelfSlot getShelfSlot()
  {
    return shelfSlot;
  }

  public boolean hasShelfSlot()
  {
    boolean has = shelfSlot != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Order getOrder()
  {
    return order;
  }

  public boolean hasOrder()
  {
    boolean has = order != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Purchase getPurchase()
  {
    return purchase;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setShelfSlot(ShelfSlot aNewShelfSlot)
  {
    boolean wasSet = false;
    if (shelfSlot != null && !shelfSlot.equals(aNewShelfSlot) && equals(shelfSlot.getCheese()))
    {
      //Unable to setShelfSlot, as existing shelfSlot would become an orphan
      return wasSet;
    }

    shelfSlot = aNewShelfSlot;
    Cheese anOldCheese = aNewShelfSlot != null ? aNewShelfSlot.getCheese() : null;

    if (!this.equals(anOldCheese))
    {
      if (anOldCheese != null)
      {
        anOldCheese.shelfSlot = null;
      }
      if (shelfSlot != null)
      {
        shelfSlot.setCheese(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setOrder(Order aOrder)
  {
    boolean wasSet = false;
    Order existingOrder = order;
    order = aOrder;
    if (existingOrder != null && !existingOrder.equals(aOrder))
    {
      existingOrder.removeCheese(this);
    }
    if (aOrder != null)
    {
      aOrder.addCheese(this);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    ShelfSlot existingShelfSlot = shelfSlot;
    shelfSlot = null;
    if (existingShelfSlot != null)
    {
      existingShelfSlot.delete();
    }
    if (order != null)
    {
      Order placeholderOrder = order;
      this.order = null;
      placeholderOrder.removeCheese(this);
    }
    Purchase existingPurchase = purchase;
    purchase = null;
    if (existingPurchase != null)
    {
      existingPurchase.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "age" + ":" + getAge()+ "," +
            "monthsToAge" + ":" + getMonthsToAge()+ "," +
            "isSpoiled" + ":" + getIsSpoiled()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "shelfSlot = "+(getShelfSlot()!=null?Integer.toHexString(System.identityHashCode(getShelfSlot())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "order = "+(getOrder()!=null?Integer.toHexString(System.identityHashCode(getOrder())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "purchase = "+(getPurchase()!=null?Integer.toHexString(System.identityHashCode(getPurchase())):"null");
  }
}