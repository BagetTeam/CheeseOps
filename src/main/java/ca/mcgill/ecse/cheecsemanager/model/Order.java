/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.model;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import java.sql.Date;
import java.util.*;

// line 86 "../../../../../CheECSEManager.ump"
public class Order extends Transaction
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Order Attributes
  private int nrCheeseWheels;
  private MaturationPeriod monthsAged;
  private Date deliveryDate;

  //Order Associations
  private WholesaleCompany company;
  private List<CheeseWheel> cheeseWheels;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(Date aTransactionDate, CheECSEManager aCheECSEManager, int aNrCheeseWheels, MaturationPeriod aMonthsAged, Date aDeliveryDate, WholesaleCompany aCompany)
  {
    super(aTransactionDate, aCheECSEManager);
    nrCheeseWheels = aNrCheeseWheels;
    monthsAged = aMonthsAged;
    deliveryDate = aDeliveryDate;
    boolean didAddCompany = setCompany(aCompany);
    if (!didAddCompany)
    {
      throw new RuntimeException("Unable to create order due to company. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    cheeseWheels = new ArrayList<CheeseWheel>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNrCheeseWheels(int aNrCheeseWheels)
  {
    boolean wasSet = false;
    nrCheeseWheels = aNrCheeseWheels;
    wasSet = true;
    return wasSet;
  }

  public boolean setMonthsAged(MaturationPeriod aMonthsAged)
  {
    boolean wasSet = false;
    monthsAged = aMonthsAged;
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

  public int getNrCheeseWheels()
  {
    return nrCheeseWheels;
  }

  public MaturationPeriod getMonthsAged()
  {
    return monthsAged;
  }

  public Date getDeliveryDate()
  {
    return deliveryDate;
  }
  /* Code from template association_GetOne */
  public WholesaleCompany getCompany()
  {
    return company;
  }
  /* Code from template association_GetMany */
  public CheeseWheel getCheeseWheel(int index)
  {
    CheeseWheel aCheeseWheel = cheeseWheels.get(index);
    return aCheeseWheel;
  }

  public List<CheeseWheel> getCheeseWheels()
  {
    List<CheeseWheel> newCheeseWheels = Collections.unmodifiableList(cheeseWheels);
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

  public int indexOfCheeseWheel(CheeseWheel aCheeseWheel)
  {
    int index = cheeseWheels.indexOf(aCheeseWheel);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCompany(WholesaleCompany aCompany)
  {
    boolean wasSet = false;
    if (aCompany == null)
    {
      return wasSet;
    }

    WholesaleCompany existingCompany = company;
    company = aCompany;
    if (existingCompany != null && !existingCompany.equals(aCompany))
    {
      existingCompany.removeOrder(this);
    }
    company.addOrder(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCheeseWheels()
  {
    return 0;
  }
  /* Code from template association_AddManyToOptionalOne */
  public boolean addCheeseWheel(CheeseWheel aCheeseWheel)
  {
    boolean wasAdded = false;
    if (cheeseWheels.contains(aCheeseWheel)) { return false; }
    Order existingOrder = aCheeseWheel.getOrder();
    if (existingOrder == null)
    {
      aCheeseWheel.setOrder(this);
    }
    else if (!this.equals(existingOrder))
    {
      existingOrder.removeCheeseWheel(aCheeseWheel);
      addCheeseWheel(aCheeseWheel);
    }
    else
    {
      cheeseWheels.add(aCheeseWheel);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCheeseWheel(CheeseWheel aCheeseWheel)
  {
    boolean wasRemoved = false;
    if (cheeseWheels.contains(aCheeseWheel))
    {
      cheeseWheels.remove(aCheeseWheel);
      aCheeseWheel.setOrder(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCheeseWheelAt(CheeseWheel aCheeseWheel, int index)
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

  public boolean addOrMoveCheeseWheelAt(CheeseWheel aCheeseWheel, int index)
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

  public void delete()
  {
    WholesaleCompany placeholderCompany = company;
    this.company = null;
    if(placeholderCompany != null)
    {
      placeholderCompany.removeOrder(this);
    }
    while( !cheeseWheels.isEmpty() )
    {
      cheeseWheels.get(0).setOrder(null);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "nrCheeseWheels" + ":" + getNrCheeseWheels()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "monthsAged" + "=" + (getMonthsAged() != null ? !getMonthsAged().equals(this)  ? getMonthsAged().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "deliveryDate" + "=" + (getDeliveryDate() != null ? !getDeliveryDate().equals(this)  ? getDeliveryDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "company = "+(getCompany()!=null?Integer.toHexString(System.identityHashCode(getCompany())):"null");
  }
}