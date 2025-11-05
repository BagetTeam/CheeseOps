package ca.mcgill.ecse.cheecsemanager.controller;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.*;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import java.util.List;
import java.util.Optional;

public class RobotController {
  static CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
  static Robot robot = manager.getRobot();

  /* =================================================== */
  /**
   * @author Ayush Patel
   *The employees place the robot at the entrance of the aisle of a shelf but
   * not facing the aisle.
   *the system allows the facility manager to trigger the treatment of all
   * cheese wheels from one purchase with a specified maturation period that are
   * not spoiled. First, the facility manager needs to activate the robot.
   * After initialization, the height of the robot defaults to row 1.
   */
  public static void activateRobot() {
    if (robot.getIsActivated())
      throw new RuntimeException("Robot is already activated");
    if (robot.getStatus() != Robot.Status.Idle)
      throw new RuntimeException("Robot is not idle");

    boolean activated = robot.activate();
    if (!activated)
      throw new RuntimeException(
          "Robot was not activated when tried to activate");
  }

  /**
   * @author Ayush Patel
   * After deactivation, the robot’s action log is empty
   * All data related to the robot is deleted from the system when the robot is
   * deactivated.
   */
  public static void deactivateRobot() {
    if (!robot.getIsActivated())
      throw new RuntimeException("Robot is not already activated");
    if (robot.getStatus() != Robot.Status.AtEntranceFacingAisle &&
        robot.getStatus() != Robot.Status.AtCheeseWheel)
      throw new RuntimeException(
          "Robot is not activated when tried to deactivate");

    boolean deactivated = robot.deactivate();
    if (!deactivated)
      throw new RuntimeException("Robot was not deactivated successfully");

    robot.delete(); // clears all data related to the robot from the system
  }

  /**
   * @author Ayush Patel
   * @param shelfId
   * Then, the facility manager initializes the robot by specifying the shelf
   * where the robot was placed by the employees
   * */
  public static void initializeRobot(String shelfId) {
    if (!robot.getIsActivated())
      throw new RuntimeException("The robot has already been initialized.");
    if (robot.getStatus() != Robot.Status.AtEntranceNotFacingAisle)
      throw new RuntimeException(
          "Robot is not at entrance not facing aisle when tried to initialize");

    Optional<Shelf> shelfRobotIsAt =
        Optional.ofNullable(Shelf.getWithId(shelfId));
    if (shelfRobotIsAt.isPresent()) {
      robot.setCurrentShelf(shelfRobotIsAt.get());
    } else {
      throw new RuntimeException("The shelf" + shelfId + " does not exist.");
    }

    logAction(LogAction.logAtShelf(shelfId));
    robot.setRow(1);
  }

  /* =================================================== */

  /**
   * The facility manager needs to trigger the treatment of all cheese wheels
   * and goes through each cheese wheel in the purchase to treat it.
   * @param purchaseId
   *
   * @author Ming Li Liu and Olivier Mao
   */
  public static void initializeTreatment(int purchaseId, MaturationPeriod monthAged) {

    // Get transaction of purchaseId and purchase
    Transaction t = manager.getTransaction(purchaseId);
    Purchase purchase = (Purchase)t;

    purchase.getCheeseWheels().forEach(wheel -> {
      treatCheeseWheel(wheel, monthAged);
    });
  }

  /** 
   * Treat cheese wheel of a specific age
   * @param wheel: the cheese wheel to treat
   * @param monthAged: the age of the cheese wheel
   * @author Ewen Gueguen
   */
  private static void treatCheeseWheel(CheeseWheel wheel, MaturationPeriod monthAged) {
    if (wheel.getMonthsAged() != monthAged) {
        return;
      }
      var shelf = wheel.getLocation().getShelf();

      if (!shelf.getId().equals(robot.getCurrentShelf().getId())) {
        goBackToEntrance();
        turnRight();
        moveToShelf(shelf.getId());
        turnLeft();
      }

      moveToCheeseWheel(wheel.getId());
      treatCurrentWheel();
  }

