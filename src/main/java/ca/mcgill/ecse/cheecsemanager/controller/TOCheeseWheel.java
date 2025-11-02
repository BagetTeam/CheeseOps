/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.controller;
import java.sql.Date;

// line 34 "../../../../../CheECSEManagerTransferObjects.ump"
public class TOCheeseWheel
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOCheeseWheel Attributes
  private int id;
  private String monthsAged;
  private boolean isSpoiled;
  private Date purchaseDate;
  private String shelfID;
  private int column;
  private int row;
  private boolean isOrdered;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOCheeseWheel(int aId, String aMonthsAged, boolean aIsSpoiled, Date aPurchaseDate, String aShelfID, int aColumn, int aRow, boolean aIsOrdered)
  {
    id = aId;
    monthsAged = aMonthsAged;
    isSpoiled = aIsSpoiled;
    purchaseDate = aPurchaseDate;
    shelfID = aShelfID;
    column = aColumn;
    row = aRow;
    isOrdered = aIsOrdered;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setMonthsAged(String aMonthsAged)
  {
    boolean wasSet = false;
    monthsAged = aMonthsAged;
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

  public boolean setPurchaseDate(Date aPurchaseDate)
  {
    boolean wasSet = false;
    purchaseDate = aPurchaseDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setShelfID(String aShelfID)
  {
    boolean wasSet = false;
    shelfID = aShelfID;
    wasSet = true;
    return wasSet;
  }

  public boolean setColumn(int aColumn)
  {
    boolean wasSet = false;
    column = aColumn;
    wasSet = true;
    return wasSet;
  }

  public boolean setRow(int aRow)
  {
    boolean wasSet = false;
    row = aRow;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsOrdered(boolean aIsOrdered)
  {
    boolean wasSet = false;
    isOrdered = aIsOrdered;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }

  public String getMonthsAged()
  {
    return monthsAged;
  }

  public boolean getIsSpoiled()
  {
    return isSpoiled;
  }

  public Date getPurchaseDate()
  {
    return purchaseDate;
  }

  public String getShelfID()
  {
    return shelfID;
  }

  /**
   * -1 if cheese wheel is not assigned to shelf
   */
  public int getColumn()
  {
    return column;
  }

  /**
   * -1 if cheese wheel is not assigned to shelf
   */
  public int getRow()
  {
    return row;
  }

  public boolean getIsOrdered()
  {
    return isOrdered;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsSpoiled()
  {
    return isSpoiled;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsOrdered()
  {
    return isOrdered;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "monthsAged" + ":" + getMonthsAged()+ "," +
            "isSpoiled" + ":" + getIsSpoiled()+ "," +
            "shelfID" + ":" + getShelfID()+ "," +
            "column" + ":" + getColumn()+ "," +
            "row" + ":" + getRow()+ "," +
            "isOrdered" + ":" + getIsOrdered()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "purchaseDate" + "=" + (getPurchaseDate() != null ? !getPurchaseDate().equals(this)  ? getPurchaseDate().toString().replaceAll("  ","    ") : "this" : "null");
  }
}