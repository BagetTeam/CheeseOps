package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.Robot;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;

public class RobotController {
  static CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
  static Robot robot = manager.getRobot();

  /* =================================================== */

  /**
   *The employees place the robot at the entrance of the aisle of a shelf but
   * not facing the aisle.
   *the system allows the facility manager to trigger the treatment of all
   * cheese wheels from one purchase with a specified maturation period that are
   * not spoiled. First, the facility manager needs to activate the robot.
   * After initialization, the height of the robot defaults to row 1.
   */
  public static void activateRobot() {
    // TODO: implement this method
  }

  /**
   * After deactivation, the robot’s action log is empty
   *
   * All data related to the robot is deleted from the system when the robot is
   * deactivated.
   */
  public static void deactivateRobot() {
    // TODO: implement this method
  }

  /**
   * Then, the facility manager initializes the robot by specifying the shelf
   * where the robot was placed by the employees
   * */
  public static void initializeRobot() {
    // TODO: implement this method
  }

  /* =================================================== */

  /**
   * @author Ming Li Liu
   * @param purchaseId
   */
  public static void initializeTreatment(int purchaseId) {
    var purchase = (Purchase)manager.getTransaction(purchaseId);

    purchase.getCheeseWheels().forEach(wheel -> {
      moveToShelf(wheel.getLocation().getShelf());
      moveToCheeseWheel(wheel);
      treatCurrentWheel();
    });
  }

  /* =================================================== */
  /**
   * Turn 90 degrees left or right when the robot is at a shelf not facing the
  aisle or facing the aisle, respectively
   * @return whether action was successful
   */
  public static boolean turnLeft() {
    // TODO: implement this method
    return false;
  }

  public static boolean turnRight() {
    // TODO: implement this method
    return false;
  }

  /**
   * Each column of a shelf is one meter wide, which is big enough for a cheese
   * wheel to be placed on the shelf without being too close to other cheese
   * wheels.
   * To move between columns of a shelf, the robot needs to move one meter
   * forward to go the next column and one meter backward to go to the previous
   * column.
   * The distance from the robot’s position at the entrance of an aisle to its
   * first column is also one meter.
   * @return whether action was successful
   */
  public static boolean moveForward() {
    // TODO: implement this method
    return false;
  }

  public static boolean moveBackward() {
    // TODO: implement this method
    return false;
  }

  /*
   * Go to a shelf when the robot is not facing an aisle (i.e., the robot
   * determines how much to move straight forward or backward)
   * @param shelfId
   * @return whether action was successful
   */
  public static boolean moveToShelf(Shelf shelf) {
    // TODO: implement this method
    return false;
  }

  /* =================================================== */
  /**
   * Go to a cheese wheel when the robot is facing an aisle or is already at a
   * cheese wheel (i.e., the robot determines how much to move straight forward
   * or backward and how much to adjust its height up or down)
   * @param wheelId
   * @return whether action was successful
   */
  public static boolean moveToCheeseWheel(CheeseWheel wheel) {
    // TODO: implement this method
    return false;
  }

  /**
   * Two adjacent rows of a shelf are spaced 40 centimeters apart. Row 1 is at
   * the bottom of the shelf. To move between rows of a shelf, the robot needs
   * to move 40 centimeters up to go the next row and 40 centimeters down to go
   * to the previous row.
   * @param height
   * @return
   */
  public static boolean adjustHeight(int height) {
    // TODO: implement this method
    return false;
  }

  /* =================================================== */
  /**
   * Treat a cheese wheel when the robot is at a cheese wheel (i.e., upon being
   * asked to treat a cheese wheel, the robot picks up the cheese wheel, washes
   * it, turns it, and places it back on the shelf)
   * @return whether action was successful
   * @author Benjamin Curis-Friedman
   */
  public static boolean treatCurrentWheel() {
    boolean success = robot.triggerTreatment();

    if (success) { // log if its true
      int wheelID = robot.getCurrentCheeseWheel().getId();
      logAction(new LogAction("Treat cheese wheel #" + wheelID + " (wash & turn)"));
    }

    return success;
  }

  /**
   * Go back to the entrance of the aisle when the robot is at a cheese wheel
   * (i.e., the robot determines how much to move straight backward and how much
   * to adjust its height down)
   * When the robot goes back to the entrance of an aisle, its height must be
   * reset to row 1
   * @return whether action was successful
   */
  public static boolean goBackToEntrance() {
    // TODO: implement this method
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
}
