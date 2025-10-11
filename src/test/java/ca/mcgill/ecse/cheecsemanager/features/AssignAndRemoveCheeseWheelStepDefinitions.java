package ca.mcgill.ecse.cheecsemanager.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet4Controller;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;;

/**
 * Step definitions for Feature P7: Assign and Remove Cheese Wheel
 * 
 * @author Audrey Lachance
 * @author Sarah Sabbagh
 * @author Jenny Saad
 * @author Mohamed Sadqui
 */

public class AssignAndRemoveCheeseWheelStepDefinitions {
  private CheECSEManager cheecsemanager = CheECSEManagerApplication.getCheecseManager();
  private String error = "";

  private Map<String, int[]> shelfDimensions = new HashMap<>();
  private List<Integer> nrCheeseWheels = new ArrayList<>();
  private List<String> monthAgeds = new ArrayList<>();

  /**
   * Gherkin Step: Given("the following farmer exists in the system \\(p7)") implemented by: Audrey
   * Lachance
   */
  @Given("the following farmer exists in the system \\(p7)")
  public void the_following_farmer_exists_in_the_system_p7(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String email = row.get("email");
      String password = row.get("password");
      String address = row.get("address");
      String name = row.get("name");

      Farmer farmer = new Farmer(email, password, address, cheecsemanager);
      farmer.setName(name);
    }
  }

  /**
   * Gherkin Step: Given("cheese wheel {int} is at shelf location with column {int} and row {int} of
   * shelf {string} \\(p7)") implemented by: Audrey Lachance
   */
  @Given("cheese wheel {int} is at shelf location with column {int} and row {int} of shelf {string} \\(p7)")
  public void cheese_wheel_is_at_shelf_location_with_column_and_row_of_shelf_p7(
      Integer cheeseWheelID, Integer column, Integer row, String shelfID) {

    CheeseWheel cheeseWheel = cheecsemanager.getCheeseWheel(cheeseWheelID - 1);

    Shelf shelf = Shelf.getWithId(shelfID);

    ShelfLocation location = null;
    for (int i = 0; i < shelf.getLocations().size(); i++) {
      ShelfLocation newloc = shelf.getLocations().get(i);
      if (newloc.getColumn() == column && newloc.getRow() == row) {
        location = newloc;
        break;
      }
    }

    if (location == null) {
      location = shelf.addLocation(column, row);
    }

    if (!location.hasCheeseWheel()) {
      cheeseWheel.setLocation(location);
    }
  }

  /**
   * Gherkin Step: Given("the following shelf exists in the system \\(p7)") implemented by: Audrey
   * Lachance
   */
  @Given("the following shelf exists in the system \\(p7)")
  public void the_following_shelf_exists_in_the_system_p7(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps();

    for (var row : rows) {
      String id = row.get("id");
      int nrColumns = Integer.parseInt(row.get("nrColumns"));
      int nrRows = Integer.parseInt(row.get("nrRows"));

      shelfDimensions.put(id, new int[] {nrColumns, nrRows});
      cheecsemanager.addShelve(id);
    }
  }

  /**
   * Gherkin Step: Given("all locations are created for shelf {string} \\(p7)") implemented by:
   * Audrey Lachance
   */
  @Given("all locations are created for shelf {string} \\(p7)")
  public void all_locations_are_created_for_shelf_p7(String shelfID) {

    Shelf shelf = Shelf.getWithId(shelfID);

    int[] dimensions = shelfDimensions.get(shelfID);

    int nrColumns = dimensions[0];
    int nrRows = dimensions[1];

    for (int i = 1; i <= nrRows; i++) {
      for (int j = 1; j <= nrColumns; j++) {
        new ShelfLocation(j, i, shelf);
      }
    }
  }

  /**
   * Gherkin Step: Given("the following purchase exists in the system \\(p7)") implemented by: Sarah
   * Sabbagh
   */
  @Given("the following purchase exists in the system \\(p7)")
  public void the_following_purchase_exists_in_the_system_p7(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps();

    for (Map<String, String> row : rows) {
      String purchaseDate = row.get("purchaseDate");
      int nrCheeseWheelsInt = Integer.parseInt(row.get("nrCheeseWheels"));
      String monthsAged = row.get("monthsAged");
      String farmerEmail = row.get("farmerEmail");


      Date date = Date.valueOf(purchaseDate);


      Farmer farmer = null;

      // This is the only way to find a farmer with an email I believe
      for (Farmer farmerOption : cheecsemanager.getFarmers()) {
        if (farmerOption.getEmail().equals(farmerEmail)) {
          farmer = farmerOption;
          break;
        }
      }

      new Purchase(date, cheecsemanager, farmer);

      nrCheeseWheels.add(nrCheeseWheelsInt);
      monthAgeds.add(monthsAged);
    }
  }

  /**
   * Gherkin Step: Given("all cheese wheels from purchase {int} are created \\(p7)") implemented by:
   * Sarah Sabbagh
   */
  @Given("all cheese wheels from purchase {int} are created \\(p7)")
  public void all_cheese_wheels_from_purchase_are_created_p7(Integer purchaseNbr) {

    purchaseNbr = purchaseNbr - 1;

    int nrCheeseWheel = nrCheeseWheels.get(purchaseNbr);
    String monthsAged = monthAgeds.get(purchaseNbr);
    Purchase purchase = (Purchase) cheecsemanager.getTransactions().get(purchaseNbr);
    MaturationPeriod period = MaturationPeriod.valueOf(monthsAged);

    for (int i = 0; i < nrCheeseWheel; i++) {
      new CheeseWheel(period, false, purchase, cheecsemanager);
    }
  }

  /**
   * Gherkin Step: Given("cheese wheel {int} is spoiled \\(p7)") implemented by: Sarah Sabbagh
   */
  @Given("cheese wheel {int} is spoiled \\(p7)")
  public void cheese_wheel_is_spoiled_p7(Integer cheeseWheelId) {

    CheeseWheel cheeseWheel = cheecsemanager.getCheeseWheel(cheeseWheelId - 1);
    cheeseWheel.setIsSpoiled(true);
  }

  /**
   * Gherkin Step: When("the facility manager attempts to assign cheese wheel {int} to shelf
   * location with column {int} and row {int} of shelf {string} \\(p7)") implemented by: Audrey
   * Lachance
   */
  @When("the facility manager attempts to assign cheese wheel {int} to shelf location with column {int} and row {int} of shelf {string} \\(p7)")
  public void the_facility_manager_attempts_to_assign_cheese_wheel_to_shelf_location_with_column_and_row_of_shelf_p7(
      Integer cheeseWheelId, Integer columnId, Integer rowId, String shelfId) {
    callController(CheECSEManagerFeatureSet4Controller.assignCheeseWheelToShelf(cheeseWheelId,
        shelfId, columnId, rowId));

  }

  /**
   * Gherkin Step: When("the facility manager attempts to remove cheese wheel {int} from its shelf
   * location \\(p7)") implemented by:Sarah Sabbagh
   */
  @When("the facility manager attempts to remove cheese wheel {int} from its shelf location \\(p7)")
  public void the_facility_manager_attempts_to_remove_cheese_wheel_from_its_shelf_location_p7(
      Integer cheeseWheelId) {

    callController(CheECSEManagerFeatureSet4Controller.removeCheeseWheelFromShelf(cheeseWheelId));

  }

  /**
   * Gherkin Step: Then("the cheese wheel {int} shall be at shelf location with column {int} and row
   * {int} of shelf {string} \\(p7)") implemented by: Audrey Lachance
   */
  @Then("the cheese wheel {int} shall be at shelf location with column {int} and row {int} of shelf {string} \\(p7)")
  public void the_cheese_wheel_shall_be_at_shelf_location_with_column_and_row_of_shelf_p7(
      Integer cheeseWheelId, Integer columnId, Integer rowId, String shelfId) {
    
    assertTrue(cheecsemanager.getCheeseWheels().size() >= cheeseWheelId,
        "Cheese wheel with ID " + cheeseWheelId + " does not exist.");
    CheeseWheel cheeseWheel = cheecsemanager.getCheeseWheel(cheeseWheelId - 1);

    ShelfLocation location = cheeseWheel.getLocation();

    assertEquals(columnId, location.getColumn());
    assertEquals(rowId, location.getRow());
    assertEquals(shelfId, location.getShelf().getId());
  }

  /**
   * Gherkin Step: Then("the number of cheese wheels on shelf {string} shall be {int} \\(p7)")
   * implemented by: Sarah Sabbagh
   */
  @Then("the number of cheese wheels on shelf {string} shall be {int} \\(p7)")
  public void the_number_of_cheese_wheels_on_shelf_shall_be_p7(String shelfId, Integer count) {

    Shelf shelf = Shelf.getWithId(shelfId);

    int actualCount = 0;

    assertNotNull(shelf, "Shelf with ID " + shelfId + " does not exist.");
    for (ShelfLocation location : shelf.getLocations()) {
      if (location.getCheeseWheel() != null) {
        actualCount++;
      }
    }
    

    assertEquals(count, actualCount);
  }

  /**
   * Gherkin Step: Then("the error {string} shall be raised \\(p7)") implemented by: Sarah Sabbagh
   */
  @Then("the error {string} shall be raised \\(p7)")
  public void the_error_shall_be_raised_p7(String expectedError) {

    assertTrue(error.contains(expectedError),
        "Expected the error message to contain: " + expectedError + " but got:" + error);
  }

  /**
   * Gherkin Step: Then("cheese wheel {int} shall not be on any shelf \\(p7)") implemented by: Sarah
   * Sabbagh
   */
  @Then("cheese wheel {int} shall not be on any shelf \\(p7)")
  public void cheese_wheel_shall_not_be_on_any_shelf_p7(Integer cheeseWheelId) {
    assertTrue(cheecsemanager.getCheeseWheels().size() >= cheeseWheelId,
        "Cheese wheel with ID " + cheeseWheelId + " does not exist.");
    CheeseWheel cheeseWheel = cheecsemanager.getCheeseWheel(cheeseWheelId - 1);

    assertNull(cheeseWheel.getLocation(),
        "Cheese wheel with ID " + cheeseWheelId + " is still assigned to a shelf location.");
  }

  private void callController(String result) {
    if (!(result.isEmpty()) && result != null) {
      error = result;
    }
  }
}
