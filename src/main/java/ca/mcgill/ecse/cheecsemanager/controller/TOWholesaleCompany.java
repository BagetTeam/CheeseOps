/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.controller;
import java.sql.Date;
import java.util.*;

// line 14 "../../../../../CheECSEManagerTransferObjects.ump"
public class TOWholesaleCompany
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOWholesaleCompany Attributes
  private String name;
  private String address;
  private List<Date> orderDates;
  private List<String> monthsAgeds;
  private List<Integer> nrCheeseWheelsOrdereds;
  private List<Integer> nrCheeseWheelsMissings;
  private List<Date> deliveryDates;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOWholesaleCompany(String aName, String aAddress)
  {
    name = aName;
    address = aAddress;
    orderDates = new ArrayList<Date>();
    monthsAgeds = new ArrayList<String>();
    nrCheeseWheelsOrdereds = new ArrayList<Integer>();
    nrCheeseWheelsMissings = new ArrayList<Integer>();
    deliveryDates = new ArrayList<Date>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setAddress(String aAddress)
  {
    boolean wasSet = false;
    address = aAddress;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetMany */
  public boolean addOrderDate(Date aOrderDate)
  {
    boolean wasAdded = false;
    wasAdded = orderDates.add(aOrderDate);
    return wasAdded;
  }

  public boolean removeOrderDate(Date aOrderDate)
  {
    boolean wasRemoved = false;
    wasRemoved = orderDates.remove(aOrderDate);
    return wasRemoved;
  }
  /* Code from template attribute_SetMany */
  public boolean addMonthsAged(String aMonthsAged)
  {
    boolean wasAdded = false;
    wasAdded = monthsAgeds.add(aMonthsAged);
    return wasAdded;
  }

  public boolean removeMonthsAged(String aMonthsAged)
  {
    boolean wasRemoved = false;
    wasRemoved = monthsAgeds.remove(aMonthsAged);
    return wasRemoved;
  }
  /* Code from template attribute_SetMany */
  public boolean addNrCheeseWheelsOrdered(Integer aNrCheeseWheelsOrdered)
  {
    boolean wasAdded = false;
    wasAdded = nrCheeseWheelsOrdereds.add(aNrCheeseWheelsOrdered);
    return wasAdded;
  }

  public boolean removeNrCheeseWheelsOrdered(Integer aNrCheeseWheelsOrdered)
  {
    boolean wasRemoved = false;
    wasRemoved = nrCheeseWheelsOrdereds.remove(aNrCheeseWheelsOrdered);
    return wasRemoved;
  }
  /* Code from template attribute_SetMany */
  public boolean addNrCheeseWheelsMissing(Integer aNrCheeseWheelsMissing)
  {
    boolean wasAdded = false;
    wasAdded = nrCheeseWheelsMissings.add(aNrCheeseWheelsMissing);
    return wasAdded;
  }

  public boolean removeNrCheeseWheelsMissing(Integer aNrCheeseWheelsMissing)
  {
    boolean wasRemoved = false;
    wasRemoved = nrCheeseWheelsMissings.remove(aNrCheeseWheelsMissing);
    return wasRemoved;
  }
  /* Code from template attribute_SetMany */
  public boolean addDeliveryDate(Date aDeliveryDate)
  {
    boolean wasAdded = false;
    wasAdded = deliveryDates.add(aDeliveryDate);
    return wasAdded;
  }

  public boolean removeDeliveryDate(Date aDeliveryDate)
  {
    boolean wasRemoved = false;
    wasRemoved = deliveryDates.remove(aDeliveryDate);
    return wasRemoved;
  }

  public String getName()
  {
    return name;
  }

  public String getAddress()
  {
    return address;
  }
  /* Code from template attribute_GetMany */
  public Date getOrderDate(int index)
  {
    Date aOrderDate = orderDates.get(index);
    return aOrderDate;
  }

  public Date[] getOrderDates()
  {
    Date[] newOrderDates = orderDates.toArray(new Date[orderDates.size()]);
    return newOrderDates;
  }

  public int numberOfOrderDates()
  {
    int number = orderDates.size();
    return number;
  }

  public boolean hasOrderDates()
  {
    boolean has = orderDates.size() > 0;
    return has;
  }

  public int indexOfOrderDate(Date aOrderDate)
  {
    int index = orderDates.indexOf(aOrderDate);
    return index;
  }
  /* Code from template attribute_GetMany */
  public String getMonthsAged(int index)
  {
    String aMonthsAged = monthsAgeds.get(index);
    return aMonthsAged;
  }

  public String[] getMonthsAgeds()
  {
    String[] newMonthsAgeds = monthsAgeds.toArray(new String[monthsAgeds.size()]);
    return newMonthsAgeds;
  }

  public int numberOfMonthsAgeds()
  {
    int number = monthsAgeds.size();
    return number;
  }

  public boolean hasMonthsAgeds()
  {
    boolean has = monthsAgeds.size() > 0;
    return has;
  }

  public int indexOfMonthsAged(String aMonthsAged)
  {
    int index = monthsAgeds.indexOf(aMonthsAged);
    return index;
  }
  /* Code from template attribute_GetMany */
  public Integer getNrCheeseWheelsOrdered(int index)
  {
    Integer aNrCheeseWheelsOrdered = nrCheeseWheelsOrdereds.get(index);
    return aNrCheeseWheelsOrdered;
  }

  public Integer[] getNrCheeseWheelsOrdereds()
  {
    Integer[] newNrCheeseWheelsOrdereds = nrCheeseWheelsOrdereds.toArray(new Integer[nrCheeseWheelsOrdereds.size()]);
    return newNrCheeseWheelsOrdereds;
  }

  public int numberOfNrCheeseWheelsOrdereds()
  {
    int number = nrCheeseWheelsOrdereds.size();
    return number;
  }

  public boolean hasNrCheeseWheelsOrdereds()
  {
    boolean has = nrCheeseWheelsOrdereds.size() > 0;
    return has;
  }

  public int indexOfNrCheeseWheelsOrdered(Integer aNrCheeseWheelsOrdered)
  {
    int index = nrCheeseWheelsOrdereds.indexOf(aNrCheeseWheelsOrdered);
    return index;
  }
  /* Code from template attribute_GetMany */
  public Integer getNrCheeseWheelsMissing(int index)
  {
    Integer aNrCheeseWheelsMissing = nrCheeseWheelsMissings.get(index);
    return aNrCheeseWheelsMissing;
  }

  public Integer[] getNrCheeseWheelsMissings()
  {
    Integer[] newNrCheeseWheelsMissings = nrCheeseWheelsMissings.toArray(new Integer[nrCheeseWheelsMissings.size()]);
    return newNrCheeseWheelsMissings;
  }

  public int numberOfNrCheeseWheelsMissings()
  {
    int number = nrCheeseWheelsMissings.size();
    return number;
  }

  public boolean hasNrCheeseWheelsMissings()
  {
    boolean has = nrCheeseWheelsMissings.size() > 0;
    return has;
  }

  public int indexOfNrCheeseWheelsMissing(Integer aNrCheeseWheelsMissing)
  {
    int index = nrCheeseWheelsMissings.indexOf(aNrCheeseWheelsMissing);
    return index;
  }
  /* Code from template attribute_GetMany */
  public Date getDeliveryDate(int index)
  {
    Date aDeliveryDate = deliveryDates.get(index);
    return aDeliveryDate;
  }

  public Date[] getDeliveryDates()
  {
    Date[] newDeliveryDates = deliveryDates.toArray(new Date[deliveryDates.size()]);
    return newDeliveryDates;
  }

  public int numberOfDeliveryDates()
  {
    int number = deliveryDates.size();
    return number;
  }

  public boolean hasDeliveryDates()
  {
    boolean has = deliveryDates.size() > 0;
    return has;
  }

  public int indexOfDeliveryDate(Date aDeliveryDate)
  {
    int index = deliveryDates.indexOf(aDeliveryDate);
    return index;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "address" + ":" + getAddress()+ "]";
  }
}