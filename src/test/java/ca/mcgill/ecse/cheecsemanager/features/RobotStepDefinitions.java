package ca.mcgill.ecse.cheecsemanager.features;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
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
      for (int i = 0; i < nrCheeseWheels; i++) {
        addedPurchase.addCheeseWheel(monthsAged, false, cheecsemanager);
      }
    }
  }

  @Given("all cheese wheels for the following purchases are created")
  public void all_cheese_wheels_for_the_following_purchases_are_created(
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    throw new io.cucumber.java.PendingException();
  }

  @Given("cheese wheel {int} is at shelf location with column {int} and row {int} of shelf {string}")
  public void cheese_wheel_is_at_shelf_location_with_column_and_row_of_shelf(Integer int1,
      Integer int2, Integer int3, String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Given("the following cheese wheels are spoiled")
  public void the_following_cheese_wheels_are_spoiled(io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    throw new io.cucumber.java.PendingException();
  }

  @Given("the robot is marked as {string} and at shelf {string} with action log {string}")
  public void the_robot_is_marked_as_and_at_shelf_with_action_log(String string, String string2,
      String string3) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
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

  @Given("the following order exists in the system")
  public void the_following_order_exists_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    throw new io.cucumber.java.PendingException();
  }

  @Given("the robot is marked as {string} and at cheese wheel {int} on shelf {string} with action log {string}")
  public void the_robot_is_marked_as_and_at_cheese_wheel_on_shelf_with_action_log(String string,
      Integer int1, String string2, String string3) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Given("all non-spoiled cheese wheels from purchase {int} are added to order {int}")
  public void all_non_spoiled_cheese_wheels_from_purchase_are_added_to_order(Integer int1,
      Integer int2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
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
