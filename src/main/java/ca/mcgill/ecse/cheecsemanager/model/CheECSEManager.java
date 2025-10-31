/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.model;
import java.util.*;
import java.sql.Date;

// line 5 "../../../../../../CheECSEManager.ump"
// line 106 "../../../../../../CheECSEManager.ump"
public class CheECSEManager
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CheECSEManager Associations
  private FacilityManager manager;
  private List<Farmer> farmers;
  private List<Shelf> shelves;
  private List<CheeseWheel> cheeseWheels;
  private List<Transaction> transactions;
  private List<WholesaleCompany> companies;
  private Robot robot;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CheECSEManager()
  {
    farmers = new ArrayList<Farmer>();
    shelves = new ArrayList<Shelf>();
    cheeseWheels = new ArrayList<CheeseWheel>();
    transactions = new ArrayList<Transaction>();
    companies = new ArrayList<WholesaleCompany>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public FacilityManager getManager()
  {
    return manager;
  }

  public boolean hasManager()
  {
    boolean has = manager != null;
    return has;
  }
  /* Code from template association_GetMany */
  public Farmer getFarmer(int index)
  {
    Farmer aFarmer = farmers.get(index);
    return aFarmer;
  }

  public List<Farmer> getFarmers()
  {
    List<Farmer> newFarmers = Collections.unmodifiableList(farmers);
    return newFarmers;
  }

  public int numberOfFarmers()
  {
    int number = farmers.size();
    return number;
  }

  public boolean hasFarmers()
  {
    boolean has = farmers.size() > 0;
    return has;
  }

  public int indexOfFarmer(Farmer aFarmer)
  {
    int index = farmers.indexOf(aFarmer);
    return index;
  }
  /* Code from template association_GetMany */
  public Shelf getShelve(int index)
  {
    Shelf aShelve = shelves.get(index);
    return aShelve;
  }

  public List<Shelf> getShelves()
  {
    List<Shelf> newShelves = Collections.unmodifiableList(shelves);
    return newShelves;
  }

  public int numberOfShelves()
  {
    int number = shelves.size();
    return number;
  }

  public boolean hasShelves()
  {
    boolean has = shelves.size() > 0;
    return has;
  }

  public int indexOfShelve(Shelf aShelve)
  {
    int index = shelves.indexOf(aShelve);
    return index;
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
  /* Code from template association_GetMany */
  public Transaction getTransaction(int index)
  {
    Transaction aTransaction = transactions.get(index);
    return aTransaction;
  }

  public List<Transaction> getTransactions()
  {
    List<Transaction> newTransactions = Collections.unmodifiableList(transactions);
    return newTransactions;
  }

  public int numberOfTransactions()
  {
    int number = transactions.size();
    return number;
  }

  public boolean hasTransactions()
  {
    boolean has = transactions.size() > 0;
    return has;
  }

  public int indexOfTransaction(Transaction aTransaction)
  {
    int index = transactions.indexOf(aTransaction);
    return index;
  }
  /* Code from template association_GetMany */
  public WholesaleCompany getCompany(int index)
  {
    WholesaleCompany aCompany = companies.get(index);
    return aCompany;
  }

  public List<WholesaleCompany> getCompanies()
  {
    List<WholesaleCompany> newCompanies = Collections.unmodifiableList(companies);
    return newCompanies;
  }

  public int numberOfCompanies()
  {
    int number = companies.size();
    return number;
  }

  public boolean hasCompanies()
  {
    boolean has = companies.size() > 0;
    return has;
  }

  public int indexOfCompany(WholesaleCompany aCompany)
  {
    int index = companies.indexOf(aCompany);
    return index;
  }
  /* Code from template association_GetOne */
  public Robot getRobot()
  {
    return robot;
  }

  public boolean hasRobot()
  {
    boolean has = robot != null;
    return has;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setManager(FacilityManager aNewManager)
  {
    boolean wasSet = false;
    if (manager != null && !manager.equals(aNewManager) && equals(manager.getCheECSEManager()))
    {
      //Unable to setManager, as existing manager would become an orphan
      return wasSet;
    }

    manager = aNewManager;
    CheECSEManager anOldCheECSEManager = aNewManager != null ? aNewManager.getCheECSEManager() : null;

    if (!this.equals(anOldCheECSEManager))
    {
      if (anOldCheECSEManager != null)
      {
        anOldCheECSEManager.manager = null;
      }
      if (manager != null)
      {
        manager.setCheECSEManager(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfFarmers()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Farmer addFarmer(String aEmail, String aPassword, String aAddress)
  {
    return new Farmer(aEmail, aPassword, aAddress, this);
  }

  public boolean addFarmer(Farmer aFarmer)
  {
    boolean wasAdded = false;
    if (farmers.contains(aFarmer)) { return false; }
    CheECSEManager existingCheECSEManager = aFarmer.getCheECSEManager();
    boolean isNewCheECSEManager = existingCheECSEManager != null && !this.equals(existingCheECSEManager);
    if (isNewCheECSEManager)
    {
      aFarmer.setCheECSEManager(this);
    }
    else
    {
      farmers.add(aFarmer);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeFarmer(Farmer aFarmer)
  {
    boolean wasRemoved = false;
    //Unable to remove aFarmer, as it must always have a cheECSEManager
    if (!this.equals(aFarmer.getCheECSEManager()))
    {
      farmers.remove(aFarmer);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addFarmerAt(Farmer aFarmer, int index)
  {  
    boolean wasAdded = false;
    if(addFarmer(aFarmer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfFarmers()) { index = numberOfFarmers() - 1; }
      farmers.remove(aFarmer);
      farmers.add(index, aFarmer);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveFarmerAt(Farmer aFarmer, int index)
  {
    boolean wasAdded = false;
    if(farmers.contains(aFarmer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfFarmers()) { index = numberOfFarmers() - 1; }
      farmers.remove(aFarmer);
      farmers.add(index, aFarmer);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addFarmerAt(aFarmer, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfShelves()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Shelf addShelve(String aId)
  {
    return new Shelf(aId, this);
  }

  public boolean addShelve(Shelf aShelve)
  {
    boolean wasAdded = false;
    if (shelves.contains(aShelve)) { return false; }
    CheECSEManager existingCheECSEManager = aShelve.getCheECSEManager();
    boolean isNewCheECSEManager = existingCheECSEManager != null && !this.equals(existingCheECSEManager);
    if (isNewCheECSEManager)
    {
      aShelve.setCheECSEManager(this);
    }
    else
    {
      shelves.add(aShelve);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeShelve(Shelf aShelve)
  {
    boolean wasRemoved = false;
    //Unable to remove aShelve, as it must always have a cheECSEManager
    if (!this.equals(aShelve.getCheECSEManager()))
    {
      shelves.remove(aShelve);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addShelveAt(Shelf aShelve, int index)
  {  
    boolean wasAdded = false;
    if(addShelve(aShelve))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShelves()) { index = numberOfShelves() - 1; }
      shelves.remove(aShelve);
      shelves.add(index, aShelve);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveShelveAt(Shelf aShelve, int index)
  {
    boolean wasAdded = false;
    if(shelves.contains(aShelve))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShelves()) { index = numberOfShelves() - 1; }
      shelves.remove(aShelve);
      shelves.add(index, aShelve);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addShelveAt(aShelve, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCheeseWheels()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public CheeseWheel addCheeseWheel(CheeseWheel.MaturationPeriod aMonthsAged, boolean aIsSpoiled, Purchase aPurchase)
  {
    return new CheeseWheel(aMonthsAged, aIsSpoiled, aPurchase, this);
  }

  public boolean addCheeseWheel(CheeseWheel aCheeseWheel)
  {
    boolean wasAdded = false;
    if (cheeseWheels.contains(aCheeseWheel)) { return false; }
    CheECSEManager existingCheECSEManager = aCheeseWheel.getCheECSEManager();
    boolean isNewCheECSEManager = existingCheECSEManager != null && !this.equals(existingCheECSEManager);
    if (isNewCheECSEManager)
    {
      aCheeseWheel.setCheECSEManager(this);
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
    //Unable to remove aCheeseWheel, as it must always have a cheECSEManager
    if (!this.equals(aCheeseWheel.getCheECSEManager()))
    {
      cheeseWheels.remove(aCheeseWheel);
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTransactions()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */


  public boolean addTransaction(Transaction aTransaction)
  {
    boolean wasAdded = false;
    if (transactions.contains(aTransaction)) { return false; }
    CheECSEManager existingCheECSEManager = aTransaction.getCheECSEManager();
    boolean isNewCheECSEManager = existingCheECSEManager != null && !this.equals(existingCheECSEManager);
    if (isNewCheECSEManager)
    {
      aTransaction.setCheECSEManager(this);
    }
    else
    {
      transactions.add(aTransaction);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTransaction(Transaction aTransaction)
  {
    boolean wasRemoved = false;
    //Unable to remove aTransaction, as it must always have a cheECSEManager
    if (!this.equals(aTransaction.getCheECSEManager()))
    {
      transactions.remove(aTransaction);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addTransactionAt(Transaction aTransaction, int index)
  {  
    boolean wasAdded = false;
    if(addTransaction(aTransaction))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTransactions()) { index = numberOfTransactions() - 1; }
      transactions.remove(aTransaction);
      transactions.add(index, aTransaction);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTransactionAt(Transaction aTransaction, int index)
  {
    boolean wasAdded = false;
    if(transactions.contains(aTransaction))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTransactions()) { index = numberOfTransactions() - 1; }
      transactions.remove(aTransaction);
      transactions.add(index, aTransaction);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTransactionAt(aTransaction, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCompanies()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public WholesaleCompany addCompany(String aName, String aAddress)
  {
    return new WholesaleCompany(aName, aAddress, this);
  }

  public boolean addCompany(WholesaleCompany aCompany)
  {
    boolean wasAdded = false;
    if (companies.contains(aCompany)) { return false; }
    CheECSEManager existingCheECSEManager = aCompany.getCheECSEManager();
    boolean isNewCheECSEManager = existingCheECSEManager != null && !this.equals(existingCheECSEManager);
    if (isNewCheECSEManager)
    {
      aCompany.setCheECSEManager(this);
    }
    else
    {
      companies.add(aCompany);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCompany(WholesaleCompany aCompany)
  {
    boolean wasRemoved = false;
    //Unable to remove aCompany, as it must always have a cheECSEManager
    if (!this.equals(aCompany.getCheECSEManager()))
    {
      companies.remove(aCompany);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCompanyAt(WholesaleCompany aCompany, int index)
  {  
    boolean wasAdded = false;
    if(addCompany(aCompany))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCompanies()) { index = numberOfCompanies() - 1; }
      companies.remove(aCompany);
      companies.add(index, aCompany);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCompanyAt(WholesaleCompany aCompany, int index)
  {
    boolean wasAdded = false;
    if(companies.contains(aCompany))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCompanies()) { index = numberOfCompanies() - 1; }
      companies.remove(aCompany);
      companies.add(index, aCompany);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCompanyAt(aCompany, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setRobot(Robot aNewRobot)
  {
    boolean wasSet = false;
    if (robot != null && !robot.equals(aNewRobot) && equals(robot.getCheECSEManager()))
    {
      //Unable to setRobot, as existing robot would become an orphan
      return wasSet;
    }

    robot = aNewRobot;
    CheECSEManager anOldCheECSEManager = aNewRobot != null ? aNewRobot.getCheECSEManager() : null;

    if (!this.equals(anOldCheECSEManager))
    {
      if (anOldCheECSEManager != null)
      {
        anOldCheECSEManager.robot = null;
      }
      if (robot != null)
      {
        robot.setCheECSEManager(this);
      }
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    FacilityManager existingManager = manager;
    manager = null;
    if (existingManager != null)
    {
      existingManager.delete();
      existingManager.setCheECSEManager(null);
    }
    while (farmers.size() > 0)
    {
      Farmer aFarmer = farmers.get(farmers.size() - 1);
      aFarmer.delete();
      farmers.remove(aFarmer);
    }
    
    while (shelves.size() > 0)
    {
      Shelf aShelve = shelves.get(shelves.size() - 1);
      aShelve.delete();
      shelves.remove(aShelve);
    }
    
    while (cheeseWheels.size() > 0)
    {
      CheeseWheel aCheeseWheel = cheeseWheels.get(cheeseWheels.size() - 1);
      aCheeseWheel.delete();
      cheeseWheels.remove(aCheeseWheel);
    }
    
    while (transactions.size() > 0)
    {
      Transaction aTransaction = transactions.get(transactions.size() - 1);
      aTransaction.delete();
      transactions.remove(aTransaction);
    }
    
    while (companies.size() > 0)
    {
      WholesaleCompany aCompany = companies.get(companies.size() - 1);
      aCompany.delete();
      companies.remove(aCompany);
    }
    
    Robot existingRobot = robot;
    robot = null;
    if (existingRobot != null)
    {
      existingRobot.delete();
      existingRobot.setCheECSEManager(null);
    }
  }

}