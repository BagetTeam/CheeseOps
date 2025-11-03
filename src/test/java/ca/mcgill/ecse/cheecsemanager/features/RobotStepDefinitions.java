package ca.mcgill.ecse.cheecsemanager.features;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDate;
import java.time.ZoneId;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.RobotController;
import ca.mcgill.ecse.cheecsemanager.model.*;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RobotStepDefinitions {
  private CheECSEManager cheecsemanager = CheECSEManagerApplication.getCheecseManager();
  private Exception error;

  private Robot getRobot(){
    if(cheecsemanager.hasRobot()){
      return cheecsemanager.getRobot();
    } else {
      Robot robot = new Robot(false, 0, 1, 0, null, cheecsemanager);
      cheecsemanager.setRobot(robot);
      return cheecsemanager.getRobot();
    }
  }

  /**
   * @author Ayush Patel
   * This method is used in given steps to set the status of the robot to the desired status
   * */
  private void setStatus(Robot robot, Robot.Status targetStatus){
    Robot.Status currentStatus = robot.getStatus();
    if(currentStatus.equals(targetStatus)){
      return;
    }
  }
  /**
   * Create shelves from the provided datatable.
   * Table columns expected: id
   * Uses CheECSEManager.addShelve(id).
   *
   * @param dataTable Cucumber datatable with a column "id"
   * @author Olivier Mao
   */
  @Given("the following shelf exists in the system")
  public void the_following_shelf_exists_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String id = row.get("id");
      if (id != null && !id.isEmpty()) {
        cheecsemanager.addShelve(id);
      }
    }
  }

  /**
   * Create all locations for a specific shelf.
   * 
   * @param shelfId: The id of the shelf to create locations for
   * @author Ewen Gueguen
   */
  @Given("all locations are created for shelf {string}")
  public void all_locations_are_created_for_shelf(String shelfId) {
    var shelves = cheecsemanager.getShelves();
    for (var shelf : shelves) {
      if (shelfId.equals(shelf.getId())) {
        // TODO TS IS NOT IT
        for (var location : shelf.getLocations()) {
          shelf.addLocation(location);
        }
      }
    }
  }

  /**
   * Create farmers from the provided datatable.
   * Table columns expected: email,password,address,name (name optional)
   *
   * @param dataTable Cucumber datatable with farmer rows
   * @author Olivier Mao
   */
  @Given("the following farmer exists in the system")
  public void the_following_farmer_exists_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String email = row.get("email");
      String password = row.get("password");
      String address = row.get("address");
      String name = row.get("name");
      Farmer f = cheecsemanager.addFarmer(email, password, address);
      if (name != null && !name.isEmpty()) {
        f.setName(name);
      }
    }
  }

  /**
   * Table columns expected: purchaseDate, nrCheeseWheels, monthsAged, farmerEmail
   * 
   * @param dataTable Cucumber datatable with purchase rows
   * @author Ewen Gueguen
   */
  @Given("the following purchase exists in the system")
  public void the_following_purchase_exists_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> purchases = dataTable.asMaps();
    for (var purchase : purchases) {
      String dateString = purchase.get("purchaseDate"); // e.g., "2024-03-15"
      long epochTime = LocalDate.parse(dateString)
              .atStartOfDay(ZoneId.systemDefault())
              .toInstant()
              .toEpochMilli();
      Date transactionDate = new Date(epochTime);
      int nrCheeseWheels = Integer.parseInt(purchase.get("nrCheeseWheels"));
      MaturationPeriod monthsAged = MaturationPeriod.valueOf(purchase.get("monthsAged"));
      String farmerEmail = purchase.get("farmerEmail");
      var farmer = (Farmer) Farmer.getWithEmail(farmerEmail);
      var addedPurchase = farmer.addPurchase(transactionDate, cheecsemanager);
      // TODO TS IS NOT IT
      for (int i = 0; i < nrCheeseWheels; i++) {
        addedPurchase.addCheeseWheel(monthsAged, false, cheecsemanager);
      }
    }
  }

   /**
   * No-op GIVEN: purchases are created (and their wheels added) by the "the following purchase exists in the system"
   * step(s). Keep this step blank and idempotent so features that include it still run.
   *
   * @author Olivier Mao
   */
  @Given("all cheese wheels for the following purchases are created")
  public void all_cheese_wheels_for_the_following_purchases_are_created(
      io.cucumber.datatable.DataTable dataTable) {
    // Intentionally left blank: purchase creation steps already create cheese wheels.
  }

  /**
   * This step definition ensures that the cheese wheel is at a shelf location with a given column
   * and row
   *
   * @param cheeseWheelIndex The index of the cheese wheel
   * @param column The column number on the shelf
   * @param row The row number on the shelf
   * @param shelfId The ID of the shelf
   * @author Olivier Mao
   */
  @Given("cheese wheel {int} is at shelf location with column {int} and row {int} of shelf {string}")
  public void cheese_wheel_is_at_shelf_location_with_column_and_row_of_shelf(Integer cheeseWheelIndex,
      Integer column, Integer row, String shelfId) {
    CheeseWheel cw = cheecsemanager.getCheeseWheel(cheeseWheelIndex - 1);
    Shelf shelf = Shelf.getWithId(shelfId);
    if (shelf == null || cw == null) {
      return;
    }
    
    ShelfLocation location = null;
    for (ShelfLocation loc : shelf.getLocations()) {
      if (loc.getColumn() == column && loc.getRow() == row) {
        location = loc;
        break;
      }
    }

    if (location == null) {
      location = shelf.addLocation(column, row);
    }
    
    if (!location.hasCheeseWheel()) {
      cw.setLocation(location);
    }

  }

  /**
   * Table columns expected: id
   * 
   * @param dataTable Cucumber datatable with cheese wheel rows (id)
   * @author Ewen Gueguen
   */
  @Given("the following cheese wheels are spoiled")
  public void the_following_cheese_wheels_are_spoiled(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> cheeseWheelsRows = dataTable.asMaps();
    for (var cheeseWheelRow : cheeseWheelsRows) {
      int id = Integer.parseInt(cheeseWheelRow.get("id"));
      List<CheeseWheel> cheeseWheels = cheecsemanager.getCheeseWheels();
      CheeseWheel cheeseWheel = null;
      for (CheeseWheel cW : cheeseWheels) {
        if (cW.getId() == id) {
          cheeseWheel = cW;
          break;
        }
      }
      if (cheeseWheel == null) {
        throw new RuntimeException("Cheese wheel " + id + " does not exist.");
      }
      cheeseWheel.setIsSpoiled(true);
    }
  }

  /**
   * @author Ayush Patel
   * @param state current state of the robot
   * @param shelfId shelf where the robot is
   * @param actionLog log message found in the robot's logs
   */
  @Given("the robot is marked as {string} and at shelf {string} with action log {string}")
  public void the_robot_is_marked_as_and_at_shelf_with_action_log(String state, String shelfId,
                                                                  String actionLog) {
    Robot robot = getRobot();
    Shelf shelf = Shelf.getWithId(shelfId);
    robot.setCurrentShelf(shelf);
    switch (state) {
      case "Idle":
        setStatus(robot ,Robot.Status.Idle);
        break;
      case "AtEntranceFacingAisle":
        setStatus(robot, Robot.Status.AtEntranceFacingAisle);
        break;
      case "AtEntranceNotFacingAisle":
        setStatus(robot, Robot.Status.AtEntranceNotFacingAisle);
        break;
      case "AtCheeseWheel":
        setStatus(robot, Robot.Status.AtCheeseWheel);
        break;
      default:
        throw new RuntimeException("Unknown state: " + state);
    }

    while (robot.numberOfLog() > 0) {
      LogEntry logEntry = robot.getLog(robot.numberOfLog() - 1);
      logEntry.delete();
    }

    robot.addLog(actionLog);
  }

   /**
   * Increase the months aged value of cheese wheels.
   * Each row must contain "id" and "newMonthsAged" columns. Uses the model API to set the months aged value.
   *
   * @param dataTable the Cucumber datatable with cheese wheel rows (id, newMonthsAged)
   * @author Ewen Gueguen
   */
  @Given("the months aged value of the following cheese wheels is increased")
  public void the_months_aged_value_of_the_following_cheese_wheels_is_increased(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> cheeseWheels = dataTable.asMaps();
    for (var row : cheeseWheels) {
      int id = Integer.parseInt(row.get("id"));
      MaturationPeriod months = MaturationPeriod.valueOf(row.get("newMonthsAged"));
      cheecsemanager.getCheeseWheel(id).setMonthsAged(months);
    }
  }

  /**
   * @author Ayush Patel
   * @param state the current state of the robot
   * */
  @Given("the robot is marked as {string}")
  public void the_robot_is_marked_as(String state) {
    Robot robot = cheecsemanager.getRobot();
    RobotController.deactivateRobot(); // to ensure starting with robot with raw data
    switch (state) {
      case "Idle":
        robot.setStatus(Robot.Status.Idle);
        break;
      case "AtEntranceFacingAisle":
        robot.setStatus(Robot.Status.AtEntranceFacingAisle);
        break;
      case "AtEntranceNotFacingAisle":
        robot.setStatus(Robot.Status.AtEntranceNotFacingAisle);
        break;
      case "AtCheeseWheel":
        robot.setStatus(Robot.Status.AtCheeseWheel);
        break;
      default:
        throw new RuntimeException("Unknown state: " + state);
    }
  }

    /**
   * Ensures the wholesale companies from the provided datatable exist in the system.
   * Each row must contain "name" and "address" columns. Uses the model API to add companies.
   *
   * @param dataTable the Cucumber datatable with company rows (name, address)
   * @author Olivier Mao
   */
  @Given("the following wholesale company exists in the system")
  public void the_following_wholesale_company_exists_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> companies = dataTable.asMaps();
    for (var row : companies) {
      String name = row.get("name");
      String address = row.get("address");
      cheecsemanager.addCompany(name, address);
    }
  }

  /**
   * Creates orders from the provided datatable.
   * Each row must contain the columns: "transactionDate", "nrCheeseWheels", "monthsAged", "deliveryDate", "company".
   * Corresponding wholesale company already exist in the system.
   * Uses model API to create Order objects
   * 
   * @param dataTable the Cucumber datatable containing order information
   * @throws IllegalArgumentException if the referenced wholesale company does not exist in the system
   * @author Eun-jun Chang
   */
  @Given("the following order exists in the system")
  public void the_following_order_exists_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for(var row : rows) {
    	Date transactionDate = Date.valueOf(row.get("transactionDate"));
    	int nrCheeseWheels = Integer.parseInt(row.get("nrCheeseWheels"));
    	CheeseWheel.MaturationPeriod monthsAged = CheeseWheel.MaturationPeriod.valueOf(row.get("monthsAged"));
    	Date deliveryDate = Date.valueOf(row.get("deliveryDate"));
    	String companyName = row.get("company");
    	
    	var manager = CheECSEManagerApplication.getCheecseManager();
    	WholesaleCompany company = null;
    	for(WholesaleCompany wc : manager.getCompanies()) {
    		if(wc.getName().equalsIgnoreCase(companyName)) {
    			company = wc;
    			break;
    		}
    	}
    	if(company == null) {
    		throw new IllegalArgumentException("Company " + companyName + " does not exist.");
    	}
    	company.addOrder(transactionDate, manager, nrCheeseWheels, monthsAged, deliveryDate);
    	//the cheese wheels will be added later in the all non-spoiled cheese wheels from purchase
    }
  }

  @Given("the robot is marked as {string} and at cheese wheel {int} on shelf {string} with action log {string}")
  public void the_robot_is_marked_as_and_at_cheese_wheel_on_shelf_with_action_log(String state, Integer wheelId, String shelfId, String actionLog) {
    Robot robot = cheecsemanager.getRobot();
    CheeseWheel cheeseWheel = cheecsemanager.getCheeseWheel(wheelId);
    Shelf shelf = Shelf.getWithId(shelfId);
    robot.setCurrentShelf(shelf);
    robot.setCurrentCheeseWheel(cheeseWheel);
    switch (state) {
      case "AtCheeseWheel":
        robot.setStatus(Robot.Status.AtCheeseWheel);
        break;
      default:
        throw new RuntimeException("Unsupported state for this state: " + state);
    }

    robot.addLog(actionLog);
  }

  /**
   * Adds all non-spoiled cheese wheels from a given purchase to a given order. 
   * Uses model API to get Purchase and Order objects
   * 
   * @param purchaseId index of the purchase to take cheese wheels from
   * @param orderId index of the order to which the cheese wheels will be added
   * @throws IllegalArgumentException if either the purchase or the order does not exist
   * @author Eun-jun Chang
   */
  @Given("all non-spoiled cheese wheels from purchase {int} are added to order {int}")
  public void all_non_spoiled_cheese_wheels_from_purchase_are_added_to_order(Integer purchaseId,
      Integer orderId) {
    Purchase purchase = null;
    Order order = null;

    for(Transaction t : cheecsemanager.getTransactions()) {
    	if (t instanceof Order && t.getId() == orderId) {
    	    order = (Order) t;
    	} else if (t instanceof Purchase && t.getId() == purchaseId) {
    	    purchase = (Purchase) t;
    	}
    }
    if(purchase == null) {
    	throw new IllegalArgumentException("The purchase " + purchaseId + " does not exist.");
    }
    if(order == null) {
    	throw new IllegalArgumentException("The order " + orderId + " does not exist."); 
    }
    for(CheeseWheel cheese : purchase.getCheeseWheels()) {
    	if(!cheese.isIsSpoiled()) {
    		order.addCheeseWheel(cheese);
    	}
    }
  }

  /**\
   * @author Ayush Patel
   */
  @When("the facility manager attempts to activate the robot")
  public void the_facility_manager_attempts_to_activate_the_robot() {
    // Write code here that turns the phrase above into concrete actions
    try{
      RobotController.activateRobot();
    } catch (Exception e){
      error = e;
    }
  }

  /**\
   * @author Ayush Patel
   */
  @When("the robot controller attempts to turn the robot left")
  public void the_robot_controller_attempts_to_turn_the_robot_left() {
    try{
      RobotController.turnLeft();
    } catch(Exception e){
      error = e;
    }
  }

  /**\
   * @author Ayush Patel
   */
  @When("the robot controller attempts to turn the robot right")
  public void the_robot_controller_attempts_to_turn_the_robot_right() {
    try{
      RobotController.turnRight();
    } catch(Exception e){
      error = e;
    }
  }

  /**\
   * @author Ayush Patel
   */
  @When("the robot controller attempts to move the robot to cheese wheel {int}")
  public void the_robot_controller_attempts_to_move_the_robot_to_cheese_wheel(Integer wheelId) {
    // Write code here that turns the phrase above into concrete actions
    try{
      RobotController.moveToCheeseWheel(wheelId);
    } catch(Exception e){
      error = e;
    }
  }

  /**\
   * @author Ayush Patel
   */
  @When("the robot controller attempts to move the robot to the entrance")
  public void the_robot_controller_attempts_to_move_the_robot_to_the_entrance() {
    try{
      RobotController.goBackToEntrance();
    } catch(Exception e){
      error = e;
    }
  }

  @When("the robot controller attempts to trigger the robot to perform treatment")
  public void the_robot_controller_attempts_to_trigger_the_robot_to_perform_treatment() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  /**\
   * @author Ayush Patel
   */
  @When("the robot controller attempts to move the robot to shelf {string}")
  public void the_robot_controller_attempts_to_move_the_robot_to_shelf(String shelfId) {
    // Write code here that turns the phrase above into concrete actions
    try{
      RobotController.moveToShelf(shelfId);
    } catch(Exception e){
      error = e;
    }
  }

  @When("the facility manager attempts to trigger the robot to perform treatment on {string} old cheese wheels of purchase {int}")
  public void the_facility_manager_attempts_to_trigger_the_robot_to_perform_treatment_on_old_cheese_wheels_of_purchase(
      String string, Integer int1) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  /**\
   * @author Ayush Patel
   */
  @When("the facility manager attempts to deactivate the robot")
  public void the_facility_manager_attempts_to_deactivate_the_robot() {
    try{
      RobotController.deactivateRobot();
    } catch(Exception e){
      error = e;
    }
  }

  /**
   * @author Ayush Patel
   */
  @Then("the robot shall be marked as {string}")
  public void the_robot_shall_be_marked_as(String expectedStatusString) {
    Robot robot = cheecsemanager.getRobot();
    Robot.Status actualStatus = robot.getStatus();
    Robot.Status expectedStatus = switch(expectedStatusString) {
      case "Idle" -> Robot.Status.Idle;
      case "AtCheeseWheel" -> Robot.Status.AtCheeseWheel;
      case "AtEntranceNotFacingAisle" -> Robot.Status.AtEntranceNotFacingAisle;
      case "AtEntranceFacingAisle" -> Robot.Status.AtEntranceFacingAisle;
      default -> throw new IllegalArgumentException("Unknown status:" + expectedStatusString);
    };

    if (!actualStatus.equals(expectedStatus)) {
      throw new RuntimeException("Unexpected value. Expected: " + expectedStatusString + ", Actual: " + actualStatus);
    }

  }

  /**
   * @author Ayush Patel
   */
  @Then("the current shelf of the robot shall be not specified")
  public void the_current_shelf_of_the_robot_shall_be_not_specified() {
    Robot robot = cheecsemanager.getRobot();
    Optional<Shelf> currentShelf = Optional.ofNullable(robot.getCurrentShelf());

    if (currentShelf.isPresent()) {
      throw new RuntimeException("Current shelf does exist with id: " + currentShelf.get().getId());
    }
  }

  /**
   * @author Ayush Patel
   */
  @Then("the current cheese wheel of the robot shall be not specified")
  public void the_current_cheese_wheel_of_the_robot_shall_be_not_specified() {
    // Write code here that turns the phrase above into concrete actions
    Robot robot = cheecsemanager.getRobot();
    Optional<CheeseWheel> currentCheeseWheel = Optional.ofNullable(robot.getCurrentCheeseWheel());

    if (currentCheeseWheel.isPresent()){
      throw new RuntimeException("Current cheese wheel does exist with id:" + currentCheeseWheel.get().getId());
    }
  }

  /**
   * @author Ayush Patel
   */
  @Then("the action log of the robot shall be empty")
  public void the_action_log_of_the_robot_shall_be_empty() {
    Robot robot = cheecsemanager.getRobot();
    Optional<List<LogEntry>> actionLog = Optional.ofNullable(robot.getLog());

    if (actionLog.isPresent() && !actionLog.get().isEmpty()) {
      throw new RuntimeException("Action log is not empty. Current log size: " + actionLog.get().size());
    }
  }

  /**\
   * @author Ayush Patel
   */
  @When("the facility manager attempts to initialize the robot with shelf {string}")
  public void the_facility_manager_attempts_to_initialize_the_robot_with_shelf(String shelfId) {
    // Write code here that turns the phrase above into concrete actions
    try{
      RobotController.initializeRobot(shelfId);
    } catch(Exception e){
      error = e;
    }
  }

  @When("the facility manager attempts to view the action log of the robot")
  public void the_facility_manager_attempts_to_view_the_action_log_of_the_robot() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the presented action log of the robot shall be empty")
  public void the_presented_action_log_of_the_robot_shall_be_empty() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the error {string} shall be raised")
  public void the_error_shall_be_raised(String string) {
    // Write code here that turns the phrase above into concrete actions
    if (error == null) {
      throw new RuntimeException("Expected an error to be raised, but none was thrown.");
    }
    if (!string.equals(error.getMessage())) {
      throw new RuntimeException("Expected error message: \"" + string +
              "\" but got: \"" + error.getMessage() + "\"");
    }
  }

  @Then("the current shelf of the robot shall be {string}")
  public void the_current_shelf_of_the_robot_shall_be(String string) {
    // Write code here that turns the phrase above into concrete actions
    Robot robot = cheecsemanager.getRobot();
    Shelf currShelf = robot.getCurrentShelf();
    String currentShelfId = currShelf.getId();
    if (currentShelfId == null) {
      throw new RuntimeException("Robot has no current shelf");
    }

    if (!string.equals(currentShelfId)) {
      throw new RuntimeException("Unexpected value. Expected: " + string + ", Actual: " + currentShelfId);
    }
  }

  @Then("the action log of the robot shall be {string}")
  public void the_action_log_of_the_robot_shall_be(String string) {
    // Write code here that turns the phrase above into concrete actions
    Robot robot = cheecsemanager.getRobot();
    Optional<List<LogEntry>> logs = Optional.ofNullable(robot.getLog());
    String allLogs = "";
    if (logs.isPresent()) {
      for (LogEntry logEntry : logs.get()) {
        allLogs += logEntry.toString();
      }
    } else {
      throw new RuntimeException("Robot has no current log");
    }

    if(!allLogs.equals(string)){
      throw new RuntimeException("Unexpected value. Expected: " + string + ", Actual: " + allLogs);
    }
  }

  @Then("the presented action log of the robot shall be {string}")
  public void the_presented_action_log_of_the_robot_shall_be(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the number of robots in the system shall be {int}")
  public void the_number_of_robots_in_the_system_shall_be(Integer int1) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  /**
   * @author Ayush Patel
   * */
  @Then("the current cheese wheel of the robot shall {int}")
  public void the_current_cheese_wheel_of_the_robot_shall(Integer wheelId) {
    // Write code here that turns the phrase above into concrete actions
    Robot robot = cheecsemanager.getRobot();
    CheeseWheel currentCheeseWheel = robot.getCurrentCheeseWheel();
    if (currentCheeseWheel == null) {
      throw new RuntimeException("Robot has no current cheese wheel");
    }
    if(!currentCheeseWheel.equals(cheecsemanager.getCheeseWheel(wheelId))) {
      throw new RuntimeException("Unexpected value. Expected: " + wheelId + ", Actual: " + currentCheeseWheel.getId());
    }
  }

  /**
   * @author Ayush Patel
   * */
  @Then("the current cheese wheel of the robot shall be {int}")
  public void the_current_cheese_wheel_of_the_robot_shall_be(Integer wheelId) {
    Robot robot = cheecsemanager.getRobot();
    CheeseWheel currentCheeseWheel = robot.getCurrentCheeseWheel();
    if (currentCheeseWheel == null) {
      throw new RuntimeException("Robot has no current cheese wheel");
    }
    if(!currentCheeseWheel.equals(cheecsemanager.getCheeseWheel(wheelId))) {
      throw new RuntimeException("Unexpected value. Expected: " + wheelId + ", Actual: " + currentCheeseWheel.getId());
    }
  }
}
