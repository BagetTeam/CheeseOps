package ca.mcgill.ecse.cheecsemanager.controller;

public class LogAction {
  String description;

  public LogAction(String description) { this.description = description; }

  public LogAction LogAtShelf(String shelfId) {
    return new LogAction("At shelf #" + shelfId + ";");
  }

  public LogAction LogAtCheeseWheel(String wheelId) {
    return new LogAction("At cheese wheel #" + wheelId + ";");
  }

  public LogAction LogStraight(int meters) {
    return new LogAction("Straight " + (meters < 0 ? "- " : "+ ") + meters +
                         " meters;");
  }

  public LogAction LogAdjustHeight(int centimeters) {
    return new LogAction("Adjust height " + (centimeters < 0 ? "- " : "+ ") +
                         centimeters + " centimeters;");
  }

  public LogAction LogTurnLeft() { return new LogAction("Turn left;"); }

  public LogAction LogTurnRight() { return new LogAction("Turn right;"); }

  public LogAction LogTreatCheeseWheel(int cheeseWheelId) {
    return new LogAction("Treat cheese wheel #" + cheeseWheelId +
                         " (wash & turn);");
  }

  @Override
  public String toString() {
    return description;
  }
}
