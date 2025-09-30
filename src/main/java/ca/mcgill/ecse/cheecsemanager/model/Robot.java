/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.model;
import java.util.*;

// line 90 "../../../../../CheECSEManager.ump"
public class Robot
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Robot Attributes
  private boolean isFacingAisle;

  //Robot Associations
  private Shelf currentShelf;
  private CheeseWheel currentCheeseWheel;
  private List<LogEntry> log;
  private CheECSEManager cheECSEManager;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Robot(boolean aIsFacingAisle, CheECSEManager aCheECSEManager)
  {
    isFacingAisle = aIsFacingAisle;
    log = new ArrayList<LogEntry>();
    boolean didAddCheECSEManager = setCheECSEManager(aCheECSEManager);
    if (!didAddCheECSEManager)
    {
      throw new RuntimeException("Unable to create robot due to cheECSEManager. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setIsFacingAisle(boolean aIsFacingAisle)
  {
    boolean wasSet = false;
    isFacingAisle = aIsFacingAisle;
    wasSet = true;
    return wasSet;
  }

  public boolean getIsFacingAisle()
  {
    return isFacingAisle;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsFacingAisle()
  {
    return isFacingAisle;
  }
  /* Code from template association_GetOne */
  public Shelf getCurrentShelf()
  {
    return currentShelf;
  }

  public boolean hasCurrentShelf()
  {
    boolean has = currentShelf != null;
    return has;
  }
  /* Code from template association_GetOne */
  public CheeseWheel getCurrentCheeseWheel()
  {
    return currentCheeseWheel;
  }

  public boolean hasCurrentCheeseWheel()
  {
    boolean has = currentCheeseWheel != null;
    return has;
  }
  /* Code from template association_GetMany */
  public LogEntry getLog(int index)
  {
    LogEntry aLog = log.get(index);
    return aLog;
  }

  public List<LogEntry> getLog()
  {
    List<LogEntry> newLog = Collections.unmodifiableList(log);
    return newLog;
  }

  public int numberOfLog()
  {
    int number = log.size();
    return number;
  }

  public boolean hasLog()
  {
    boolean has = log.size() > 0;
    return has;
  }

  public int indexOfLog(LogEntry aLog)
  {
    int index = log.indexOf(aLog);
    return index;
  }
  /* Code from template association_GetOne */
  public CheECSEManager getCheECSEManager()
  {
    return cheECSEManager;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setCurrentShelf(Shelf aNewCurrentShelf)
  {
    boolean wasSet = false;
    if (aNewCurrentShelf == null)
    {
      Shelf existingCurrentShelf = currentShelf;
      currentShelf = null;
      
      if (existingCurrentShelf != null && existingCurrentShelf.getRobot() != null)
      {
        existingCurrentShelf.setRobot(null);
      }
      wasSet = true;
      return wasSet;
    }

    Shelf currentCurrentShelf = getCurrentShelf();
    if (currentCurrentShelf != null && !currentCurrentShelf.equals(aNewCurrentShelf))
    {
      currentCurrentShelf.setRobot(null);
    }

    currentShelf = aNewCurrentShelf;
    Robot existingRobot = aNewCurrentShelf.getRobot();

    if (!equals(existingRobot))
    {
      aNewCurrentShelf.setRobot(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setCurrentCheeseWheel(CheeseWheel aNewCurrentCheeseWheel)
  {
    boolean wasSet = false;
    if (aNewCurrentCheeseWheel == null)
    {
      CheeseWheel existingCurrentCheeseWheel = currentCheeseWheel;
      currentCheeseWheel = null;
      
      if (existingCurrentCheeseWheel != null && existingCurrentCheeseWheel.getRobot() != null)
      {
        existingCurrentCheeseWheel.setRobot(null);
      }
      wasSet = true;
      return wasSet;
    }

    CheeseWheel currentCurrentCheeseWheel = getCurrentCheeseWheel();
    if (currentCurrentCheeseWheel != null && !currentCurrentCheeseWheel.equals(aNewCurrentCheeseWheel))
    {
      currentCurrentCheeseWheel.setRobot(null);
    }

    currentCheeseWheel = aNewCurrentCheeseWheel;
    Robot existingRobot = aNewCurrentCheeseWheel.getRobot();

    if (!equals(existingRobot))
    {
      aNewCurrentCheeseWheel.setRobot(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLog()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public LogEntry addLog(String aDescription)
  {
    return new LogEntry(aDescription, this);
  }

  public boolean addLog(LogEntry aLog)
  {
    boolean wasAdded = false;
    if (log.contains(aLog)) { return false; }
    Robot existingRobot = aLog.getRobot();
    boolean isNewRobot = existingRobot != null && !this.equals(existingRobot);
    if (isNewRobot)
    {
      aLog.setRobot(this);
    }
    else
    {
      log.add(aLog);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeLog(LogEntry aLog)
  {
    boolean wasRemoved = false;
    //Unable to remove aLog, as it must always have a robot
    if (!this.equals(aLog.getRobot()))
    {
      log.remove(aLog);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addLogAt(LogEntry aLog, int index)
  {  
    boolean wasAdded = false;
    if(addLog(aLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLog()) { index = numberOfLog() - 1; }
      log.remove(aLog);
      log.add(index, aLog);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLogAt(LogEntry aLog, int index)
  {
    boolean wasAdded = false;
    if(log.contains(aLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLog()) { index = numberOfLog() - 1; }
      log.remove(aLog);
      log.add(index, aLog);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addLogAt(aLog, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setCheECSEManager(CheECSEManager aNewCheECSEManager)
  {
    boolean wasSet = false;
    if (aNewCheECSEManager == null)
    {
      //Unable to setCheECSEManager to null, as robot must always be associated to a cheECSEManager
      return wasSet;
    }
    
    Robot existingRobot = aNewCheECSEManager.getRobot();
    if (existingRobot != null && !equals(existingRobot))
    {
      //Unable to setCheECSEManager, the current cheECSEManager already has a robot, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    CheECSEManager anOldCheECSEManager = cheECSEManager;
    cheECSEManager = aNewCheECSEManager;
    cheECSEManager.setRobot(this);

    if (anOldCheECSEManager != null)
    {
      anOldCheECSEManager.setRobot(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    if (currentShelf != null)
    {
      currentShelf.setRobot(null);
    }
    if (currentCheeseWheel != null)
    {
      currentCheeseWheel.setRobot(null);
    }
    while (log.size() > 0)
    {
      LogEntry aLog = log.get(log.size() - 1);
      aLog.delete();
      log.remove(aLog);
    }
    
    CheECSEManager existingCheECSEManager = cheECSEManager;
    cheECSEManager = null;
    if (existingCheECSEManager != null)
    {
      existingCheECSEManager.setRobot(null);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "isFacingAisle" + ":" + getIsFacingAisle()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "currentShelf = "+(getCurrentShelf()!=null?Integer.toHexString(System.identityHashCode(getCurrentShelf())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "currentCheeseWheel = "+(getCurrentCheeseWheel()!=null?Integer.toHexString(System.identityHashCode(getCurrentCheeseWheel())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "cheECSEManager = "+(getCheECSEManager()!=null?Integer.toHexString(System.identityHashCode(getCheECSEManager())):"null");
  }
}