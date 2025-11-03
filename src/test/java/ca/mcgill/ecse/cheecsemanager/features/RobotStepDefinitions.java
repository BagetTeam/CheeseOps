package ca.mcgill.ecse.cheecsemanager.features;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.LogAction;
import ca.mcgill.ecse.cheecsemanager.controller.RobotController;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import ca.mcgill.ecse.cheecsemanager.model.Robot.Status;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.LogEntry;
import ca.mcgill.ecse.cheecsemanager.model.Order;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.Robot;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;
import ca.mcgill.ecse.cheecsemanager.model.Transaction;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RobotStepDefinitions {
  private CheECSEManager cheecsemanager = CheECSEManagerApplication.getCheecseManager();

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
   * @param String shelfId: The id of the shelf to create locations for
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
      Date transactionDate = Date.valueOf(purchase.get("transactionDate"));
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
   * @param shelfID The ID of the shelf
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

  /** Set all cheese wheels given in to spoiled.
   * Table columns expected: id
   * 
   * @param dataTable Cucumber datatable with cheese wheel rows (id)
   * @author Ewen Gueguen
   */
  @Given("the following cheese wheels are spoiled")
  public void the_following_cheese_wheels_are_spoiled(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> cheeseWheels = dataTable.asMaps();
    for (var cheeseWheel : cheeseWheels) {
      int id = Integer.parseInt(cheeseWheel.get("id"));
      cheecsemanager.getCheeseWheel(id).setIsSpoiled(true);
    }
  }

  /** 
   * Sets the robot to a specific state, shelf, and action log for testing.
   * Uses reflection to set the private status field.
   * 
   * @param state The robot status (e.g., "AtEntranceNotFacingAisle")
   * @param shelfID The shelf ID where the robot is located
   * @param initialLog The action log string (may contain multiple entries separated by "; ")
   * @author: Ewen Gueguen
   */ 
  @Given("the robot is marked as {string} and at shelf {string} with action log {string}")
  public void the_robot_is_marked_as_and_at_shelf_with_action_log(String state, String shelfID,
      String initialLog) {
    try {
      Status status = Status.valueOf(state);
      Robot robot = cheecsemanager.getRobot();
      
      if (robot == null) {
        throw new RuntimeException("Robot does not exist. Robot must be created first.");
      }
      
      Shelf shelf = Shelf.getWithId(shelfID);
      if (shelf == null) {
        throw new RuntimeException("Shelf " + shelfID + " does not exist.");
      }
      
      robot.setCurrentShelf(shelf);
      
      // Clear existing logs by deleting them
      while (robot.numberOfLog() > 0) {
        LogEntry logEntry = robot.getLog(robot.numberOfLog() - 1);
        logEntry.delete();
      }
      
      // Parse and add log entries from the initialLog string
      if (initialLog != null && !initialLog.trim().isEmpty()) {
        // Get loga seperated by ;
        String[] logParts = initialLog.split("; ");
        for (String logPart : logParts) {
          if (logPart.trim().isEmpty()) {
            continue;
          }
          // Add semicolon back
          String logEntry = logPart.trim();
          if (!logEntry.endsWith(";")) {
            logEntry += ";";
          }
          robot.addLog(logEntry);
        }
      }
      
      // Use reflection to set the private status field
      try {
        java.lang.reflect.Field statusField = Robot.class.getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(robot, status);
      } catch (Exception e) {
        throw new RuntimeException("Failed to set robot status via reflection: " + e.getMessage(), e);
      }
      
      // Set activation state based on status
      if (status != Status.Idle) {
        robot.setIsActivated(true);
      } else {
        robot.setIsActivated(false);
      }
      
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid robot status: " + state, e);
    }
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

  // TODO Need controller method
  @Given("the robot is marked as {string}")
  public void the_robot_is_marked_as(String state) {
    Status status = Status.valueOf(state);
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
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

  // To do once controller has been implemented
  @Given("the robot is marked as {string} and at cheese wheel {int} on shelf {string} with action log {string}")
  public void the_robot_is_marked_as_and_at_cheese_wheel_on_shelf_with_action_log(String string,
      Integer int1, String string2, String string3) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  /**
   * Adds all non-spoiled cheese wheels from a given purchase to a given order. 
   * Uses model API to get Purchase and Order objects
   * 
   * @param int1 index of the purchase to take cheese wheels from 
   * @param int2 index of the order to which the cheese wheels will be added
   * @throws IlegalArgumentException if either the purchase or the order does not exist
   * @author Eun-jun Chang
   */
  @Given("all non-spoiled cheese wheels from purchase {int} are added to order {int}")
  public void all_non_spoiled_cheese_wheels_from_purchase_are_added_to_order(Integer purchaseId,
      Integer orderId) {
    var manager = CheECSEManagerApplication.getCheecseManager();
    Purchase purchase = null;
    Order order = null;

    for(Transaction t : manager.getTransactions()) {
    	if (t instanceof Order && t.getId() == orderId) {
    	    order = (Order) t;
    	    break;
    	} else if (t instanceof Purchase && t.getId() == purchaseId) {
    	    purchase = (Purchase) t;
    	    break;
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

  @When("the facility manager attempts to activate the robot")
  public void the_facility_manager_attempts_to_activate_the_robot() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the robot controller attempts to turn the robot left")
  public void the_robot_controller_attempts_to_turn_the_robot_left() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the robot controller attempts to turn the robot right")
  public void the_robot_controller_attempts_to_turn_the_robot_right() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the robot controller attempts to move the robot to cheese wheel {int}")
  public void the_robot_controller_attempts_to_move_the_robot_to_cheese_wheel(Integer int1) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the robot controller attempts to move the robot to the entrance")
  public void the_robot_controller_attempts_to_move_the_robot_to_the_entrance() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the robot controller attempts to trigger the robot to perform treatment")
  public void the_robot_controller_attempts_to_trigger_the_robot_to_perform_treatment() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the robot controller attempts to move the robot to shelf {string}")
  public void the_robot_controller_attempts_to_move_the_robot_to_shelf(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the facility manager attempts to trigger the robot to perform treatment on {string} old cheese wheels of purchase {int}")
  public void the_facility_manager_attempts_to_trigger_the_robot_to_perform_treatment_on_old_cheese_wheels_of_purchase(
      String string, Integer int1) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the facility manager attempts to deactivate the robot")
  public void the_facility_manager_attempts_to_deactivate_the_robot() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the robot shall be marked as {string}")
  public void the_robot_shall_be_marked_as(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the current shelf of the robot shall be not specified")
  public void the_current_shelf_of_the_robot_shall_be_not_specified() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the current cheese wheel of the robot shall be not specified")
  public void the_current_cheese_wheel_of_the_robot_shall_be_not_specified() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the action log of the robot shall be empty")
  public void the_action_log_of_the_robot_shall_be_empty() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the facility manager attempts to initialize the robot with shelf {string}")
  public void the_facility_manager_attempts_to_initialize_the_robot_with_shelf(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
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
    throw new io.cucumber.java.PendingException();
  }

  @Then("the current shelf of the robot shall be {string}")
  public void the_current_shelf_of_the_robot_shall_be(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the action log of the robot shall be {string}")
  public void the_action_log_of_the_robot_shall_be(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
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

  @Then("the current cheese wheel of the robot shall {int}")
  public void the_current_cheese_wheel_of_the_robot_shall(Integer int1) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the current cheese wheel of the robot shall be {int}")
  public void the_current_cheese_wheel_of_the_robot_shall_be(Integer int1) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }
}
