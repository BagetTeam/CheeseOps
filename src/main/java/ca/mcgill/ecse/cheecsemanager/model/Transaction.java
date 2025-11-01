/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.model;
import java.sql.Date;

// line 69 "../../../../../../CheECSEManager.ump"
// line 146 "../../../../../../CheECSEManager.ump"
public abstract class Transaction
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Transaction Attributes
  private Date transactionDate;

  //Autounique Attributes
  private int id;

  //Transaction Associations
  private CheECSEManager cheECSEManager;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Transaction(Date aTransactionDate, CheECSEManager aCheECSEManager)
  {
    transactionDate = aTransactionDate;
    id = nextId++;
    boolean didAddCheECSEManager = setCheECSEManager(aCheECSEManager);
    if (!didAddCheECSEManager)
    {
      throw new RuntimeException("Unable to create transaction due to cheECSEManager. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTransactionDate(Date aTransactionDate)
  {
    boolean wasSet = false;
    transactionDate = aTransactionDate;
    wasSet = true;
    return wasSet;
  }

  public Date getTransactionDate()
  {
    return transactionDate;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template association_GetOne */
  public CheECSEManager getCheECSEManager()
  {
    return cheECSEManager;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCheECSEManager(CheECSEManager aCheECSEManager)
  {
    boolean wasSet = false;
    if (aCheECSEManager == null)
    {
      return wasSet;
    }

    CheECSEManager existingCheECSEManager = cheECSEManager;
    cheECSEManager = aCheECSEManager;
    if (existingCheECSEManager != null && !existingCheECSEManager.equals(aCheECSEManager))
    {
      existingCheECSEManager.removeTransaction(this);
    }
    cheECSEManager.addTransaction(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    CheECSEManager placeholderCheECSEManager = cheECSEManager;
    this.cheECSEManager = null;
    if(placeholderCheECSEManager != null)
    {
      placeholderCheECSEManager.removeTransaction(this);
    }
  }

  // line 75 "../../../../../../CheECSEManager.ump"
   public static  void resetId(){
    nextId = 1;
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "transactionDate" + "=" + (getTransactionDate() != null ? !getTransactionDate().equals(this)  ? getTransactionDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "cheECSEManager = "+(getCheECSEManager()!=null?Integer.toHexString(System.identityHashCode(getCheECSEManager())):"null");
  }
}