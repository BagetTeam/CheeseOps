package ca.mcgill.ecse.cheecsemanager.features;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Order;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;
import ca.mcgill.ecse.cheecsemanager.model.Transaction;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateCheeseWheelStepDefinitions {

  CheECSEManager ecseManager = CheECSEManagerApplication.getCheecseManager();
  private Map<String, int[]> shelfMetaData = new HashMap<>();
  private String monthsAgedPurchase;
  private Integer nrCheeseWheelsPurchase;
  private String capturedError;

  /**
   * @author Shihab Berel
   */
  @Given("the following farmer exists in the system \\(p6)")
  public void the_following_farmer_exists_in_the_system_p6(
      io.cucumber.datatable.DataTable farmerDataTable) {

    // convert the data table from the feature file into a list of maps
    List<Map<String, String>> rows = farmerDataTable.asMaps(String.class, String.class);

    // for each farmer listed in the table, add them through the manager
    for (Map<String, String> row : rows) {
      String email = row.get("email");
      String password = row.get("password");
      String address = row.get("address");
      String name = row.get("name");

      Farmer farmer = new Farmer(email, password, address, ecseManager);
      farmer.setName(name);
    }
  }

  /**
   * @author Zhi Heng Che
   */
  @Given("the following order exists in the system \\(p6)")
  public void the_following_order_exists_in_the_system_p6(
      io.cucumber.datatable.DataTable orderDataTable) {

    List<Map<String, String>> orders = orderDataTable.asMaps(String.class, String.class);

    for (Map<String, String> orderData : orders) {
      Date transactionDate = Date.valueOf(orderData.get("transactionDate"));
      int nrCheeseWheels = Integer.parseInt(orderData.get("nrCheeseWheels"));
      CheeseWheel.MaturationPeriod monthsAged =
          CheeseWheel.MaturationPeriod.valueOf(orderData.get("monthsAged"));
      Date deliveryDate = Date.valueOf(orderData.get("deliveryDate"));
      String companyName = orderData.get("companyName");

      // Find or create the wholesale company
      WholesaleCompany company = WholesaleCompany.getWithName(companyName);

      // If company doesn't exist, create it with a default address
      if (company == null) {
        company = ecseManager.addCompany(companyName, "Default Address");
      }

      // Create the order with the correct constructor signature
      new Order(transactionDate, ecseManager, nrCheeseWheels, monthsAged, deliveryDate, company);
    }
  }

  /**
   * @author Zhi Heng Che
   */
  @Given("all non-spoiled cheese wheels from purchase {int} are added to order {int} \\(p6)")
  public void all_non_spoiled_cheese_wheels_from_purchase_are_added_to_order_p6(
      Integer purchaseID, Integer orderID) {

    Order targetOrder = null;
    Purchase targetPurchase = null;

    for (Transaction transaction : ecseManager.getTransactions()) {
      if (transaction instanceof Order && transaction.getId() == orderID) {
        targetOrder = (Order) transaction;
      }
      if (transaction instanceof Purchase && transaction.getId() == purchaseID) {
        targetPurchase = (Purchase) transaction;
      }
    }

    for (CheeseWheel cheeseWheel : targetPurchase.getCheeseWheels()) {
      if (!cheeseWheel.getIsSpoiled()) {
        targetOrder.addCheeseWheel(cheeseWheel);
      }
    }
  }

  /**
   * @author Eliott Kohn
   */
  @Given("the following shelf exists in the system \\(p6)")
  public void the_following_shelf_exists_in_the_system_p6(
      io.cucumber.datatable.DataTable shelfDataTable) {
    List<Map<String, String>> rows = shelfDataTable.asMaps();

    // For each row in the table, create a shelf using the provided ID
    for (var row : rows) {
      String shelfId = row.get("id");
      int nrColumns = Integer.parseInt(row.get("nrColumns"));
      int nrRows = Integer.parseInt(row.get("nrRows"));

      // Add shelf
      ecseManager.addShelve(shelfId);

      // Store the shelf dimensions in metadata map for later use
      shelfMetaData.put(shelfId, new int[] {nrColumns, nrRows});
    }
  }

  /**
   * @author Louis Salanon
   */
  @Given("all locations are created for shelf {string} \\(p6)")
  public void all_locations_are_created_for_shelf_p6(String shelfId) {

    Shelf shelf = Shelf.getWithId(shelfId);

    // Get the shelf dimensions from the metadata map that was populated
    int[] dimensions = shelfMetaData.get(shelfId);
    int nrColumns = dimensions[0];
    int nrRows = dimensions[1];

    // Create all locations for this shelf
    for (int r = 1; r <= nrRows; r++) {
      for (int c = 1; c <= nrColumns; c++) {
        shelf.addLocation(c, r);
      }
    }
  }

  /**
   * @author Zhi Heng Che
   */
  @Given(
      "cheese wheel {int} is at shelf location with column {int} and row {int} of shelf {string} \\(p6)")
  public void cheese_wheel_is_at_shelf_location_with_column_and_row_of_shelf_p6(
      Integer cheeseWheelID, Integer shelfColumn, Integer shelfRow, String shelfID) {

    CheeseWheel targetCheeseWheel = null;
    Shelf targetShelf = null;

    for (Shelf shelf : ecseManager.getShelves()) {
      if (shelf.getId().equals(shelfID)) {
        targetShelf = shelf;
        break;
      }
    }

    for (CheeseWheel cheeseWheel : ecseManager.getCheeseWheels()) {
      if (cheeseWheel.getId() == cheeseWheelID) {
        targetCheeseWheel = cheeseWheel;
        break;
      }
    }

    for (ShelfLocation shelfLocation : targetShelf.getLocations()) {
      if (shelfLocation.getColumn() == shelfColumn && shelfLocation.getRow() == shelfRow) {
        shelfLocation.setCheeseWheel(targetCheeseWheel);
        break;
      }
    }
  }

  /**
   * @author Louis Salanon
   */
  @Given("the following purchase exists in the system \\(p6)")
  public void the_following_purchase_exists_in_the_system_p6(
      io.cucumber.datatable.DataTable purchaseDataTable) {

    List<Map<String, String>> rows = purchaseDataTable.asMaps(String.class, String.class);

    for (Map<String, String> row : rows) {
      String purchaseDateStr = row.get("purchaseDate");
      this.nrCheeseWheelsPurchase = Integer.parseInt(row.get("nrCheeseWheels"));
      this.monthsAgedPurchase = row.get("monthsAged");
      String farmerEmail = row.get("farmerEmail");

      Farmer farmer = (Farmer) Farmer.getWithEmail(farmerEmail);
      Date purchaseDate = Date.valueOf(purchaseDateStr);
      Purchase purchase = new Purchase(purchaseDate, ecseManager, farmer);
    }
  }

  /**
   * @author Michael Ha
   */
  @Given("all cheese wheels from purchase {int} are created \\(p6)")
  public void all_cheese_wheels_from_purchase_are_created_p6(Integer purchaseID) {
    // Cheese wheels parameters are assigned in previous method creating the purchases
    Purchase purchase = null;
    for (Transaction t : ecseManager.getTransactions()) {
      if (t instanceof Purchase && t.getId() == purchaseID) {
        purchase = (Purchase) t;
        break;
      }
    }

    CheeseWheel.MaturationPeriod maturationPeriod =
        CheeseWheel.MaturationPeriod.valueOf(monthsAgedPurchase);
    for (int i = 0; i < nrCheeseWheelsPurchase; i++) {
      purchase.addCheeseWheel(maturationPeriod, false, ecseManager);
    }
  }

  /**
   * @author Yohan Le Morhedec
   */
  @Given("cheese wheel {int} is spoiled \\(p6)")
  public void cheese_wheel_is_spoiled_p6(Integer cheeseWheelID) {
    // Find cheese wheel by ID, not by index
    CheeseWheel cw = null;
    for (CheeseWheel wheel : ecseManager.getCheeseWheels()) {
      if (wheel.getId() == cheeseWheelID) {
        cw = wheel;
        break;
      }
    }
    cw.setIsSpoiled(true);
  }

  /**
   * @author Yohan Le Morhedec
   */
  @When(
      "the facility manager attempts to update cheese wheel {int} in the system with isSpoiled {string} and monthsAged {string} \\(p6)")
  public void
      the_facility_manager_attempts_to_update_cheese_wheel_in_the_system_with_is_spoiled_and_months_aged_p6(
          Integer cheeseWheelID, String isSpoiledStr, String monthsAgedStr) {

    Boolean isSpoiled = Boolean.valueOf(isSpoiledStr);

    capturedError =
        CheECSEManagerFeatureSet3Controller.updateCheeseWheel(
            cheeseWheelID, monthsAgedStr, isSpoiled);
  }

  /**
   * @author Shihab Berel
   */
  @Then("the number of cheese wheels in the system shall be {int} \\(p6)")
  public void the_number_of_cheese_wheels_in_the_system_shall_be_p6(Integer nrCheeseWheels) {
    List<CheeseWheel> cheeseWheels = ecseManager.getCheeseWheels();
    Assertions.assertEquals(
        nrCheeseWheels.intValue(),
        cheeseWheels.size(),
        "Unexpected number of cheese wheels in the system.");
  }

  /**
   * @author Maxime Robatche-Claive
   */
  @Then("the purchase {int} shall have {int} cheese wheels \\(p6)")
  public void the_purchase_shall_have_cheese_wheels_p6(Integer purchaseId, Integer expectedCount) {

    Purchase targetPurchase = null;
    for (Transaction transaction : ecseManager.getTransactions()) {
      if (transaction instanceof Purchase && transaction.getId() == purchaseId) {
        targetPurchase = (Purchase) transaction;
        break;
      }
    }

    Assertions.assertNotNull(targetPurchase, "Purchase " + purchaseId + " should exist");
    int actualCount = targetPurchase.numberOfCheeseWheels();
    Assertions.assertEquals(
        expectedCount.intValue(),
        actualCount,
        "Purchase " + purchaseId + " has unexpected cheese-wheel count.");
  }

  /**
   * @author Maxime Robatche-Claive
   */
  @Then(
      "the cheese wheel {int} with monthsAged {string}, isSpoiled {string}, and purchaseId {int} shall exist in the system \\(p6)")
  public void
      the_cheese_wheel_with_months_aged_is_spoiled_and_purchase_id_shall_exist_in_the_system_p6(
          Integer cheeseWheelID, String monthsAgedStr, String isSpoiledStr, Integer purchaseID) {

    // Find cheese wheel by ID, not by index
    CheeseWheel cw = null;
    for (CheeseWheel wheel : ecseManager.getCheeseWheels()) {
      if (wheel.getId() == cheeseWheelID) {
        cw = wheel;
        break;
      }
    }

    Assertions.assertNotNull(cw, "Cheese wheel " + cheeseWheelID + " should exist");

    // monthsAged
    CheeseWheel.MaturationPeriod expectedPeriod =
        CheeseWheel.MaturationPeriod.valueOf(monthsAgedStr);
    Assertions.assertEquals(
        expectedPeriod,
        cw.getMonthsAged(),
        "monthsAged mismatch for cheese wheel " + cheeseWheelID);

    // isSpoiled
    boolean expectedSpoiled = Boolean.parseBoolean(isSpoiledStr);
    Assertions.assertEquals(
        expectedSpoiled, cw.getIsSpoiled(), "isSpoiled mismatch for cheese wheel " + cheeseWheelID);

    // purchaseId
    Assertions.assertNotNull(
        cw.getPurchase(), "Cheese wheel " + cheeseWheelID + " has no purchase");
    Assertions.assertEquals(
        purchaseID.intValue(),
        cw.getPurchase().getId(),
        "purchaseId mismatch for cheese wheel " + cheeseWheelID);
  }

  /**
   * @author Eliott Kohn
   */
  @Then("the cheese wheel {int} shall not be on any shelf \\(p6)")
  public void the_cheese_wheel_shall_not_be_on_any_shelf_p6(Integer cheeseWheelID) {
    // Find cheese wheel by ID, not by index
    CheeseWheel cheeseWheel = null;
    for (CheeseWheel wheel : ecseManager.getCheeseWheels()) {
      if (wheel.getId() == cheeseWheelID) {
        cheeseWheel = wheel;
        break;
      }
    }

    // Verify cheese wheel exists
    Assertions.assertNotNull(
        cheeseWheel, "Expected cheese wheel with id " + cheeseWheelID + " to exist.");

    // Check that it has no location
    ShelfLocation location = cheeseWheel.getLocation();
    Assertions.assertNull(
        location,
        "Expected cheese wheel "
            + cheeseWheelID
            + " to not be on any shelf, but it has a location");
  }

  /**
   * @author Eliott Kohn
   */
  @Then("the number of cheese wheels on shelf {string} shall be {int} \\(p6)")
  public void the_number_of_cheese_wheels_on_shelf_shall_be_p6(
      String shelfID, Integer expectedCount) {
    Shelf shelf = Shelf.getWithId(shelfID);

    // Verify shelf exists
    Assertions.assertNotNull(shelf, "Shelf " + shelfID + " not found in system.");

    // Count the number of cheese wheels currently placed on this shelf
    int actualCount = 0;
    for (ShelfLocation loc : shelf.getLocations()) {
      if (loc.hasCheeseWheel()) {
        actualCount++;
      }
    }
    Assertions.assertEquals(
        expectedCount.intValue(),
        actualCount,
        "Expected "
            + expectedCount
            + " cheese wheels on shelf "
            + shelfID
            + " but found "
            + actualCount);
  }

  /**
   * @author Maxime Robatche-Claive
   */
  @Then("the cheese wheel {int} shall not be part of any order \\(p6)")
  public void the_cheese_wheel_shall_not_be_part_of_any_order_p6(Integer cheeseWheelID) {
    // Find cheese wheel by ID, not by index
    CheeseWheel cw = null;
    for (CheeseWheel wheel : ecseManager.getCheeseWheels()) {
      if (wheel.getId() == cheeseWheelID) {
        cw = wheel;
        break;
      }
    }
    Assertions.assertNotNull(
        cw, "Cheese wheel with id " + cheeseWheelID + " should exist for this assertion");

    // scan all orders and ensure none contains this wheel
    for (Transaction t : ecseManager.getTransactions()) {
      if (t instanceof Order) {
        Order o = (Order) t;
        boolean contains = o.getCheeseWheels().contains(cw);
        Assertions.assertFalse(
            contains, "Cheese wheel " + cheeseWheelID + " is still part of order " + o.getId());
      }
    }
  }

  /**
   * @author Yohan Le Morhedec
   */
  @Then("the order {int} shall have {int} cheese wheels \\(p6)")
  public void the_order_shall_have_cheese_wheels_p6(Integer orderID, Integer expectedCount) {
    Order orderInline =
        ecseManager.getTransactions().stream()
            .filter(t -> t instanceof Order && t.getId() == orderID)
            .map(t -> (Order) t)
            .findFirst()
            .orElse(null);
    Assertions.assertNotNull(orderInline, "Order with id " + orderID + " should exist");
    Assertions.assertEquals(
        expectedCount.intValue(),
        orderInline.getCheeseWheels().size(),
        "Unexpected number of cheese wheels in the order.");
  }

  /**
   * @author Michael Ha
   */
  @Then("the error {string} shall be raised \\(p6)")
  public void the_error_shall_be_raised_p6(String expectedError) {
    Assertions.assertNotNull(capturedError, "Expected an error to be raised.");
    Assertions.assertEquals(
        expectedError, capturedError, "Captured error does not match expected error.");
  }

  /**
   * @author Louis Salanon
   */
  @Then("the cheese wheel {int} shall not exist in the system \\(p6)")
  public void the_cheese_wheel_shall_not_exist_in_the_system_p6(Integer cheeseWheelID) {

    boolean exists = false;

    for (CheeseWheel wheel : ecseManager.getCheeseWheels()) {
      if (wheel.getId() == cheeseWheelID) {
        exists = true;
        break;
      }
    }
    Assertions.assertFalse(
        exists, "Cheese wheel with id " + cheeseWheelID + " still exists in the system.");
  }
}
