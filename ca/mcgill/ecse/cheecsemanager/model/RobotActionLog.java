/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/



// line 54 "model.ump"
// line 152 "model.ump"
public class RobotActionLog
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //RobotActionLog Attributes
  private String severity;
  private String title;
  private String details;

  //RobotActionLog Associations
  private Robot robot;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RobotActionLog(String aSeverity, String aTitle, String aDetails, Robot aRobot)
  {
    severity = aSeverity;
    title = aTitle;
    details = aDetails;
    boolean didAddRobot = setRobot(aRobot);
    if (!didAddRobot)
    {
      throw new RuntimeException("Unable to create actionLog due to robot. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setSeverity(String aSeverity)
  {
    boolean wasSet = false;
    severity = aSeverity;
    wasSet = true;
    return wasSet;
  }

  public boolean setTitle(String aTitle)
  {
    boolean wasSet = false;
    title = aTitle;
    wasSet = true;
    return wasSet;
  }

  public boolean setDetails(String aDetails)
  {
    boolean wasSet = false;
    details = aDetails;
    wasSet = true;
    return wasSet;
  }

  public String getSeverity()
  {
    return severity;
  }

  public String getTitle()
  {
    return title;
  }

  public String getDetails()
  {
    return details;
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
      existingRobot.removeActionLog(this);
    }
    robot.addActionLog(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Robot placeholderRobot = robot;
    this.robot = null;
    if(placeholderRobot != null)
    {
      placeholderRobot.removeActionLog(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "severity" + ":" + getSeverity()+ "," +
            "title" + ":" + getTitle()+ "," +
            "details" + ":" + getDetails()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "robot = "+(getRobot()!=null?Integer.toHexString(System.identityHashCode(getRobot())):"null");
  }
}