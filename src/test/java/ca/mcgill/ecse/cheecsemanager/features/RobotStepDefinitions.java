package ca.mcgill.ecse.cheecsemanager.features;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Collectors;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.RobotController;
import ca.mcgill.ecse.cheecsemanager.model.*;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;

import static org.junit.Assert.*;

public class RobotStepDefinitions {
  private static CheECSEManager cheecsemanager = CheECSEManagerApplication.getCheecseManager();
  private static Exception error;
  private static Robot robot;

  private Robot getRobot(){
    if (robot == null) {
      robot = cheecsemanager.hasRobot() ? cheecsemanager.getRobot() : new Robot(null, false, cheecsemanager);
    }
    return robot;
  }

  /**
   * @author Ayush Patel
   * This method is used in given steps to set the status of the robot to the desired status
   * */
  private void setStatus(Robot.Status targetStatus, Shelf shelf, CheeseWheel cheeseWheel) {
    Robot robot = getRobot();
    if (shelf != null){
      robot.setCurrentShelf(shelf);
    }

    if (cheeseWheel != null){
      robot.setCurrentCheeseWheel(cheeseWheel);
    }

    if (targetStatus.equals(Robot.Status.Idle)){
      robot.setIsActivated(true);
      return;
    }

    robot.activate();

    if (targetStatus.equals(Robot.Status.AtEntranceFacingAisle)) {
      robot.turnLeft();
      robot.setRow(1);
      robot.setColumn(0);
    } else if (targetStatus.equals(Robot.Status.AtCheeseWheel)) {
      robot.turnLeft();
      if (cheeseWheel != null) {
        robot.moveToCheeseWheel(cheeseWheel);
        robot.setRow(cheeseWheel.getLocation().getRow());
        robot.setColumn(cheeseWheel.getLocation().getColumn());
      } else {
        // Assume that the robot is at the location of the
        // placeHolderCheeseWheel which is some random wheel.
        // This must done to successfully change the state to AtCheeseWheel
        Optional<CheeseWheel> placeHolderCheeseWheel = cheecsemanager.getCheeseWheels().stream().
                filter(cheeseWheel_ -> !cheeseWheel_.getIsSpoiled()).
                findFirst();
        robot.setCurrentShelf(placeHolderCheeseWheel.get().getLocation().getShelf());
        robot.setRow(placeHolderCheeseWheel.get().getLocation().getRow());
        robot.setColumn(placeHolderCheeseWheel.get().getLocation().getColumn());
        robot.moveToCheeseWheel(placeHolderCheeseWheel.get());
      }
    }
  }

