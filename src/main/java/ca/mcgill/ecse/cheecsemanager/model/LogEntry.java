/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.model;

// line 99 "../../../../../../CheECSEManager.ump"
// line 166 "../../../../../../CheECSEManager.ump"
public class LogEntry
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //LogEntry Attributes
  private String description;

  //LogEntry Associations
  private Robot robot;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public LogEntry(String aDescription, Robot aRobot)
  {
    description = aDescription;
    boolean didAddRobot = setRobot(aRobot);
    if (!didAddRobot)
    {
      throw new RuntimeException("Unable to create log due to robot. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public String getDescription()
  {
    return description;
  }
  /* Code from template association_GetOne */
  public Robot getRobot()
  {
    return robot;
  }
  /* Code from template association_SetOneToMany */
  public boolean setRobot(Robot aRobot)
  {
    boolean wasSet = false;
    if (aRobot == null)
    {
      return wasSet;
    }

    Robot existingRobot = robot;
    robot = aRobot;
    if (existingRobot != null && !existingRobot.equals(aRobot))
    {
      existingRobot.removeLog(this);
    }
    robot.addLog(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Robot placeholderRobot = robot;
    this.robot = null;
    if(placeholderRobot != null)
    {
      placeholderRobot.removeLog(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "description" + ":" + getDescription()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "robot = "+(getRobot()!=null?Integer.toHexString(System.identityHashCode(getRobot())):"null");
  }
}