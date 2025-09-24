/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

// line 87 "model.ump"
// line 171 "model.ump"
public class CheECSEManager
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //CheECSEManager Associations
  private List<User> users;
  private Robot robot;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public CheECSEManager(Robot aRobot)
  {
    users = new ArrayList<User>();
    if (aRobot == null || aRobot.getApplication() != null)
    {
      throw new RuntimeException("Unable to create CheECSEManager due to aRobot. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    robot = aRobot;
  }

  public CheECSEManager(int aDirectionForRobot, ShelfSlot aDesiredSlotForRobot)
  {
    users = new ArrayList<User>();
    robot = new Robot(aDirectionForRobot, this, aDesiredSlotForRobot);
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public User getUser(int index)
  {
    User aUser = users.get(index);
    return aUser;
  }

  public List<User> getUsers()
  {
    List<User> newUsers = Collections.unmodifiableList(users);
    return newUsers;
  }

  public int numberOfUsers()
  {
    int number = users.size();
    return number;
  }

  public boolean hasUsers()
  {
    boolean has = users.size() > 0;
    return has;
  }

  public int indexOfUser(User aUser)
  {
    int index = users.indexOf(aUser);
    return index;
  }
  /* Code from template association_GetOne */
  public Robot getRobot()
  {
    return robot;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfUsers()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addUser(User aUser)
  {
    boolean wasAdded = false;
    if (users.contains(aUser)) { return false; }
    users.add(aUser);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeUser(User aUser)
  {
    boolean wasRemoved = false;
    if (users.contains(aUser))
    {
      users.remove(aUser);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addUserAt(User aUser, int index)
  {  
    boolean wasAdded = false;
    if(addUser(aUser))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
      users.remove(aUser);
      users.add(index, aUser);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveUserAt(User aUser, int index)
  {
    boolean wasAdded = false;
    if(users.contains(aUser))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
      users.remove(aUser);
      users.add(index, aUser);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addUserAt(aUser, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    users.clear();
    Robot existingRobot = robot;
    robot = null;
    if (existingRobot != null)
    {
      existingRobot.delete();
    }
  }

}