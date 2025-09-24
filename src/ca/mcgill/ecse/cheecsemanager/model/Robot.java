/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

// line 52 "model.ump"
// line 139 "model.ump"
public class Robot
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Robot Attributes
  private int direction;

  //Robot Associations
  private CheECSEManager application;
  private ShelfSlot desiredSlot;
  private List<RobotActionLog> actionLogs;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Robot(int aDirection, CheECSEManager aApplication, ShelfSlot aDesiredSlot)
  {
    direction = aDirection;
    if (aApplication == null || aApplication.getRobot() != null)
    {
      throw new RuntimeException("Unable to create Robot due to aApplication. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    application = aApplication;
    if (!setDesiredSlot(aDesiredSlot))
    {
      throw new RuntimeException("Unable to create Robot due to aDesiredSlot. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    actionLogs = new ArrayList<RobotActionLog>();
  }

  public Robot(int aDirection, ShelfSlot aDesiredSlot)
  {
    direction = aDirection;
    application = new CheECSEManager(this);
    desiredSlot = new ShelfSlot(null);
    actionLogs = new ArrayList<RobotActionLog>();
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
  public CheECSEManager getApplication()
  {
    return application;
  }
  /* Code from template association_GetOne */
  public ShelfSlot getDesiredSlot()
  {
    return desiredSlot;
  }
  /* Code from template association_GetMany */
  public RobotActionLog getActionLog(int index)
  {
    RobotActionLog aActionLog = actionLogs.get(index);
    return aActionLog;
  }

  public List<RobotActionLog> getActionLogs()
  {
    List<RobotActionLog> newActionLogs = Collections.unmodifiableList(actionLogs);
    return newActionLogs;
  }

  public int numberOfActionLogs()
  {
    int number = actionLogs.size();
    return number;
  }

  public boolean hasActionLogs()
  {
    boolean has = actionLogs.size() > 0;
    return has;
  }

  public int indexOfActionLog(RobotActionLog aActionLog)
  {
    int index = actionLogs.indexOf(aActionLog);
    return index;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setDesiredSlot(ShelfSlot aNewDesiredSlot)
  {
    boolean wasSet = false;
    if (aNewDesiredSlot != null)
    {
      desiredSlot = aNewDesiredSlot;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfActionLogs()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public RobotActionLog addActionLog(String aSeverity, String aTitle, String aDetails)
  {
    return new RobotActionLog(aSeverity, aTitle, aDetails, this);
  }

  public boolean addActionLog(RobotActionLog aActionLog)
  {
    boolean wasAdded = false;
    if (actionLogs.contains(aActionLog)) { return false; }
    Robot existingRobot = aActionLog.getRobot();
    boolean isNewRobot = existingRobot != null && !this.equals(existingRobot);
    if (isNewRobot)
    {
      aActionLog.setRobot(this);
    }
    else
    {
      actionLogs.add(aActionLog);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeActionLog(RobotActionLog aActionLog)
  {
    boolean wasRemoved = false;
    //Unable to remove aActionLog, as it must always have a robot
    if (!this.equals(aActionLog.getRobot()))
    {
      actionLogs.remove(aActionLog);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addActionLogAt(RobotActionLog aActionLog, int index)
  {  
    boolean wasAdded = false;
    if(addActionLog(aActionLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfActionLogs()) { index = numberOfActionLogs() - 1; }
      actionLogs.remove(aActionLog);
      actionLogs.add(index, aActionLog);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveActionLogAt(RobotActionLog aActionLog, int index)
  {
    boolean wasAdded = false;
    if(actionLogs.contains(aActionLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfActionLogs()) { index = numberOfActionLogs() - 1; }
      actionLogs.remove(aActionLog);
      actionLogs.add(index, aActionLog);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addActionLogAt(aActionLog, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    CheECSEManager existingApplication = application;
    application = null;
    if (existingApplication != null)
    {
      existingApplication.delete();
    }
    desiredSlot = null;
    while (actionLogs.size() > 0)
    {
      RobotActionLog aActionLog = actionLogs.get(actionLogs.size() - 1);
      aActionLog.delete();
      actionLogs.remove(aActionLog);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "direction" + ":" + getDirection()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "application = "+(getApplication()!=null?Integer.toHexString(System.identityHashCode(getApplication())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "desiredSlot = "+(getDesiredSlot()!=null?Integer.toHexString(System.identityHashCode(getDesiredSlot())):"null");
  }
}