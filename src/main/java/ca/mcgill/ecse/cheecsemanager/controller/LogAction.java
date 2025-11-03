package ca.mcgill.ecse.cheecsemanager.controller;

public class LogAction {
  String description;

  public LogAction(String description) { this.description = description; }

  static public LogAction logAtShelf(String shelfId) {
    return new LogAction("At shelf #" + shelfId + ";");
  }

  static public LogAction logAtCheeseWheel(int wheelId) {
    return new LogAction("At cheese wheel #" + wheelId + ";");
  }

  static public LogAction logStraight(int meters) {
    return new LogAction("Straight " + (meters < 0 ? "- " : "+ ") + meters +
                         " meters;");
  }

  static public LogAction logAdjustHeight(int centimeters) {
    return new LogAction("Adjust height " + (centimeters < 0 ? "- " : "+ ") +
                         centimeters + " centimeters;");
  }

  static public LogAction logTurnLeft() { return new LogAction("Turn left;"); }

  static public LogAction logTurnRight() {
    return new LogAction("Turn right;");
  }

  static public LogAction logTreatCheeseWheel(int cheeseWheelId) {
    return new LogAction("Treat cheese wheel #" + cheeseWheelId +
                         " (wash & turn);");
  }

  @Override
  public String toString() {
    return description;
  }
}
