/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.sql.Date;
import java.util.*;

// line 38 "model.ump"
// line 135 "model.ump"
public class Order
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Order Attributes
  private int kind;
  private int numberOfMissingCheese;
  private Date purchaseDate;
  private Date deliveryDate;

  //Order Associations
  private List<Cheese> orderedCheese;
  private Company company;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(int aKind, int aNumberOfMissingCheese, Date aPurchaseDate, Date aDeliveryDate, Company aCompany)
  {
    kind = aKind;
    numberOfMissingCheese = aNumberOfMissingCheese;
    purchaseDate = aPurchaseDate;
    deliveryDate = aDeliveryDate;
    orderedCheese = new ArrayList<Cheese>();
    boolean didAddCompany = setCompany(aCompany);
    if (!didAddCompany)
    {
      throw new RuntimeException("Unable to create order due to company. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setKind(int aKind)
  {
    boolean wasSet = false;
    kind = aKind;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumberOfMissingCheese(int aNumberOfMissingCheese)
  {
    boolean wasSet = false;
    numberOfMissingCheese = aNumberOfMissingCheese;
    wasSet = true;
    return wasSet;
  }

  public boolean setPurchaseDate(Date aPurchaseDate)
  {
    boolean wasSet = false;
    purchaseDate = aPurchaseDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setDeliveryDate(Date aDeliveryDate)
  {
    boolean wasSet = false;
    deliveryDate = aDeliveryDate;
    wasSet = true;
    return wasSet;
  }

  public int getKind()
  {
    return kind;
  }

  public int getNumberOfMissingCheese()
  {
    return numberOfMissingCheese;
  }

  public Date getPurchaseDate()
  {
    return purchaseDate;
  }

  public Date getDeliveryDate()
  {
    return deliveryDate;
  }
  /* Code from template association_GetMany */
  public Cheese getOrderedCheese(int index)
  {
    Cheese aOrderedCheese = orderedCheese.get(index);
    return aOrderedCheese;
  }

  public List<Cheese> getOrderedCheese()
  {
    List<Cheese> newOrderedCheese = Collections.unmodifiableList(orderedCheese);
    return newOrderedCheese;
  }

  public int numberOfOrderedCheese()
  {
    int number = orderedCheese.size();
    return number;
  }

  public boolean hasOrderedCheese()
  {
    boolean has = orderedCheese.size() > 0;
    return has;
  }

  public int indexOfOrderedCheese(Cheese aOrderedCheese)
  {
    int index = orderedCheese.indexOf(aOrderedCheese);
    return index;
  }
  /* Code from template association_GetOne */
  public Company getCompany()
  {
    return company;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrderedCheese()
  {
    return 0;
  }
  /* Code from template association_AddManyToOptionalOne */
  public boolean addOrderedCheese(Cheese aOrderedCheese)
  {
    boolean wasAdded = false;
    if (orderedCheese.contains(aOrderedCheese)) { return false; }
    Order existingOrder = aOrderedCheese.getOrder();
    if (existingOrder == null)
    {
      aOrderedCheese.setOrder(this);
    }
    else if (!this.equals(existingOrder))
    {
      existingOrder.removeOrderedCheese(aOrderedCheese);
      addOrderedCheese(aOrderedCheese);
    }
    else
    {
      orderedCheese.add(aOrderedCheese);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrderedCheese(Cheese aOrderedCheese)
  {
    boolean wasRemoved = false;
    if (orderedCheese.contains(aOrderedCheese))
    {
      orderedCheese.remove(aOrderedCheese);
      aOrderedCheese.setOrder(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderedCheeseAt(Cheese aOrderedCheese, int index)
  {  
    boolean wasAdded = false;
    if(addOrderedCheese(aOrderedCheese))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderedCheese()) { index = numberOfOrderedCheese() - 1; }
      orderedCheese.remove(aOrderedCheese);
      orderedCheese.add(index, aOrderedCheese);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderedCheeseAt(Cheese aOrderedCheese, int index)
  {
    boolean wasAdded = false;
    if(orderedCheese.contains(aOrderedCheese))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderedCheese()) { index = numberOfOrderedCheese() - 1; }
      orderedCheese.remove(aOrderedCheese);
      orderedCheese.add(index, aOrderedCheese);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderedCheeseAt(aOrderedCheese, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCompany(Company aCompany)
  {
    boolean wasSet = false;
    if (aCompany == null)
    {
      return wasSet;
    }

    Company existingCompany = company;
    company = aCompany;
    if (existingCompany != null && !existingCompany.equals(aCompany))
    {
      existingCompany.removeOrder(this);
    }
    company.addOrder(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    while( !orderedCheese.isEmpty() )
    {
      orderedCheese.get(0).setOrder(null);
    }
    Company placeholderCompany = company;
    this.company = null;
    if(placeholderCompany != null)
    {
      placeholderCompany.removeOrder(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "kind" + ":" + getKind()+ "," +
            "numberOfMissingCheese" + ":" + getNumberOfMissingCheese()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "purchaseDate" + "=" + (getPurchaseDate() != null ? !getPurchaseDate().equals(this)  ? getPurchaseDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "deliveryDate" + "=" + (getDeliveryDate() != null ? !getDeliveryDate().equals(this)  ? getDeliveryDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "company = "+(getCompany()!=null?Integer.toHexString(System.identityHashCode(getCompany())):"null");
  }
}