package ca.mcgill.ecse.cheecsemanager.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet2Controller;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Step definitions for the Add Shelf feature (p17)
 *
 * @author Noah Vaillancourt, Aly Gaber, Hugues Salmon, Nicolas Younan, Abdelmalek Zerhouni, Charles
 *     Edward Gagnon
 */
public class AddShelfStepDefinitions {
  private String error;
  private Map<String, Map<String, Integer>> shelfDimensions = new HashMap<>();

  /**
   * This step definition ensures that the shelf from the datatable exists in the system.
   *
   * @param dataTable The data for the shelf
   * @author Charles Edward Gagnon
   */
  @Given("the following shelf exists in the system \\(p17)")
  public void the_following_shelf_exists_in_the_system_p17(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();

    for (Map<String, String> row : rows) {
      String id = row.get("id");
      int nrColumns = Integer.parseInt(row.get("nrColumns"));
      int nrRows = Integer.parseInt(row.get("nrRows"));

      // Create shelf with all locations using model layer
      Shelf shelf = new Shelf(id, manager);

      // Store dimensions to create locations later
      Map<String, Integer> dimensions = new HashMap<>();
      dimensions.put("nrColumns", nrColumns);
      dimensions.put("nrRows", nrRows);
      shelfDimensions.put(id, dimensions);
    }
  }

  /**
   * This step definition ensures that all locations are created for the specified shelf.
   *
   * @param shelfId The id of the shelf
   * @author Noah Vaillancourt
   */
  @Given("all locations are created for shelf {string} \\(p17)")
  public void all_locations_are_created_for_shelf_p17(String shelfId) {
    // Verify that the shelf exists and has locations
    Shelf shelf = Shelf.getWithId(shelfId);
    // Get the stored dimensions for this shelf
    Map<String, Integer> dimensions = shelfDimensions.get(shelfId);
    int nrColumns = dimensions.get("nrColumns");
    int nrRows = dimensions.get("nrRows");

    // Create the locations for the shelf
    for (int col = 1; col <= nrColumns; col++) {
      for (int r = 1; r <= nrRows; r++) {
        shelf.addLocation(col, r);
      }
    }
  }

  /**
   * This step definition calls the controller to add a shelf to the system.
   *
   * @param id The shelf id
   * @param nrColumns The number of columns
   * @param nrRows The number of rows
   * @author Aly Gaber
   */
  @When(
      "the facility manager attempts to add a shelf in the system with id "
          + "{string}, {int} columns, and {int} rows \\(p17)")
  public void
      the_facility_manager_attempts_to_add_a_shelf_in_the_system_with_id_columns_and_rows_p17(
          String id, Integer nrColumns, Integer nrRows) {
    error = CheECSEManagerFeatureSet2Controller.addShelf(id, nrColumns, nrRows);
  }

  /**
   * This step definition verifies the number of shelves in the system.
   *
   * @param expectedCount The expected number of shelves
   * @author Hugues Salmon
   */
  @Then("the number of shelves in the system shall be {int} \\(p17)")
  public void the_number_of_shelves_in_the_system_shall_be_p17(Integer expectedCount) {
    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
    assertEquals(expectedCount.intValue(), manager.numberOfShelves());
  }

  /**
   * This step definition verifies that a shelf with the specified configuration exists in the
   * system.
   *
   * @param id The shelf id
   * @param nrColumns The number of columns
   * @param nrRows The number of rows
   * @param locationsSize The total number of locations
   * @author Nicolas Younan
   */
  @Then(
      "the shelf {string} with {int} columns and {int} rows and a total of "
          + "{int} locations shall exist in the system \\(p17)")
  public void
      the_shelf_with_columns_and_rows_and_a_total_of_locations_shall_exist_in_the_system_p17(
          String id, Integer nrColumns, Integer nrRows, Integer locationsSize) {
    // Use Shelf's static lookup method
    Shelf shelf = Shelf.getWithId(id);
    assertNotNull(shelf, "Shelf " + id + " should exist");

    // Verify number of locations
    assertEquals(locationsSize.intValue(), shelf.numberOfLocations());

    // Verify that locations match the expected columns and rows
    Map<String, Integer> locationMap = new HashMap<>();
    for (ShelfLocation loc : shelf.getLocations()) {
      String key = loc.getRow() + "," + loc.getColumn();
      locationMap.put(key, locationMap.getOrDefault(key, 0) + 1);
    }

    // Expected number of unique locations should match
    assertEquals(nrColumns * nrRows, locationMap.size());
  }

  /**
   * This step definition verifies that each row and column combination has exactly one location.
   *
   * @param id The shelf id
   * @author Abdelmalek Zerhouni
   */
  @Then(
      "for each row and column of the shelf {string}, there is exactly one "
          + "location in the system associated with that row and column \\(p17)")
  public void
      for_each_row_and_column_of_the_shelf_there_is_exactly_one_location_in_the_system_associated_with_that_row_and_column_p17(
          String id) {
    Shelf shelf = Shelf.getWithId(id);
    assertNotNull(shelf, "Shelf " + id + " should exist");

    // Create a map to track which row/column combinations exist
    Map<String, Integer> locationMap = new HashMap<>();
    for (ShelfLocation loc : shelf.getLocations()) {
      String key = loc.getRow() + "," + loc.getColumn();
      locationMap.put(key, locationMap.getOrDefault(key, 0) + 1);
    }

    // Verify each combination appears exactly once
    for (Map.Entry<String, Integer> entry : locationMap.entrySet()) {
      assertEquals(
          1,
          entry.getValue().intValue(),
          "Location " + entry.getKey() + " should appear exactly once");
    }
  }

  /**
   * This step definition verifies that the shelves from the datatable exist in the system.
   *
   * @param dataTable The data for the shelves
   * @author Charles Edward Gagnon
   */
  @Then("the following shelves shall exist in the system \\(p17)")
  public void the_following_shelves_shall_exist_in_the_system_p17(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();

    assertEquals(rows.size(), manager.numberOfShelves());

    for (Map<String, String> row : rows) {
      String id = row.get("id");
      int nrColumns = Integer.parseInt(row.get("nrColumns"));
      int nrRows = Integer.parseInt(row.get("nrRows"));

      Shelf shelf = Shelf.getWithId(id);
      assertNotNull(shelf, "Shelf " + id + " should exist");

      // Verify the shelf has the correct number of locations
      int expectedLocations = nrColumns * nrRows;
      assertEquals(expectedLocations, shelf.numberOfLocations());
    }
  }

  /**
   * This step definition verifies that the expected error was raised.
   *
   * @param expectedError The expected error message
   * @author Noah Vaillancourt
   */
  @Then("the error {string} shall be raised \\(p17)")
  public void the_error_shall_be_raised_p17(String expectedError) {
    assertNotNull(error, "An error should have been raised");
    assertTrue(
        error.contains(expectedError) || error.equals(expectedError),
        "Expected error to contain: '" + expectedError + "' but got: '" + error + "'");
  }
}
