/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.controller;
import java.util.*;

// line 24 "../../../../../CheECSEManagerTransferObjects.ump"
public class TOShelf
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOShelf Attributes
  private String shelfID;
  private int maxColumns;
  private int maxRows;
  private List<Integer> cheeseWheelIDs;
  private List<Integer> columnNrs;
  private List<Integer> rowNrs;
  private List<String> monthsAgeds;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOShelf(String aShelfID, int aMaxColumns, int aMaxRows)
  {
    shelfID = aShelfID;
    maxColumns = aMaxColumns;
    maxRows = aMaxRows;
    cheeseWheelIDs = new ArrayList<Integer>();
    columnNrs = new ArrayList<Integer>();
    rowNrs = new ArrayList<Integer>();
    monthsAgeds = new ArrayList<String>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setShelfID(String aShelfID)
  {
    boolean wasSet = false;
    shelfID = aShelfID;
    wasSet = true;
    return wasSet;
  }

  public boolean setMaxColumns(int aMaxColumns)
  {
    boolean wasSet = false;
    maxColumns = aMaxColumns;
    wasSet = true;
    return wasSet;
  }

  public boolean setMaxRows(int aMaxRows)
  {
    boolean wasSet = false;
    maxRows = aMaxRows;
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
  public boolean addColumnNr(Integer aColumnNr)
  {
    boolean wasAdded = false;
    wasAdded = columnNrs.add(aColumnNr);
    return wasAdded;
  }

  public boolean removeColumnNr(Integer aColumnNr)
  {
    boolean wasRemoved = false;
    wasRemoved = columnNrs.remove(aColumnNr);
    return wasRemoved;
  }
  /* Code from template attribute_SetMany */
  public boolean addRowNr(Integer aRowNr)
  {
    boolean wasAdded = false;
    wasAdded = rowNrs.add(aRowNr);
    return wasAdded;
  }

  public boolean removeRowNr(Integer aRowNr)
  {
    boolean wasRemoved = false;
    wasRemoved = rowNrs.remove(aRowNr);
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

  public String getShelfID()
  {
    return shelfID;
  }

  public int getMaxColumns()
  {
    return maxColumns;
  }

  public int getMaxRows()
  {
    return maxRows;
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
  public Integer getColumnNr(int index)
  {
    Integer aColumnNr = columnNrs.get(index);
    return aColumnNr;
  }

  public Integer[] getColumnNrs()
  {
    Integer[] newColumnNrs = columnNrs.toArray(new Integer[columnNrs.size()]);
    return newColumnNrs;
  }

  public int numberOfColumnNrs()
  {
    int number = columnNrs.size();
    return number;
  }

  public boolean hasColumnNrs()
  {
    boolean has = columnNrs.size() > 0;
    return has;
  }

  public int indexOfColumnNr(Integer aColumnNr)
  {
    int index = columnNrs.indexOf(aColumnNr);
    return index;
  }
  /* Code from template attribute_GetMany */
  public Integer getRowNr(int index)
  {
    Integer aRowNr = rowNrs.get(index);
    return aRowNr;
  }

  public Integer[] getRowNrs()
  {
    Integer[] newRowNrs = rowNrs.toArray(new Integer[rowNrs.size()]);
    return newRowNrs;
  }

  public int numberOfRowNrs()
  {
    int number = rowNrs.size();
    return number;
  }

  public boolean hasRowNrs()
  {
    boolean has = rowNrs.size() > 0;
    return has;
  }

  public int indexOfRowNr(Integer aRowNr)
  {
    int index = rowNrs.indexOf(aRowNr);
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

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "shelfID" + ":" + getShelfID()+ "," +
            "maxColumns" + ":" + getMaxColumns()+ "," +
            "maxRows" + ":" + getMaxRows()+ "]";
  }
}