  /* =================================================== */
  /**
   * @author Ayush Patel
   * Turn 90 degrees left or right when the robot is at a shelf not facing the
  aisle or facing the aisle, respectively
   * @return whether action was successful
   */
  public static boolean turnLeft() {
    if (!robot.getIsActivated())
      throw new RuntimeException("The robot must be activated first.");
    if (robot.getStatus() != Robot.Status.AtEntranceNotFacingAisle)
      throw new RuntimeException("The robot cannot be turned left.");

    boolean turnedLeft = robot.turnLeft();
    if (turnedLeft) {
      logAction(LogAction.logTurnLeft());
      return true;
    } else {
      return false;
    }
  }

  public static boolean turnRight() {
    if (!robot.getIsActivated())
      throw new RuntimeException("The robot must be activated first.");
    if (robot.getStatus() != Robot.Status.AtEntranceFacingAisle)
      throw new RuntimeException("The robot cannot be turned right.");

    boolean turnedRight = robot.turnRight();
    if (turnedRight) {
      logAction(LogAction.logTurnRight());
      return true;
    } else {
      return false;
    }
  }

  /**
   * Go to a shelf when the robot is not facing an aisle (i.e., the robot
   * determines how much to move straight forward or backward)
   * @param shelfId the id of the target shelf
   * @return whether action was successful
   */
  public static boolean moveToShelf(String shelfId) throws RuntimeException {
    if (shelfId == null || shelfId.isEmpty()) {
      throw new RuntimeException("A shelf must be specified.");
    }
    if (!robot.getIsActivated())
      throw new RuntimeException("The robot must be activated first.");
    if (robot.getStatus() != Robot.Status.AtEntranceNotFacingAisle)
      throw new RuntimeException("The robot cannot be moved to shelf #" +
                                 shelfId + ".");

    Shelf currentShelf = robot.getCurrentShelf();
    Shelf targetShelf = Shelf.getWithId(shelfId);
    if (targetShelf == null) {
      throw new RuntimeException("The shelf " + shelfId + " does not exist.");
    }

    if (currentShelf.equals(targetShelf)) {
      return false;
    }

    List<Shelf> allShelves = manager.getShelves();
    int currentShelfIndex = allShelves.indexOf(currentShelf);
    int targetShelfIndex = allShelves.indexOf(targetShelf);

    if (targetShelfIndex == -1) {
      throw new IllegalArgumentException("Target shelf does not exist: " +
                                         targetShelf);
    }

    int diff = targetShelfIndex - currentShelfIndex;
    logAction(LogAction.logStraight(diff * 2));
    logAction(LogAction.logAtShelf(shelfId));

    robot.setCurrentShelf(targetShelf);
    return true;
  }

  /* =================================================== */
  /**
   * @author Ayush Patel
   * Go to a cheese wheel when the robot is facing an aisle or is already at a
   * cheese wheel (i.e., the robot determines how much to move straight forward
   * or backward and how much to adjust its height up or down)
   * @param wheelId the id of the target cheeseWheel
   * @return whether action was successful
   */
  public static boolean moveToCheeseWheel(int wheelId) {
    Robot.Status status = robot.getStatus();
    if (!robot.getIsActivated())
      throw new RuntimeException("The robot must be activated first.");
    if (status != Robot.Status.AtEntranceFacingAisle &&
        status != Robot.Status.AtCheeseWheel)
      throw new RuntimeException("The robot cannot be moved to cheese wheel #" +
                                 wheelId + ".");

    Shelf currentShelf = robot.getCurrentShelf();
    CheeseWheel targetCheeseWheel =
        manager.getCheeseWheels()
            .stream()
            .filter(wheel -> wheel.getId() == wheelId)
            .findFirst()
            .orElseThrow(()
                             -> new RuntimeException("Cheese wheel " + wheelId +
                                                     " does not exist."));

    ShelfLocation shelfLocationOfTarget = targetCheeseWheel.getLocation();
    Shelf shelfOfTarget = shelfLocationOfTarget.getShelf();

    if (!currentShelf.getId().equals(shelfOfTarget.getId())) {
      throw new RuntimeException("Cheese wheel #" + wheelId +
                                 " is not on shelf #" + currentShelf.getId() +
                                 ".");
    }

    int targetRow = shelfLocationOfTarget.getRow();
    int targetCol = shelfLocationOfTarget.getColumn();
    int currRow = robot.getRow();
    int currCol = robot.getColumn();

    if (targetRow == currRow && targetCol == currCol) {
      return false;
    }

    robot.setColumn(targetCol);
    if (targetCol != currCol) {
      logAction(LogAction.logStraight(targetCol - currCol));
    }
    if (targetRow != currRow) {
      logAction(LogAction.logAdjustHeight((targetRow - 1) * 40)); // the robot
    }
    robot.setRow(targetRow);

    robot.moveToCheeseWheel(targetCheeseWheel);
    logAction(LogAction.logAtCheeseWheel(wheelId));

    return true;
  }

