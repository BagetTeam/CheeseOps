package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.*;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.LogEntry;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.Robot;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import java.util.List;

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
    if (robot.getIsActivated()) throw new RuntimeException("Robot is already activated");
    if (robot.getStatus() != Robot.Status.Idle) throw new RuntimeException("Robot is not idle");

    boolean activated = robot.activate();
    if (!activated) throw new RuntimeException("Robot was not activated when tried to activate");
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
    if (robot.getStatus() != Robot.Status.AtEntranceNotFacingAisle || robot.getStatus() != Robot.Status.AtCheeseWheel)
      throw new RuntimeException("Robot is not activated when tried to deactivate");

    boolean deactivated = robot.deactivate();
    if(!deactivated)
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
      throw new RuntimeException("Robot is not at entrance not facing aisle when tried to initialize");

    Optional<Shelf> shelfRobotIsAt = Optional.ofNullable(Shelf.getWithId(shelfId));
    if (shelfRobotIsAt.isPresent()) {
      robot.setCurrentShelf(shelfRobotIsAt.get());
    } else {
      throw new RuntimeException("The shelf" + shelfId + " does not exist.");
    }

    robot.setRow(1);
    logAction(LogAction.logAtShelf(shelfId));
    logAction(LogAction.logAdjustHeight(0));
  }

  /* =================================================== */

  /**
   * The facility manager needs to trigger the treatment of all cheese wheels
   * and goes through each cheese wheel in the purchase to treat it.
   * @param purchaseId
   *
   * @author Ming Li Liu
   */
  public static void initializeTreatment(int purchaseId) {
    var purchase = (Purchase)manager.getTransaction(purchaseId);

    purchase.getCheeseWheels().forEach(wheel -> {
      moveToShelf(wheel.getLocation().getShelf().getId());
      moveToCheeseWheel(wheel.getId());
      var shelf = wheel.getLocation().getShelf();

      if (!shelf.getId().equals(robot.getCurrentShelf().getId())) {
        goBackToEntrance();
        moveToShelf(shelf);
      }

      moveToCheeseWheel(wheel);
      treatCurrentWheel();
    });
  }

  /* =================================================== */
  /**
   * @author Ayush Patel
   * Turn 90 degrees left or right when the robot is at a shelf not facing the
  aisle or facing the aisle, respectively
   * @return whether action was successful
   */
  public static boolean turnLeft() {
    if(!robot.getIsActivated())
      throw new RuntimeException("The robot must be activated first");
    if(robot.getStatus() != Robot.Status.AtEntranceNotFacingAisle)
      throw new RuntimeException("The robot cannot be turned left.");

    boolean turnedLeft = robot.turnLeft();
    if(turnedLeft){
      logAction(LogAction.logTurnLeft());
      return true;
    } else {
      return false;
    }
  }

  public static boolean turnRight() {
    if(!robot.getIsActivated())
      throw new RuntimeException("The robot must be activated first");
    if(robot.getStatus() != Robot.Status.AtEntranceFacingAisle)
      throw new RuntimeException("The robot cannot be turned left.");

    boolean turnedRight = robot.turnRight();
    if(turnedRight){
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
  public static boolean moveToShelf(String shelfId) {
    if(!robot.getIsActivated())
      throw new RuntimeException("The robot must be activated first");
    if(robot.getStatus() != Robot.Status.AtEntranceNotFacingAisle)
      throw new RuntimeException("Cannot move to shelf from " + robot.getStatus() + " status");

    Shelf currentShelf = robot.getCurrentShelf();
    Shelf targetShelf = Shelf.getWithId(shelfId);
    if(targetShelf == null){
      throw new RuntimeException("Shelf " + shelfId + " does not exist.");
    }

    if(currentShelf.equals(targetShelf)){
      return false;
    }

    List<Shelf> allShelves = robot.getCheECSEManager().getShelves();
    int currentShelfIndex = allShelves.indexOf(currentShelf);
    int targetShelfIndex = allShelves.indexOf(targetShelf);

    if (targetShelfIndex == -1) {
      throw new IllegalArgumentException("Target shelf does not exist: " + targetShelf);
    }

    boolean forward = targetShelfIndex > currentShelfIndex;

    while (currentShelfIndex != targetShelfIndex) {
      if (forward) {
        currentShelfIndex++;
        logAction(LogAction.logStraight(2));
      } else {
        currentShelfIndex--;
        logAction(LogAction.logStraight(-2));
      }
    }

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
    if(!robot.getIsActivated())
      throw new RuntimeException("The robot must be activated first");
    Robot.Status status = robot.getStatus();
    if(status != Robot.Status.AtEntranceFacingAisle && status != Robot.Status.AtCheeseWheel)
      throw new RuntimeException("Cannot move to shelf from " + robot.getStatus() + " status");

    Shelf currentShelf = robot.getCurrentShelf();
    CheeseWheel targetCheeseWheel = manager.getCheeseWheel(wheelId);
    if(targetCheeseWheel == null){
      throw new RuntimeException("Cheese wheel " + wheelId + " does not exist.");
    }

    ShelfLocation shelfLocationOfTarget = targetCheeseWheel.getLocation();
    Shelf shelfOfTarget = shelfLocationOfTarget.getShelf();
    if(!currentShelf.equals(shelfOfTarget)){
      throw new RuntimeException("The cheese wheel " + wheelId + " does not match the current shelf.");
    }

    int targetRow = shelfLocationOfTarget.getRow();
    int targetCol = shelfLocationOfTarget.getColumn();
    int currRow = robot.getRow();
    int currCol = robot.getColumn();

    if(targetRow == currRow && targetCol == currCol){
      return false;
    }

    boolean moveColForward = targetCol > currCol;
    boolean moveRowUp = targetRow > currRow;

    while (targetCol != currCol) {
      if (moveColForward) {
        currCol++;
        logAction(LogAction.logStraight(1));
      } else{
        currCol--;
        logAction(LogAction.logStraight(-1));
      }
      robot.setColumn(currCol);
    }

    while (targetRow != currRow) {
      if (moveRowUp) {
        currRow++;
        logAction(LogAction.logAdjustHeight(40));
      } else{
        currRow--;
        logAction(LogAction.logAdjustHeight(-40));
      }
      robot.setRow(currRow);
    }

    logAction(LogAction.logAtCheeseWheel(wheelId));
    robot.setStatus(Robot.Status.AtCheeseWheel);
    return true;
  }

  /* =================================================== */
  /**
   * Treat a cheese wheel when the robot is at a cheese wheel (i.e., upon being
   * asked to treat a cheese wheel, the robot picks up the cheese wheel, washes
   * it, turns it, and places it back on the shelf)
   * @author Benjamin Curis-Friedman
   * @return whether action was successful
   */
  public static boolean treatCurrentWheel() {
    // TODO: implement this method
    return false;
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
    if(!robot.getIsActivated())
      throw new RuntimeException("The robot must be activated first");
    Robot.Status status = robot.getStatus();
    if(status != Robot.Status.AtCheeseWheel)
      throw new RuntimeException("Cannot move to shelf from " + robot.getStatus() + " status");

    int targetRow = 1;
    int targetCol = 0; // just a placeholder for being outside the shelf
    int currRow = robot.getRow();
    int currCol = robot.getColumn();

    boolean moveColBackward = targetCol < currCol;
    boolean moveRowDown = targetRow < currRow;

    while (currCol != targetCol) {
      if (moveColBackward) {
        currCol--;
        logAction(LogAction.logStraight(-1));
      } else {
        currCol++;
        logAction(LogAction.logStraight(1));
      }
      robot.setColumn(currCol);
    }

    while (currRow != targetRow) {
      if (moveRowDown) {
        currRow--;
        logAction(LogAction.logAdjustHeight(-40));
      } else {
        currRow++;
        logAction(LogAction.logAdjustHeight(40));
      }
      robot.setRow(currRow);
    }


    robot.setStatus(Robot.Status.AtEntranceFacingAisle);
    return false;
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
    manager.getRobot().addLog(action.toString());
  }

  /**
   * View the robot’s action log
   * @return
   * @author Ming Li Liu
   */
  public static List<TOLogEntry> viewLog() {
    return manager.getRobot()
        .getLog()
        .stream()
        .map(log -> new TOLogEntry(log.getDescription()))
        .toList();
  }
}