  /**
   * This method is used to clear the robot's information
   * before doing some other operations
   * */
  @Before
  public void initializeRobot(){
    Robot robot = getRobot();
    robot.setCurrentCheeseWheel(null);
    robot.setCurrentShelf(null);
    Robot.Status currentStatus = robot.getStatus();

    switch (currentStatus){
      case AtEntranceNotFacingAisle:
        robot.turnLeft();
        robot.deactivate();
        break;
      case AtEntranceFacingAisle:
        robot.deactivate();
        break;
      case AtCheeseWheel:
        robot.deactivate();
    }
    while (robot.numberOfLog() > 0) {
      LogEntry logEntry = robot.getLog(robot.numberOfLog() - 1);
      logEntry.delete();
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
    Shelf shelf = Shelf.getWithId(shelfId);
    switch (state) {
      case "Idle":
        setStatus(Robot.Status.Idle, shelf, null);
        break;
      case "AtEntranceFacingAisle":
        setStatus(Robot.Status.AtEntranceFacingAisle, shelf, null);
        break;
      case "AtEntranceNotFacingAisle":
        setStatus(Robot.Status.AtEntranceNotFacingAisle, shelf, null);
        break;
      case "AtCheeseWheel":
        setStatus(Robot.Status.AtCheeseWheel, shelf, null);
        break;
      default:
        throw new RuntimeException("Unknown state: " + state);
    }

    Robot robot = getRobot();
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
    switch (state) {
      case "Idle":
        setStatus(Robot.Status.Idle, null, null);
        break;
      case "AtEntranceFacingAisle":
        setStatus(Robot.Status.AtEntranceFacingAisle, null, null);
        break;
      case "AtEntranceNotFacingAisle":
        setStatus(Robot.Status.AtEntranceNotFacingAisle, null, null);
        break;
      case "AtCheeseWheel":
        setStatus(Robot.Status.AtCheeseWheel, null, null);
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
    Optional<CheeseWheel> cheeseWheel = cheecsemanager.getCheeseWheels().stream()
            .filter(wheel -> wheel.getId() == wheelId)
            .findFirst();
    Shelf shelf = Shelf.getWithId(shelfId);
    switch (state) {
      case "AtCheeseWheel":
        setStatus(Robot.Status.AtCheeseWheel, shelf, cheeseWheel.get());
        break;
      default:
        throw new RuntimeException("Unsupported state for this state: " + state);
    }

    Robot robot = getRobot();
    while (robot.numberOfLog() > 0) {
      LogEntry logEntry = robot.getLog(robot.numberOfLog() - 1);
      logEntry.delete();
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

  /**\
   * @author Olivier Mao
   */
  @When("the robot controller attempts to trigger the robot to perform treatment")
  public void the_robot_controller_attempts_to_trigger_the_robot_to_perform_treatment() {
    // skibdi
    try {
      int lastIndex = cheecsemanager.getTransactions().size() - 1;
      RobotController.initializeTreatment(lastIndex);
    } catch (Exception e) {
      error = e;
    }
  }

  /**\
   * @author Ayush Patel
   */
  @When("the robot controller attempts to move the robot to shelf {string}")
  public void the_robot_controller_attempts_to_move_the_robot_to_shelf(String shelfId) {
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
    Robot.Status expectedStatus = switch(expectedStatusString) {
      case "Idle" -> Robot.Status.Idle;
      case "AtCheeseWheel" -> Robot.Status.AtCheeseWheel;
      case "AtEntranceNotFacingAisle" -> Robot.Status.AtEntranceNotFacingAisle;
      case "AtEntranceFacingAisle" -> Robot.Status.AtEntranceFacingAisle;
      default -> throw new IllegalArgumentException("Unknown status:" + expectedStatusString);
    };
  assertEquals(expectedStatus, getRobot().getStatus());
  }

  /**
   * @author Ayush Patel
   */
  @Then("the current shelf of the robot shall be not specified")
  public void the_current_shelf_of_the_robot_shall_be_not_specified() {
      assertNull(getRobot().getCurrentShelf());
  }

  /**
   * @author Ayush Patel
   */
  @Then("the current cheese wheel of the robot shall be not specified")
  public void the_current_cheese_wheel_of_the_robot_shall_be_not_specified() {
      assertNull(getRobot().getCurrentCheeseWheel());
  }

  /**
   * @author Ayush Patel
   */
  @Then("the action log of the robot shall be empty")
  public void the_action_log_of_the_robot_shall_be_empty() {
      assertTrue(getRobot().getLog().isEmpty());
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
    RobotController.viewLog();
  }

  @Then("the presented action log of the robot shall be empty")
  public void the_presented_action_log_of_the_robot_shall_be_empty() {
    assertTrue(getRobot().getLog().isEmpty());
  }

  @Then("the error {string} shall be raised")
  public void the_error_shall_be_raised(String string) {
    assertEquals(string, error.getMessage());
  }

  @Then("the current shelf of the robot shall be {string}")
  public void the_current_shelf_of_the_robot_shall_be(String string) {
    assertEquals(string,getRobot().getCurrentShelf().getId());
  }

  @Then("the action log of the robot shall be {string}")
  public void the_action_log_of_the_robot_shall_be(String string) {
    List<LogEntry> logs = getRobot().getLog();
    String logString = logs.stream().map(LogEntry::getDescription).collect(Collectors.joining(" "));
    assertEquals(string, logString);
  }

  @Then("the presented action log of the robot shall be {string}")
  public void the_presented_action_log_of_the_robot_shall_be(String string) {
    List<LogEntry> logs = getRobot().getLog();
    String logString = logs.stream().map(LogEntry::getDescription).collect(Collectors.joining(" "));
    assertEquals(string, logString);
  }

  @Then("the number of robots in the system shall be {int}")
  public void the_number_of_robots_in_the_system_shall_be(Integer expectedNumberOfRobots) {
    Integer actualNumberOfRobots = cheecsemanager.hasRobot() ? 1: 0;
    assertEquals(expectedNumberOfRobots, actualNumberOfRobots);
  }

  /**
   * @author Ayush Patel
   * */
  @Then("the current cheese wheel of the robot shall {int}")
  public void the_current_cheese_wheel_of_the_robot_shall(Integer wheelId) {
    assertEquals(wheelId, Optional.of(getRobot().getCurrentCheeseWheel().getId()).get());
  }

  /**
   * @author Ayush Patel
   * */
  @Then("the current cheese wheel of the robot shall be {int}")
  public void the_current_cheese_wheel_of_the_robot_shall_be(Integer wheelId) {
    assertEquals(wheelId, Optional.of(getRobot().getCurrentCheeseWheel().getId()).get());
  }
}
