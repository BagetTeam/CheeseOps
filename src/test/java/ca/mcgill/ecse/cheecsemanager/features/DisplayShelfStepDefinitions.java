package ca.mcgill.ecse.cheecsemanager.features;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;

public class DisplayShelfStepDefinitions {
  private CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
  private Map<String, int[]> shelfDimensions;
  private List<Purchase> purchases;
  private List<Map<String, String>> purchaseDataAsList;
  private List<TOShelf> shelves;

  private static MaturationPeriod parseMaturation(String s) {
    switch (s) {
      case "Six":
        return MaturationPeriod.Six;
      case "Twelve":
        return MaturationPeriod.Twelve;
      case "TwentyFour":
        return MaturationPeriod.TwentyFour;
      case "ThirtySix":
        return MaturationPeriod.ThirtySix;
    }
    throw new IllegalArgumentException(
        "This function has a string paramater written exactly as on of the four possibilities in the MatruationPeriod enum");
  }

  /**
   * This step definition ensures that the shelf from the datatable exists in the system p1.
   * 
   * @param dataTable The data for the shelf
   *
   * @author Bassam Baki
   */
  @Given("the following shelf exists in the system \\(p1)")
  public void the_following_shelf_exists_in_the_system_p1(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> dataAsList = dataTable.asMaps();
    shelfDimensions = new HashMap<>(); // Reset hash map

    for (int i = 0; i < dataAsList.size(); i++) {
      var shelfData = dataAsList.get(i);

      manager.addShelve(shelfData.get("id"));
      int nrColumns = Integer.parseInt(shelfData.get("nrColumns"));
      int nrRows = Integer.parseInt(shelfData.get("nrRows"));
      shelfDimensions.put(shelfData.get("id"), new int[] {nrColumns, nrRows});
    }
  }

  /**
   * This step definition the shelf has the wanted locations given nbrRows and nbrColumns.
   * 
   * @param string The id for the shelf
   *
   * @author Bassam Baki
   * @author William Zhang
   */
  @Given("all locations are created for shelf {string} \\(p1)")
  public void all_locations_are_created_for_shelf_p1(String id) {
    Shelf shelf = Shelf.getWithId(id);

    int nrColumns = shelfDimensions.get(id)[0];
    int nrRows = shelfDimensions.get(id)[1];

    for (int i = 1; i <= nrColumns; i++) {
      for (int j = 1; j <= nrRows; j++) {
        shelf.addLocation(i, j);
      }
    }
  }

  /**
   * This step definition ensures that a given farmer exists in the system.
   * 
   * @param dataTable The email, password, address, and name of a specified farmer in the system
   * 
   * @author Bassam Baki
   */
  @Given("the following farmer exists in the system \\(p1)")
  public void the_following_farmer_exists_in_the_system_p1(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> dataAsList = dataTable.asMaps();

    for (var farmerData: dataAsList) {
      Farmer farmer = new Farmer(farmerData.get("email"), farmerData.get("password"),
          farmerData.get("address"), manager);
      farmer.setName(farmerData.get("name"));
    }
  }

  /**
   * This step definition ensures that a given purchase exists in the system.
   * 
   * @param dataTable The date, number of cheese wheels, the months aged of the cheese wheels, and
   *        farmer email of a specified purchase in the system
   * 
   * @author Bassam Baki
   * @author William Zhang
   */
  @Given("the following purchase exists in the system \\(p1)")
  public void the_following_purchase_exists_in_the_system_p1(
      io.cucumber.datatable.DataTable dataTable) {
    purchaseDataAsList = dataTable.asMaps();
    purchases = new ArrayList<>(); // Reset list

    for (var purchaseData: purchaseDataAsList) {
      Date date = Date.valueOf(purchaseData.get("purchaseDate"));
      Farmer farmer = (Farmer) Farmer.getWithEmail(purchaseData.get("farmerEmail"));
      Purchase purchase = new Purchase(date, manager, farmer);
      purchases.add(purchase);
    }
  }

