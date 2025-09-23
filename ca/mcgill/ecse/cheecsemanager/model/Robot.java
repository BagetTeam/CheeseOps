/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

// line 47 "model.ump"
// line 135 "model.ump"
public class Robot
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Robot Attributes
  private int direction;

  //Robot Associations
  private CheeseManager cheeseManager;
  private ShelfSlot shelfSlot;
  private List<RobotActionLog> robotActionLogs;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Robot(int aDirection, CheeseManager aCheeseManager, ShelfSlot aShelfSlot)
  {
    direction = aDirection;
    if (aCheeseManager == null || aCheeseManager.getRobot() != null)
    {
      throw new RuntimeException("Unable to create Robot due to aCheeseManager. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    cheeseManager = aCheeseManager;
    if (!setShelfSlot(aShelfSlot))
    {
      throw new RuntimeException("Unable to create Robot due to aShelfSlot. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    robotActionLogs = new ArrayList<RobotActionLog>();
  }

  public Robot(int aDirection, ShelfSlot aShelfSlot)
  {
    direction = aDirection;
    cheeseManager = new CheeseManager(this);
    shelfSlot = new ShelfSlot(null);
    robotActionLogs = new ArrayList<RobotActionLog>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDirection(int aDirection)
  {
    boolean wasSet = false;
    direction = aDirection;
    wasSet = true;
    return wasSet;
  }

  public int getDirection()
  {
    return direction;
  }
  /* Code from template association_GetOne */
  public CheeseManager getCheeseManager()
  {
    return cheeseManager;
  }
  /* Code from template association_GetOne */
  public ShelfSlot getShelfSlot()
  {
    return shelfSlot;
  }
  /* Code from template association_GetMany */
  public RobotActionLog getRobotActionLog(int index)
  {
    RobotActionLog aRobotActionLog = robotActionLogs.get(index);
    return aRobotActionLog;
  }

  public List<RobotActionLog> getRobotActionLogs()
  {
    List<RobotActionLog> newRobotActionLogs = Collections.unmodifiableList(robotActionLogs);
    return newRobotActionLogs;
  }

  public int numberOfRobotActionLogs()
  {
    int number = robotActionLogs.size();
    return number;
  }

  public boolean hasRobotActionLogs()
  {
    boolean has = robotActionLogs.size() > 0;
    return has;
  }

  public int indexOfRobotActionLog(RobotActionLog aRobotActionLog)
  {
    int index = robotActionLogs.indexOf(aRobotActionLog);
    return index;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setShelfSlot(ShelfSlot aNewShelfSlot)
  {
    boolean wasSet = false;
    if (aNewShelfSlot != null)
    {
      shelfSlot = aNewShelfSlot;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRobotActionLogs()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public RobotActionLog addRobotActionLog(String aSeverity, String aTitle, String aDetails)
  {
    return new RobotActionLog(aSeverity, aTitle, aDetails, this);
  }

  public boolean addRobotActionLog(RobotActionLog aRobotActionLog)
  {
    boolean wasAdded = false;
    if (robotActionLogs.contains(aRobotActionLog)) { return false; }
    Robot existingRobot = aRobotActionLog.getRobot();
    boolean isNewRobot = existingRobot != null && !this.equals(existingRobot);
    if (isNewRobot)
    {
      aRobotActionLog.setRobot(this);
    }
    else
    {
      robotActionLogs.add(aRobotActionLog);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRobotActionLog(RobotActionLog aRobotActionLog)
  {
    boolean wasRemoved = false;
    //Unable to remove aRobotActionLog, as it must always have a robot
    if (!this.equals(aRobotActionLog.getRobot()))
    {
      robotActionLogs.remove(aRobotActionLog);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addRobotActionLogAt(RobotActionLog aRobotActionLog, int index)
  {  
    boolean wasAdded = false;
    if(addRobotActionLog(aRobotActionLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRobotActionLogs()) { index = numberOfRobotActionLogs() - 1; }
      robotActionLogs.remove(aRobotActionLog);
      robotActionLogs.add(index, aRobotActionLog);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRobotActionLogAt(RobotActionLog aRobotActionLog, int index)
  {
    boolean wasAdded = false;
    if(robotActionLogs.contains(aRobotActionLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRobotActionLogs()) { index = numberOfRobotActionLogs() - 1; }
      robotActionLogs.remove(aRobotActionLog);
      robotActionLogs.add(index, aRobotActionLog);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRobotActionLogAt(aRobotActionLog, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    CheeseManager existingCheeseManager = cheeseManager;
    cheeseManager = null;
    if (existingCheeseManager != null)
    {
      existingCheeseManager.delete();
    }
    shelfSlot = null;
    while (robotActionLogs.size() > 0)
    {
      RobotActionLog aRobotActionLog = robotActionLogs.get(robotActionLogs.size() - 1);
      aRobotActionLog.delete();
      robotActionLogs.remove(aRobotActionLog);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "direction" + ":" + getDirection()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "cheeseManager = "+(getCheeseManager()!=null?Integer.toHexString(System.identityHashCode(getCheeseManager())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "shelfSlot = "+(getShelfSlot()!=null?Integer.toHexString(System.identityHashCode(getShelfSlot())):"null");
  }
}