  /* =================================================== */
  /**
   * Treat a cheese wheel when the robot is at a cheese wheel (i.e., upon being
   * asked to treat a cheese wheel, the robot picks up the cheese wheel, washes
   * it, turns it, and places it back on the shelf)
   * @author Benjamin Curis-Friedman and Olivier Mao (modified)
   * @return whether action was successful
   */
  public static boolean treatCurrentWheel() {
    // Validation for if the robot is activated
    if (!robot.getIsActivated()) {
      throw new RuntimeException("The robot must be activated first.");
    }
    // Valdiation to see if the robot can perform treatment
    if (robot.getStatus() != Robot.Status.AtCheeseWheel) {
      throw new RuntimeException("The robot cannot be perform treatment.");
    }

    boolean success = robot.triggerTreatment();

    if (success) { // log if its true
      int wheelID = robot.getCurrentCheeseWheel().getId();
      logAction(LogAction.logTreatCheeseWheel(wheelID));     
    }
    
    return success;
  }

  /**
   * @author Ayush Patel
   * Go back to the entrance of the aisle when the robot is at a cheese wheel
   * (i.e., the robot determines how much to move straight backward and how much
   * to adjust its height down)
   * When the robot goes back to the entrance of an aisle, its height must be
   * reset to row 1
   * @return whether action was successful
   */
  public static boolean goBackToEntrance() {
    if (!robot.getIsActivated()) {
      throw new RuntimeException("The robot must be activated first.");
    }
    if ((robot.getStatus() != Robot.Status.AtCheeseWheel &&
         robot.getStatus() != Robot.Status.AtEntranceFacingAisle))
      throw new RuntimeException(
          "The robot cannot be moved to the entrance of the aisle.");

    int targetRow = 1;
    int targetCol = 0;
    int currRow = robot.getRow();
    int currCol = robot.getColumn();
    int deltaCol = targetCol - currCol;
    int deltaRow = targetRow - currRow;

    if (deltaCol != 0) {
      logAction(LogAction.logStraight(deltaCol));
    }

    if (deltaRow != 0) {
      logAction(LogAction.logAdjustHeight((deltaRow) * 40));
    }

    robot.setCurrentCheeseWheel(null);
    robot.moveToEntrance();
    return true;
  }
  /* =================================================== */

  /**
   *
   * At any point, the facility manager can review the robot’s action log. The
   * robot logs all of its actions as follows in separate log entries:
   *
   * • At shelf #<id>;
   *
   * • At cheese wheel #<id>;
   *
   * • Straight <+/-> <n> meters;
   *
   * • Adjust height <+/-> <n> centimeters;
   *
   * • Turn <left/right>;
   *
   * • Treat cheese wheel #<id> (wash & turn);
   *
   * @author Ming Li Liu
   */
  public static void logAction(LogAction action) {
    robot.addLog(action.toString());
  }

  /**
   * View the robot’s action log
   * @return
   * @author Ming Li Liu
   */
  public static List<TOLogEntry> viewLog() {
    return robot.getLog()
        .stream()
        .map(log -> new TOLogEntry(log.getDescription()))
        .toList();
  }
}
