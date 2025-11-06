/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling
 * language!*/

package ca.mcgill.ecse.cheecsemanager.model;
import java.util.*;

// line 1 "../../../../../../Robot.ump"
// line 109 "../../../../../../Robot.ump"
// line 93 "../../../../../../model.ump"
// line 163 "../../../../../../model.ump"
public class Robot {

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  // Robot Attributes
  private boolean isActivated;
  private int directionInDegrees;
  private int row;
  private int column;
  private Purchase currentPurchaseTreated;
  private boolean isFacingAisle;

  // Robot State Machines
  public enum Status {
    Idle,
    AtEntranceNotFacingAisle,
    AtEntranceFacingAisle,
    AtCheeseWheel
  }
  private Status status;

  // Robot Associations
  private Shelf currentShelf;
  private CheeseWheel currentCheeseWheel;
  private List<LogEntry> log;
  private CheECSEManager cheECSEManager;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Robot(Purchase aCurrentPurchaseTreated, boolean aIsFacingAisle,
               CheECSEManager aCheECSEManager) {
    isActivated = false;
    directionInDegrees = 0;
    row = 1;
    column = 0;
    currentPurchaseTreated = aCurrentPurchaseTreated;
    isFacingAisle = aIsFacingAisle;
    log = new ArrayList<LogEntry>();
    boolean didAddCheECSEManager = setCheECSEManager(aCheECSEManager);
    if (!didAddCheECSEManager) {
      throw new RuntimeException(
          "Unable to create robot due to cheECSEManager. See " +
          "https://" +
          "manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    setStatus(Status.Idle);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setIsActivated(boolean aIsActivated) {
    boolean wasSet = false;
    isActivated = aIsActivated;
    wasSet = true;
    return wasSet;
  }

  public boolean setDirectionInDegrees(int aDirectionInDegrees) {
    boolean wasSet = false;
    directionInDegrees = aDirectionInDegrees;
    wasSet = true;
    return wasSet;
  }

  public boolean setRow(int aRow) {
    boolean wasSet = false;
    row = aRow;
    wasSet = true;
    return wasSet;
  }

  public boolean setColumn(int aColumn) {
    boolean wasSet = false;
    column = aColumn;
    wasSet = true;
    return wasSet;
  }

  public boolean setCurrentPurchaseTreated(Purchase aCurrentPurchaseTreated) {
    boolean wasSet = false;
    currentPurchaseTreated = aCurrentPurchaseTreated;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsFacingAisle(boolean aIsFacingAisle) {
    boolean wasSet = false;
    isFacingAisle = aIsFacingAisle;
    wasSet = true;
    return wasSet;
  }

  public boolean getIsActivated() { return isActivated; }

  public int getDirectionInDegrees() { return directionInDegrees; }

  public int getRow() { return row; }

  public int getColumn() { return column; }

  public Purchase getCurrentPurchaseTreated() { return currentPurchaseTreated; }

  public boolean getIsFacingAisle() { return isFacingAisle; }
  /* Code from template attribute_IsBoolean */
  public boolean isIsActivated() { return isActivated; }
  /* Code from template attribute_IsBoolean */
  public boolean isIsFacingAisle() { return isFacingAisle; }

  public String getStatusFullName() {
    String answer = status.toString();
    return answer;
  }

  public Status getStatus() { return status; }

  public boolean initialize(String shelfId) {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus) {
    case Idle:
      // line 11 "../../../../../../Robot.ump"
      initializeRobot(shelfId);
      setStatus(Status.AtEntranceNotFacingAisle);
      wasEventProcessed = true;
      break;
    default:
      // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean turnLeft() {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus) {
    case AtEntranceNotFacingAisle:
      // line 16 "../../../../../../Robot.ump"
      setDirectionInDegrees((getDirectionInDegrees() + 90) % 360);
      setStatus(Status.AtEntranceFacingAisle);
      wasEventProcessed = true;
      break;
    default:
      // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean turnRight() {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus) {
    case AtEntranceFacingAisle:
      // line 31 "../../../../../../Robot.ump"
      setDirectionInDegrees((getDirectionInDegrees() - 90) % 360);
      setStatus(Status.AtEntranceNotFacingAisle);
      wasEventProcessed = true;
      break;
    default:
      // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean moveToCheeseWheel(CheeseWheel aWheel) {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus) {
    case AtEntranceFacingAisle:
      if (cheeseWheelExists(aWheel)) {
        // line 36 "../../../../../../Robot.ump"
        setCurrentCheeseWheel(aWheel);
        setStatus(Status.AtCheeseWheel);
        wasEventProcessed = true;
        break;
      }
      break;
    case AtCheeseWheel:
      if (cheeseWheelExists(aWheel)) {
        // line 56 "../../../../../../Robot.ump"
        setCurrentCheeseWheel(aWheel);
        setStatus(Status.AtCheeseWheel);
        wasEventProcessed = true;
        break;
      }
      break;
    default:
      // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean deactivate() {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus) {
    case AtEntranceFacingAisle:
      // line 41 "../../../../../../Robot.ump"
      deactivateRobot();
      setStatus(Status.Idle);
      wasEventProcessed = true;
      break;
    case AtCheeseWheel:
      // line 61 "../../../../../../Robot.ump"
      deactivateRobot();
      setStatus(Status.Idle);
      wasEventProcessed = true;
      break;
    default:
      // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean triggerTreatment() {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus) {
    case AtCheeseWheel:
      if (canTreatCurrentWheel()) {
        setStatus(Status.AtCheeseWheel);
        wasEventProcessed = true;
        break;
      }
      break;
    default:
      // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean moveToEntrance() {
    boolean wasEventProcessed = false;

    Status aStatus = status;
    switch (aStatus) {
    case AtCheeseWheel:
      // line 50 "../../../../../../Robot.ump"
      setRow(1);
      setColumn(0);
      setStatus(Status.AtEntranceFacingAisle);
      wasEventProcessed = true;
      break;
    default:
      // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setStatus(Status aStatus) { status = aStatus; }
  /* Code from template association_GetOne */
  public Shelf getCurrentShelf() { return currentShelf; }

  public boolean hasCurrentShelf() {
    boolean has = currentShelf != null;
    return has;
  }
  /* Code from template association_GetOne */
  public CheeseWheel getCurrentCheeseWheel() { return currentCheeseWheel; }

  public boolean hasCurrentCheeseWheel() {
    boolean has = currentCheeseWheel != null;
    return has;
  }
  /* Code from template association_GetMany */
  public LogEntry getLog(int index) {
    LogEntry aLog = log.get(index);
    return aLog;
  }

  public List<LogEntry> getLog() {
    List<LogEntry> newLog = Collections.unmodifiableList(log);
    return newLog;
  }

  public int numberOfLog() {
    int number = log.size();
    return number;
  }

  public boolean hasLog() {
    boolean has = log.size() > 0;
    return has;
  }

  public int indexOfLog(LogEntry aLog) {
    int index = log.indexOf(aLog);
    return index;
  }
  /* Code from template association_GetOne */
  public CheECSEManager getCheECSEManager() { return cheECSEManager; }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setCurrentShelf(Shelf aNewCurrentShelf) {
    boolean wasSet = false;
    if (aNewCurrentShelf == null) {
      Shelf existingCurrentShelf = currentShelf;
      currentShelf = null;

      if (existingCurrentShelf != null &&
          existingCurrentShelf.getRobot() != null) {
        existingCurrentShelf.setRobot(null);
      }
      wasSet = true;
      return wasSet;
    }

    Shelf currentCurrentShelf = getCurrentShelf();
    if (currentCurrentShelf != null &&
        !currentCurrentShelf.equals(aNewCurrentShelf)) {
      currentCurrentShelf.setRobot(null);
    }

    currentShelf = aNewCurrentShelf;
    Robot existingRobot = aNewCurrentShelf.getRobot();

    if (!equals(existingRobot)) {
      aNewCurrentShelf.setRobot(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setCurrentCheeseWheel(CheeseWheel aNewCurrentCheeseWheel) {
    boolean wasSet = false;
    if (aNewCurrentCheeseWheel == null) {
      CheeseWheel existingCurrentCheeseWheel = currentCheeseWheel;
      currentCheeseWheel = null;

      if (existingCurrentCheeseWheel != null &&
          existingCurrentCheeseWheel.getRobot() != null) {
        existingCurrentCheeseWheel.setRobot(null);
      }
      wasSet = true;
      return wasSet;
    }

    CheeseWheel currentCurrentCheeseWheel = getCurrentCheeseWheel();
    if (currentCurrentCheeseWheel != null &&
        !currentCurrentCheeseWheel.equals(aNewCurrentCheeseWheel)) {
      currentCurrentCheeseWheel.setRobot(null);
    }

    currentCheeseWheel = aNewCurrentCheeseWheel;
    Robot existingRobot = aNewCurrentCheeseWheel.getRobot();

    if (!equals(existingRobot)) {
      aNewCurrentCheeseWheel.setRobot(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLog() { return 0; }
  /* Code from template association_AddManyToOne */
  public LogEntry addLog(String aDescription) {
    return new LogEntry(aDescription, this);
  }

  public boolean addLog(LogEntry aLog) {
    boolean wasAdded = false;
    if (log.contains(aLog)) {
      return false;
    }
    Robot existingRobot = aLog.getRobot();
    boolean isNewRobot = existingRobot != null && !this.equals(existingRobot);
    if (isNewRobot) {
      aLog.setRobot(this);
    } else {
      log.add(aLog);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeLog(LogEntry aLog) {
    boolean wasRemoved = false;
    // Unable to remove aLog, as it must always have a robot
    if (!this.equals(aLog.getRobot())) {
      log.remove(aLog);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addLogAt(LogEntry aLog, int index) {
    boolean wasAdded = false;
    if (addLog(aLog)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfLog()) {
        index = numberOfLog() - 1;
      }
      log.remove(aLog);
      log.add(index, aLog);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLogAt(LogEntry aLog, int index) {
    boolean wasAdded = false;
    if (log.contains(aLog)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfLog()) {
        index = numberOfLog() - 1;
      }
      log.remove(aLog);
      log.add(index, aLog);
      wasAdded = true;
    } else {
      wasAdded = addLogAt(aLog, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setCheECSEManager(CheECSEManager aNewCheECSEManager) {
    boolean wasSet = false;
    if (aNewCheECSEManager == null) {
      // Unable to setCheECSEManager to null, as robot must always be associated
      // to a cheECSEManager
      return wasSet;
    }

    Robot existingRobot = aNewCheECSEManager.getRobot();
    if (existingRobot != null && !equals(existingRobot)) {
      // Unable to setCheECSEManager, the current cheECSEManager already has a
      // robot, which would be orphaned if it were re-assigned
      return wasSet;
    }

    CheECSEManager anOldCheECSEManager = cheECSEManager;
    cheECSEManager = aNewCheECSEManager;
    cheECSEManager.setRobot(this);

    if (anOldCheECSEManager != null) {
      anOldCheECSEManager.setRobot(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    if (currentShelf != null) {
      currentShelf.setRobot(null);
    }
    if (currentCheeseWheel != null) {
      currentCheeseWheel.setRobot(null);
    }
    while (log.size() > 0) {
      LogEntry aLog = log.get(log.size() - 1);
      aLog.delete();
      log.remove(aLog);
    }

    CheECSEManager existingCheECSEManager = cheECSEManager;
    cheECSEManager = null;
    if (existingCheECSEManager != null) {
      existingCheECSEManager.setRobot(null);
    }
  }

  // line 69 "../../../../../../Robot.ump"
  private void initializeRobot(String shelfId) {
    Shelf shelf = Shelf.getWithId(shelfId);
    if (shelf == null)
      throw new RuntimeException("Shelf " + shelfId + " does not exist.");

    setCurrentShelf(shelf);
    setRow(1);
    setColumn(0);
  }

  // line 78 "../../../../../../Robot.ump"
  private void deactivateRobot() {
    setIsActivated(false);
    delete();
  }

  // line 83 "../../../../../../Robot.ump"
  private Boolean shelfExists(String aId) { return Shelf.hasWithId(aId); }

  // line 87 "../../../../../../Robot.ump"
  private Boolean cheeseWheelExists(CheeseWheel wheel) {
    var shelf = getCurrentShelf();
    var location = wheel.getLocation();

    if (location == null)
      return false;
    var candidateShelf = location.getShelf();

    return candidateShelf != null && candidateShelf.equals(shelf);
  }

  // line 97 "../../../../../../Robot.ump"
  private Boolean canTreatCurrentWheel() {
    return getCurrentPurchaseTreated().indexOfCheeseWheel(
               getCurrentCheeseWheel()) != -1;
  }

  // line 101 "../../../../../../Robot.ump"
  public void setInnerStatus(Status aStatus) { setStatus(aStatus); }

  public String toString() {
    return super.toString() + "["
        + "isActivated"
        + ":" + getIsActivated() + ","
        + "directionInDegrees"
        + ":" + getDirectionInDegrees() + ","
        + "row"
        + ":" + getRow() + ","
        + "column"
        + ":" + getColumn() + ","
        + "isFacingAisle"
        + ":" + getIsFacingAisle() + "]" +
        System.getProperties().getProperty("line.separator") + "  "
        + "currentPurchaseTreated"
        + "=" +
        (getCurrentPurchaseTreated() != null
             ? !getCurrentPurchaseTreated().equals(this)
                   ? getCurrentPurchaseTreated().toString().replaceAll("  ",
                                                                       "    ")
                   : "this"
             : "null") +
        System.getProperties().getProperty("line.separator") + "  "
        + "currentShelf = " +
        (getCurrentShelf() != null
             ? Integer.toHexString(System.identityHashCode(getCurrentShelf()))
             : "null") +
        System.getProperties().getProperty("line.separator") + "  "
        + "currentCheeseWheel = " +
        (getCurrentCheeseWheel() != null
             ? Integer.toHexString(
                   System.identityHashCode(getCurrentCheeseWheel()))
             : "null") +
        System.getProperties().getProperty("line.separator") + "  "
        + "cheECSEManager = " +
        (getCheECSEManager() != null
             ? Integer.toHexString(System.identityHashCode(getCheECSEManager()))
             : "null");
  }
}
