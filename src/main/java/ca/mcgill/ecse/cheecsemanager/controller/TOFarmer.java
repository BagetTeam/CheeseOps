/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.controller;
import java.util.*;
import java.sql.Date;

// line 3 "../../../../../CheECSEManagerTransferObjects.ump"
public class TOFarmer
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOFarmer Attributes
  private String email;
  private String password;
  private String name;
  private String address;
  private List<Integer> cheeseWheelIDs;
  private List<Date> purchaseDates;
  private List<String> monthsAgeds;
  private List<String> isSpoileds;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOFarmer(String aEmail, String aPassword, String aName, String aAddress)
  {
    email = aEmail;
    password = aPassword;
    name = aName;
    address = aAddress;
    cheeseWheelIDs = new ArrayList<Integer>();
    purchaseDates = new ArrayList<Date>();
    monthsAgeds = new ArrayList<String>();
    isSpoileds = new ArrayList<String>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

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
  public boolean addCheeseWheelID(Integer aCheeseWheelID)
  {
    boolean wasAdded = false;
    wasAdded = cheeseWheelIDs.add(aCheeseWheelID);
    return wasAdded;
  }

  public boolean removeCheeseWheelID(Integer aCheeseWheelID)
  {
    boolean wasRemoved = false;
    wasRemoved = cheeseWheelIDs.remove(aCheeseWheelID);
    return wasRemoved;
  }
  /* Code from template attribute_SetMany */
  public boolean addPurchaseDate(Date aPurchaseDate)
  {
    boolean wasAdded = false;
    wasAdded = purchaseDates.add(aPurchaseDate);
    return wasAdded;
  }

  public boolean removePurchaseDate(Date aPurchaseDate)
  {
    boolean wasRemoved = false;
    wasRemoved = purchaseDates.remove(aPurchaseDate);
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
  public boolean addIsSpoiled(String aIsSpoiled)
  {
    boolean wasAdded = false;
    wasAdded = isSpoileds.add(aIsSpoiled);
    return wasAdded;
  }

  public boolean removeIsSpoiled(String aIsSpoiled)
  {
    boolean wasRemoved = false;
    wasRemoved = isSpoileds.remove(aIsSpoiled);
    return wasRemoved;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPassword()
  {
    return password;
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
  public Integer getCheeseWheelID(int index)
  {
    Integer aCheeseWheelID = cheeseWheelIDs.get(index);
    return aCheeseWheelID;
  }

  public Integer[] getCheeseWheelIDs()
  {
    Integer[] newCheeseWheelIDs = cheeseWheelIDs.toArray(new Integer[cheeseWheelIDs.size()]);
    return newCheeseWheelIDs;
  }

  public int numberOfCheeseWheelIDs()
  {
    int number = cheeseWheelIDs.size();
    return number;
  }

  public boolean hasCheeseWheelIDs()
  {
    boolean has = cheeseWheelIDs.size() > 0;
    return has;
  }

  public int indexOfCheeseWheelID(Integer aCheeseWheelID)
  {
    int index = cheeseWheelIDs.indexOf(aCheeseWheelID);
    return index;
  }
  /* Code from template attribute_GetMany */
  public Date getPurchaseDate(int index)
  {
    Date aPurchaseDate = purchaseDates.get(index);
    return aPurchaseDate;
  }

  public Date[] getPurchaseDates()
  {
    Date[] newPurchaseDates = purchaseDates.toArray(new Date[purchaseDates.size()]);
    return newPurchaseDates;
  }

  public int numberOfPurchaseDates()
  {
    int number = purchaseDates.size();
    return number;
  }

  public boolean hasPurchaseDates()
  {
    boolean has = purchaseDates.size() > 0;
    return has;
  }

  public int indexOfPurchaseDate(Date aPurchaseDate)
  {
    int index = purchaseDates.indexOf(aPurchaseDate);
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
  public String getIsSpoiled(int index)
  {
    String aIsSpoiled = isSpoileds.get(index);
    return aIsSpoiled;
  }

  public String[] getIsSpoileds()
  {
    String[] newIsSpoileds = isSpoileds.toArray(new String[isSpoileds.size()]);
    return newIsSpoileds;
  }

  public int numberOfIsSpoileds()
  {
    int number = isSpoileds.size();
    return number;
  }

  public boolean hasIsSpoileds()
  {
    boolean has = isSpoileds.size() > 0;
    return has;
  }

  public int indexOfIsSpoiled(String aIsSpoiled)
  {
    int index = isSpoileds.indexOf(aIsSpoiled);
    return index;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "email" + ":" + getEmail()+ "," +
            "password" + ":" + getPassword()+ "," +
            "name" + ":" + getName()+ "," +
            "address" + ":" + getAddress()+ "]";
  }
}