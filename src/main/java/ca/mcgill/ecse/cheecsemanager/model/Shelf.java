/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.cheecsemanager.model;
import java.util.*;

// line 41 "../../../../../CheECSEManagerPersistence.ump"
// line 42 "../../../../../CheECSEManager.ump"
public class Shelf
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Shelf> shelfsById = new HashMap<String, Shelf>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Shelf Attributes
  private String id;

  //Shelf Associations
  private List<ShelfLocation> locations;
  private CheECSEManager cheECSEManager;
  private Robot robot;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Shelf(String aId, CheECSEManager aCheECSEManager)
  {
    if (!setId(aId))
    {
      throw new RuntimeException("Cannot create due to duplicate id. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    locations = new ArrayList<ShelfLocation>();
    boolean didAddCheECSEManager = setCheECSEManager(aCheECSEManager);
    if (!didAddCheECSEManager)
    {
      throw new RuntimeException("Unable to create shelve due to cheECSEManager. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(String aId)
  {
    boolean wasSet = false;
    String anOldId = getId();
    if (anOldId != null && anOldId.equals(aId)) {
      return true;
    }
    if (hasWithId(aId)) {
      return wasSet;
    }
    id = aId;
    wasSet = true;
    if (anOldId != null) {
      shelfsById.remove(anOldId);
    }
    shelfsById.put(aId, this);
    return wasSet;
  }

  public String getId()
  {
    return id;
  }
  /* Code from template attribute_GetUnique */
  public static Shelf getWithId(String aId)
  {
    return shelfsById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(String aId)
  {
    return getWithId(aId) != null;
  }
  /* Code from template association_GetMany */
  public ShelfLocation getLocation(int index)
  {
    ShelfLocation aLocation = locations.get(index);
    return aLocation;
  }

  public List<ShelfLocation> getLocations()
  {
    List<ShelfLocation> newLocations = Collections.unmodifiableList(locations);
    return newLocations;
  }

  public int numberOfLocations()
  {
    int number = locations.size();
    return number;
  }

  public boolean hasLocations()
  {
    boolean has = locations.size() > 0;
    return has;
  }

  public int indexOfLocation(ShelfLocation aLocation)
  {
    int index = locations.indexOf(aLocation);
    return index;
  }
  /* Code from template association_GetOne */
  public CheECSEManager getCheECSEManager()
  {
    return cheECSEManager;
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLocations()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ShelfLocation addLocation(int aColumn, int aRow)
  {
    return new ShelfLocation(aColumn, aRow, this);
  }

  public boolean addLocation(ShelfLocation aLocation)
  {
    boolean wasAdded = false;
    if (locations.contains(aLocation)) { return false; }
    Shelf existingShelf = aLocation.getShelf();
    boolean isNewShelf = existingShelf != null && !this.equals(existingShelf);
    if (isNewShelf)
    {
      aLocation.setShelf(this);
    }
    else
    {
      locations.add(aLocation);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeLocation(ShelfLocation aLocation)
  {
    boolean wasRemoved = false;
    //Unable to remove aLocation, as it must always have a shelf
    if (!this.equals(aLocation.getShelf()))
    {
      locations.remove(aLocation);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addLocationAt(ShelfLocation aLocation, int index)
  {  
    boolean wasAdded = false;
    if(addLocation(aLocation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLocations()) { index = numberOfLocations() - 1; }
      locations.remove(aLocation);
      locations.add(index, aLocation);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLocationAt(ShelfLocation aLocation, int index)
  {
    boolean wasAdded = false;
    if(locations.contains(aLocation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLocations()) { index = numberOfLocations() - 1; }
      locations.remove(aLocation);
      locations.add(index, aLocation);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addLocationAt(aLocation, index);
    }
    return wasAdded;
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
      existingCheECSEManager.removeShelve(this);
    }
    cheECSEManager.addShelve(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setRobot(Robot aNewRobot)
  {
    boolean wasSet = false;
    if (aNewRobot == null)
    {
      Robot existingRobot = robot;
      robot = null;
      
      if (existingRobot != null && existingRobot.getCurrentShelf() != null)
      {
        existingRobot.setCurrentShelf(null);
      }
      wasSet = true;
      return wasSet;
    }

    Robot currentRobot = getRobot();
    if (currentRobot != null && !currentRobot.equals(aNewRobot))
    {
      currentRobot.setCurrentShelf(null);
    }

    robot = aNewRobot;
    Shelf existingCurrentShelf = aNewRobot.getCurrentShelf();

    if (!equals(existingCurrentShelf))
    {
      aNewRobot.setCurrentShelf(this);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    shelfsById.remove(getId());
    while (locations.size() > 0)
    {
      ShelfLocation aLocation = locations.get(locations.size() - 1);
      aLocation.delete();
      locations.remove(aLocation);
    }
    
    CheECSEManager placeholderCheECSEManager = cheECSEManager;
    this.cheECSEManager = null;
    if(placeholderCheECSEManager != null)
    {
      placeholderCheECSEManager.removeShelve(this);
    }
    if (robot != null)
    {
      robot.setCurrentShelf(null);
    }
  }


  /**
   * private static Map<String, Shelf> shelfsById = new HashMap<String, Shelf>();
   */
  // line 45 "../../../../../CheECSEManagerPersistence.ump"
   public static  void reinitializeUniqueShelfID(List<Shelf> shelves){
    shelfsById.clear();
      for (var shelf : shelves) {
        shelfsById.put(shelf.getId(), shelf);
      }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "cheECSEManager = "+(getCheECSEManager()!=null?Integer.toHexString(System.identityHashCode(getCheECSEManager())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "robot = "+(getRobot()!=null?Integer.toHexString(System.identityHashCode(getRobot())):"null");
  }
}