  /**
   * This step definition ensures that each purchase's cheese wheels have been made.
   * 
   * @param int1 The number for the order
   *
   * @author Bassam Baki
   * @author William Zhang
   */
  @Given("all cheese wheels from purchase {int} are created \\(p1)")
  public void all_cheese_wheels_from_purchase_are_created_p1(Integer purchaseNumber) {
    for (var purchaseData: purchaseDataAsList) {
      for (int j = 0; j < Integer.parseInt(purchaseData.get("nrCheeseWheels")); j++) {
        MaturationPeriod maturationPeriod = parseMaturation(purchaseData.get("monthsAged"));
        new CheeseWheel(maturationPeriod, false, purchases.get(purchaseNumber - 1), manager);
      }
    }
  }

  /**
   * This step definition ensures that certain ID cheese wheels are located on a chosen shelf based
   * on a given dataTable.
   * 
   * @param string The shelf ID
   * @param dataTable The position, in column and row, of a specified cheese ID on the shelf
   * 
   * @author Bassam Baki
   * @author William Zhang
   */
  @Given("the following cheese wheel exists on shelf {string} \\(p1)")
  public void the_following_cheese_wheel_exists_on_shelf_p1(String id,
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> dataAsList = dataTable.asMaps();

    Shelf shelf = Shelf.getWithId(id);
    List<ShelfLocation> shelfLocations = shelf.getLocations();

    for (int i = 0; i < dataAsList.size(); i++) {
      var cheesePlacement = dataAsList.get(i);

      for (int j = 0; j < shelfLocations.size(); j++) {
        ShelfLocation shelfLocation = shelfLocations.get(j);

        int cheeseColumn = Integer.parseInt(cheesePlacement.get("column"));
        int cheeseRow = Integer.parseInt(cheesePlacement.get("row"));
        CheeseWheel currentCheesewheel =
            manager.getCheeseWheel(Integer.parseInt(cheesePlacement.get("id")) - 1);

        if (shelfLocation.getColumn() == cheeseColumn && shelfLocation.getRow() == cheeseRow) {
          shelfLocation.setCheeseWheel(currentCheesewheel);
        }
      }
    }
  }

  /**
   * This step definition calls the controller to find a shelf given an id.
   * 
   * @param id The id of the shelf
   *
   * @author William Zhang
   */
  @When("the facility manager attempts to display from the system the shelf with id {string} \\(p1)")
  public void the_facility_manager_attempts_to_display_from_the_system_the_shelf_with_id_p1(
      String ShelfID) {
    // Use list even for single shelf to conform with cucumber feature
    shelves = new ArrayList<>();
    shelves.add(CheECSEManagerFeatureSet1Controller.getShelf(ShelfID));
  }

  /**
   * This step definition calls the controller and gets a list of all the shelves in the system.
   * 
   * @author Dragos Bajanica
   */
  @When("the facility manager attempts to display from the system all the shelves \\(p1)")
  public void the_facility_manager_attempts_to_display_from_the_system_all_the_shelves_p1() {
    shelves = CheECSEManagerFeatureSet1Controller.getShelves();
  }

  /**
   * This step definition verifies that the two shelves created in the Givens are in the system
   * 
   * @param int1 number of shelves in the system
   *
   * @author William Zhang
   */
  @Then("the number of shelves in the system shall be {int} \\(p1)")
  public void the_number_of_shelves_in_the_system_shall_be_p1(Integer numOfShelves) {
    assertEquals(numOfShelves.intValue(), manager.getShelves().size());
  }

