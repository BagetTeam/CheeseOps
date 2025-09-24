/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/



// line 23 "model.ump"
// line 121 "model.ump"
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
  private ShelfSlot slot;
  private Order Order;
  private Purchase purchase;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Cheese(int aAge, int aMonthsToAge, boolean aIsSpoiled, Purchase aPurchase)
  {
    age = aAge;
    monthsToAge = aMonthsToAge;
    isSpoiled = aIsSpoiled;
    boolean didAddPurchase = setPurchase(aPurchase);
    if (!didAddPurchase)
    {
      throw new RuntimeException("Unable to create cheeseWheel due to purchase. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
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
  public ShelfSlot getSlot()
  {
    return slot;
  }

  public boolean hasSlot()
  {
    boolean has = slot != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Order getOrder()
  {
    return Order;
  }

  public boolean hasOrder()
  {
    boolean has = Order != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Purchase getPurchase()
  {
    return purchase;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setSlot(ShelfSlot aNewSlot)
  {
    boolean wasSet = false;
    if (slot != null && !slot.equals(aNewSlot) && equals(slot.getCheese()))
    {
      //Unable to setSlot, as existing slot would become an orphan
      return wasSet;
    }

    slot = aNewSlot;
    Cheese anOldCheese = aNewSlot != null ? aNewSlot.getCheese() : null;

    if (!this.equals(anOldCheese))
    {
      if (anOldCheese != null)
      {
        anOldCheese.slot = null;
      }
      if (slot != null)
      {
        slot.setCheese(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setOrder(Order aOrder)
  {
    boolean wasSet = false;
    Order existingOrder = Order;
    Order = aOrder;
    if (existingOrder != null && !existingOrder.equals(aOrder))
    {
      existingOrder.removeOrderedCheese(this);
    }
    if (aOrder != null)
    {
      aOrder.addOrderedCheese(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMandatoryMany */
  public boolean setPurchase(Purchase aPurchase)
  {
    boolean wasSet = false;
    //Must provide purchase to cheeseWheel
    if (aPurchase == null)
    {
      return wasSet;
    }

    if (purchase != null && purchase.numberOfCheeseWheels() <= Purchase.minimumNumberOfCheeseWheels())
    {
      return wasSet;
    }

    Purchase existingPurchase = purchase;
    purchase = aPurchase;
    if (existingPurchase != null && !existingPurchase.equals(aPurchase))
    {
      boolean didRemove = existingPurchase.removeCheeseWheel(this);
      if (!didRemove)
      {
        purchase = existingPurchase;
        return wasSet;
      }
    }
    purchase.addCheeseWheel(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    ShelfSlot existingSlot = slot;
    slot = null;
    if (existingSlot != null)
    {
      existingSlot.delete();
    }
    if (Order != null)
    {
      Order placeholderOrder = Order;
      this.Order = null;
      placeholderOrder.removeOrderedCheese(this);
    }
    Purchase placeholderPurchase = purchase;
    this.purchase = null;
    if(placeholderPurchase != null)
    {
      placeholderPurchase.removeCheeseWheel(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "age" + ":" + getAge()+ "," +
            "monthsToAge" + ":" + getMonthsToAge()+ "," +
            "isSpoiled" + ":" + getIsSpoiled()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "slot = "+(getSlot()!=null?Integer.toHexString(System.identityHashCode(getSlot())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "Order = "+(getOrder()!=null?Integer.toHexString(System.identityHashCode(getOrder())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "purchase = "+(getPurchase()!=null?Integer.toHexString(System.identityHashCode(getPurchase())):"null");
  }
}