package ca.mcgill.ecse.cheecsemanager.features;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Order;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.sql.Date;


public class DisplayCheeseWheelStepDefinitions {

  private CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
  private String error = "";
  private Map<String, int[]> shelfDims;
  private Map<String, Farmer> farmers;
  private List<Order> orders;
  private List<TOCheeseWheel> presentedCheeseWheels;
  
  private Purchase currentPurchase;
  private int currentPurchaseCheeseWheelCount;
  private MaturationPeriod currentPurchaseMaturationPeriod;

  /**
   * Creates a farmer in the system using the provided data table. This step sets up test data
   * by creating farmer objects with their details and storing them for later use.
   * @param dataTable Contains farmer details including email, name, password, and address
   * @author Julien Yang
   */
  @Given("the following farmer exists in the system \\(p10)")
  public void the_following_farmer_exists_in_the_system_p10(
      io.cucumber.datatable.DataTable dataTable) {
    
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> row : rows) {
      String email = row.get("email");
      String name = row.get("name");
      String password = row.get("password");
      String address = row.get("address");
      Farmer farmer = new Farmer(email, password, address, manager);

      if (name != null && !name.isEmpty()) {
        farmer.setName(name);
      }
    }
  }

  /**
   * Creates a shelf in the system with the specified dimensions. This step creates shelf objects
   * and stores their dimension information for later location creation.
   * @param dataTable Contains shelf details including id, nrColumns, and nrRows
   * @author Julien Yang, Charles Benoit
   */
  @Given("the following shelf exists in the system \\(p10)")
  public void the_following_shelf_exists_in_the_system_p10(
      io.cucumber.datatable.DataTable dataTable) {
    manager = CheECSEManagerApplication.getCheecseManager();
    shelfDims = new HashMap<String, int[]>();
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> row : rows) {
      String id = row.get("id");
      manager.addShelve(id);

      // Create the shelf locations here
      Integer numberOfColumns = Integer.parseInt(row.get("nrColumns"));
      Integer numberOfRows = Integer.parseInt(row.get("nrRows"));

      // Add shelf dimensions to global
      shelfDims.put(id, new int[] {numberOfColumns, numberOfRows});
    }
  }

  /**
   * Creates all storage locations for a specified shelf. This step populates a shelf with
   * all possible location combinations based on its dimensions.
   * @param shelfId The shelf ID for which to create locations
   * @author Julien Yang, Charles Benoit
   */
  @Given("all locations are created for shelf {string} \\(p10)")
  public void all_locations_are_created_for_shelf_p10(String shelfId) {
    Shelf shelf = Shelf.getWithId(shelfId);
    int nrColumns = shelfDims.get(shelfId)[0];
    int nrRows = shelfDims.get(shelfId)[1];
    for (int i = 1; i <= nrColumns; i++) {
      for (int j = 1; j <= nrRows; j++) {
          shelf.addLocation(i, j);
      }
    }
  }

  /**
   * Assigns a cheese wheel to a specific location on a shelf. This step places a cheese wheel
   * at the specified coordinates on the given shelf.
   * @param cheeseWheelId The cheese wheel ID to locate
   * @param shelfId The shelf ID where to place the cheese wheel
   * @param column The column position on the shelf
   * @param row The row position on the shelf
   * @author Yueran Lu, Charles Benoit
   */
  @Given("the cheese wheel with id {int} is located at shelf {string} at column {int} and row {int} \\(p10)")
  public void the_cheese_wheel_with_id_is_located_at_shelf_at_column_and_row_p10(Integer cheeseWheelId,
      String shelfId, Integer column, Integer row) {
    // Author: Yueran Lu, Charles Benoit
    CheECSEManager cheECSEManager = CheECSEManagerApplication.getCheecseManager();
    
    // Find cheese wheel by ID, not by index
    CheeseWheel cheeseWheel = null;
    for (CheeseWheel cw : cheECSEManager.getCheeseWheels()) {
      if (cw.getId() == cheeseWheelId) {
        cheeseWheel = cw;
        break;
      }
    }
    
    Shelf shelf = Shelf.getWithId(shelfId);

    if (cheeseWheel != null) {
      for (var location : shelf.getLocations()) {
        if (location.getColumn() == column && location.getRow() == row) {
          cheeseWheel.setLocation(location);
          break;
        }
      }
    }
  }

  /**
   * Creates an order in the system with the specified details. This step sets up order data
   * including transaction dates, cheese wheel quantities, and wholesale company information.
   * @param dataTable Contains order details including transactionDate, nrCheeseWheels, monthsAged, deliveryDate, and companyName
   * @author Yueran Lu
   */
  @Given("the following order exists in the system \\(p10)")
  public void the_following_order_exists_in_the_system_p10(
      io.cucumber.datatable.DataTable dataTable) {

    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> row : rows) {
      LocalDate localDate = LocalDate.parse(row.get("transactionDate"));
      Date transactionDate = Date.valueOf(localDate);
      localDate = LocalDate.parse(row.get("deliveryDate"));
      Date deliveryDate = Date.valueOf(localDate);
      Integer nrCheeseWheels = Integer.parseInt(row.get("nrCheeseWheels"));
      String companyName = row.get("companyName");
      WholesaleCompany curCompany = WholesaleCompany.hasWithName(companyName) ? WholesaleCompany.getWithName(companyName) : new WholesaleCompany(companyName, "", manager);

      MaturationPeriod maturationPeriod;
      String monthsAged = row.get("monthsAged");

      if (monthsAged.equals("Six")) {
        maturationPeriod = MaturationPeriod.Six;
      } else if (monthsAged.equals("Twelve")) {
        maturationPeriod = MaturationPeriod.Twelve;
      } else if (monthsAged.equals("TwentyFour")) {
        maturationPeriod = MaturationPeriod.TwentyFour;
      } else {
        maturationPeriod = MaturationPeriod.ThirtySix;
      }

      Order newOrder = new Order(transactionDate, manager, nrCheeseWheels, maturationPeriod,
          deliveryDate, curCompany);
    }
  }

  /**
   * Assigns all non-spoiled cheese wheels from a purchase to an order. This step links
   * cheese wheels that are still good to a specific order for sale.
   * @param int1 The purchase ID from which to get cheese wheels
   * @param int2 The order ID to which cheese wheels should be assigned
   * @author Thomas Lewis
   */
  @Given("all non-spoiled cheese wheels from purchase {int} are part of order {int} \\(p10)")
  public void all_non_spoiled_cheese_wheels_from_purchase_are_part_of_order_p10(Integer purchaseId,
      Integer orderId) {
    CheECSEManager cheECSEManager = CheECSEManagerApplication.getCheecseManager();
    List<CheeseWheel> cheeseWheels = cheECSEManager.getCheeseWheels();

    // get orders
    Order order = (Order) cheECSEManager.getTransaction(orderId.intValue()-1);

    // get cheese from purchase that aren't spoiled and add to order
    for (CheeseWheel cw : cheeseWheels) {
      if (cw.getPurchase().getId() == purchaseId.intValue() && !cw.getIsSpoiled()) {
        order.addCheeseWheel(cw);
      }
    }
    // Write code here that turns the phrase above into concrete actions
  }

  /**
   * Creates a purchase in the system and stores information for later cheese wheel creation.
   * This step sets up purchase data and stores the necessary details to create cheese wheels later.
   * @param dataTable Contains purchase details including purchaseDate, nrCheeseWheels, monthsAged, and farmerEmail
   * @author Yuehan Li, Charles Benoit
   */
  @Given("the following purchase exists in the system \\(p10)")
  public void the_following_purchase_exists_in_the_system_p10(
      io.cucumber.datatable.DataTable dataTable) {
    
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> row : rows) {
      LocalDate localDate = LocalDate.parse(row.get("purchaseDate"));
      Date purchaseDate = Date.valueOf(localDate);
      String farmerEmail = row.get("farmerEmail");
      Farmer farmer = (Farmer) Farmer.getWithEmail(farmerEmail);
      Purchase purchase = new Purchase(purchaseDate, manager, farmer);

      // Store information for creating cheese wheels later
      int nrCheeseWheels = Integer.parseInt(row.get("nrCheeseWheels"));
      MaturationPeriod maturationPeriod;
      String monthsAged = row.get("monthsAged");

      if (monthsAged.equals("Six")) {
        maturationPeriod = MaturationPeriod.Six;
      } else if (monthsAged.equals("Twelve")) {
        maturationPeriod = MaturationPeriod.Twelve;
      } else if (monthsAged.equals("TwentyFour")) {
        maturationPeriod = MaturationPeriod.TwentyFour;
      } else {
        maturationPeriod = MaturationPeriod.ThirtySix;
      }

      currentPurchase = purchase;
      currentPurchaseCheeseWheelCount = nrCheeseWheels;
      currentPurchaseMaturationPeriod = maturationPeriod;
    }
  }

  /**
   * Creates all cheese wheels for a specified purchase using previously stored information.
   * This step actually instantiates the cheese wheel objects for the purchase.
   * @param purchaseId The purchase ID for which to create cheese wheels
   * @author Dongshen Guan
   */
  @Given("all cheese wheels from purchase {int} are created \\(p10)")
  public void all_cheese_wheels_from_purchase_are_created_p10(Integer purchaseId) {
    if (currentPurchase != null) {
      for (int i = 0; i < currentPurchaseCheeseWheelCount; i++) {
        CheeseWheel cheeseWheel = new CheeseWheel(currentPurchaseMaturationPeriod, false, currentPurchase, manager);
        currentPurchase.addCheeseWheel(cheeseWheel);
      }
    }
  }

  /**
   * Marks a specific cheese wheel as spoiled in the system. This step modifies the spoiled
   * status of a cheese wheel to simulate deterioration or damage.
   * @param cheeseWheelId The ID of the cheese wheel to mark as spoiled
   * @author Dongshen Guan
   */
  @Given("cheese wheel {int} is spoiled \\(p10)")
  public void cheese_wheel_is_spoiled_p10(Integer cheeseWheelId) {
    for (CheeseWheel cheeseWheel : manager.getCheeseWheels()) {
      if (cheeseWheel.getId() == cheeseWheelId) {
        cheeseWheel.setIsSpoiled(true);
        break;
      }
    }
  }


  /**
   * Manager attempts to display a specific cheese wheel using the controller. This step tests
   * the controller functionality and captures the displayed cheese wheel information.
   * @param cheeseWheelId The ID of the cheese wheel to display
   * @author Dory nrCheeseWheels
   */
  @When("the facility manager attempts to display cheese wheel {int} \\(p10)")
  public void the_facility_manager_attempts_to_display_cheese_wheel_p10(Integer cheeseWheelId) {
    presentedCheeseWheels = new ArrayList<TOCheeseWheel>();
    TOCheeseWheel cheeseWheel = CheECSEManagerFeatureSet3Controller.getCheeseWheel(cheeseWheelId);
    if (cheeseWheel != null) {
      presentedCheeseWheels.add(cheeseWheel);
    }
  }

  /**
   * Manager attempts to display all cheese wheels in the system using the controller. This step
   * tests the controller functionality and captures all displayed cheese wheel information.
   * @author Yuehan Li
   */
  @When("the facility manager attempts to display all cheese wheels \\(p10)")
  public void the_facility_manager_attempts_to_display_all_cheese_wheels_p10() {
    presentedCheeseWheels = CheECSEManagerFeatureSet3Controller.getCheeseWheels();
  }

  /**
   * Verifies that the system contains the expected number of cheese wheels. This step validates
   * the total count of cheese wheels in the system against the expected value.
   * @param nrCheeseWheels The expected number of cheese wheels in the system
   * @author Yuehan Li
   */
  @Then("the number of cheese wheels in the system shall be {int} \\(p10)")
  public void the_number_of_cheese_wheels_in_the_system_shall_be_p10(Integer nrCheeseWheels) {
    assertEquals(nrCheeseWheels, manager.getCheeseWheels().size());
  }

  /**
   * Manager verifies that the presented cheese wheels match the expected data using the controller.
   * This step validates the display functionality by comparing actual displayed data with expected
   * cheese wheel attributes including id, monthsAged, isSpoiled, purchaseDate, shelfId, column, row, and isOrdered.
   * @param dataTable The Cucumber DataTable containing expected cheese wheel information, where
   *        each row is represented as a Map of attribute names to values
   * @author Dory Song
   */
  @Then("the following cheese wheels shall be presented \\(p10)")
  public void the_following_cheese_wheels_shall_be_presented_p10(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> expectedCheeseWheels = dataTable.asMaps(String.class, String.class);
    assertEquals(expectedCheeseWheels.size(), presentedCheeseWheels.size());
    for (TOCheeseWheel toCheeseWheel : presentedCheeseWheels) {
      boolean foundId = false;
      for (var expectedCheeseWheel : expectedCheeseWheels) {
        if (Integer.parseInt(expectedCheeseWheel.get("id")) != toCheeseWheel.getId()) {
          continue;
        }
    
        foundId = true;

        Date expectedPurchaseDate = Date.valueOf(expectedCheeseWheel.get("purchaseDate"));
        assertEquals(expectedPurchaseDate, toCheeseWheel.getPurchaseDate());
        assertEquals(expectedCheeseWheel.get("monthsAged"), toCheeseWheel.getMonthsAged());
        assertEquals(expectedCheeseWheel.get("isSpoiled"), toCheeseWheel.getIsSpoiled() ? "true" : "false");
        assertEquals(expectedCheeseWheel.get("shelfId"), toCheeseWheel.getShelfID());
        assertEquals(expectedCheeseWheel.get("column"), Integer.toString(toCheeseWheel.getColumn()));
        assertEquals(expectedCheeseWheel.get("row"), Integer.toString(toCheeseWheel.getRow()));
        assertEquals(expectedCheeseWheel.get("isOrdered"), toCheeseWheel.getIsOrdered() ? "true" : "false");
      }

      assertTrue(foundId, "Did not find the TOCheeseWheel object's Id in expected cheese wheels");
    }
  }

  /**
   * Verifies that no cheese wheels are presented in the display results. This step validates
   * scenarios where the display operation should return empty results.
   * @author Julien Yang, Dongshen Guan
   */
  @Then("no cheese wheels shall be presented \\(p10)")
  public void no_cheese_wheels_shall_be_presented_p10() {
    assertTrue(presentedCheeseWheels.isEmpty(),
        "Expected no cheese wheels to be presented, but some were found");
  }
}
    