  /**
   * This step definition verifies that the shelf details match the expected data table provided.
   * 
   * @param dataTable The expected columns: shelf ID, number of columns, number of rows
   * 
   * @author Omar Jaber
   */
  @Then("the following shelf details shall be presented \\(p1)")
  public void the_following_shelf_details_shall_be_presented_p1(
      io.cucumber.datatable.DataTable dataTable) {
    // Get data table
    List<Map<String, String>> rows = dataTable.asMaps();

    // Check for correct size match
    assertEquals(rows.size(), shelves.size());

    // Check table matches shelves
    for (int i = 0; i < rows.size(); i++) {
      var row = rows.get(i);
      var shelf = shelves.get(i);

      // Parse data table cells
      String tableID = row.get("id");
      int tableNrColumns = Integer.parseInt(row.get("nrColumns"));
      int tableNrRows = Integer.parseInt(row.get("nrRows"));

      // Check that the Shelf details from TO object match the data table
      assertEquals(tableID, shelf.getShelfID());
      assertEquals(tableNrColumns, shelf.getMaxColumns());
      assertEquals(tableNrRows, shelf.getMaxRows());
    }
  }

  /**
   * This step definition ensures that the all the positions of each cheese wheel in the shelf is as
   * expected.
   * 
   * @param string The ID of the wanted shelf
   * @param dataTable The expected data in the shelf
   * 
   * @author Dragos Bajanica
   */
  @Then("the following cheese wheels shall be presented for shelf {string} \\(p1)")
  public void the_following_cheese_wheels_shall_be_presented_for_shelf_p1(String shelfID,
      io.cucumber.datatable.DataTable dataTable) {
    // Get data table
    List<Map<String, String>> rows = dataTable.asMaps();

    // Find wanted shelf
    TOShelf shelf = null;
    for (TOShelf currentShelf : shelves) {
      assertNotNull(currentShelf, "Expected created shelf in the system, got null instead.");

      // Check if it is the wanted shelf
      if (currentShelf.getShelfID().equals(shelfID)) {
        shelf = currentShelf;
      }
    }

    // Check if correct shelf was found
    assertNotNull(shelf, "There is no shelf found with ID " + shelfID + ".");
    assertEquals(shelfID, shelf.getShelfID(), "The wrong shelf ID was found.");

    // Get list of all cheese wheel IDs
    Integer[] ids = shelf.getCheeseWheelIDs();
    assertEquals(rows.size(), ids.length);

    // Check table matches shelf
    for (int i = 0; i < rows.size(); i++) {
      // Get current row and cheese wheel ID
      var row = rows.get(i);
      var id = ids[i];

      // Parse data table cells
      Integer tableColumn = Integer.parseInt(row.get("column"));
      Integer tableRow = Integer.parseInt(row.get("row"));
      Integer tableID = Integer.parseInt(row.get("id"));

      // Check that the cheese wheels from TO object and data table match
      assertEquals(tableColumn, shelf.getColumnNr(i));
      assertEquals(tableRow, shelf.getRowNr(i));
      assertEquals(tableID, id);
      assertEquals(row.get("monthsAged"), shelf.getMonthsAged(i));
    }
  }

  /**
   * This step definition ensures that the shelf has no cheese wheels assigned to any of its
   * locations.
   * 
   * @param shelfId The shelf identifier
   *
   * @author Joonhyun Chang
   */
  @Then("no cheese wheels shall be presented for shelf {string} \\(p1)")
  public void no_cheese_wheels_shall_be_presented_for_shelf_p1(String shelfID) {
    Shelf shelf = Shelf.getWithId(shelfID);
    assertNotNull(shelf, "The wanted shelf with ID " + shelfID + " does not exist.");

    for (ShelfLocation location : shelf.getLocations()) {
      assertNull(location.getCheeseWheel(),
          "Expected no cheese wheels assigned to shelf " + shelfID + " locations.");
    }
  }

  /**
   * This step definition ensures that the shelf gotten is null.
   * 
   * @author Yang Li
   */
  @Then("no shelves shall be presented \\(p1)")
  public void no_shelves_shall_be_presented_p1() {
    for (TOShelf shelf : shelves) {
      assertNull(shelf, "Expected no shelves displayed in the system.");
    }
  }
}
