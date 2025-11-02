package ca.mcgill.ecse.cheecsemanager.features;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import ca.mcgill.ecse.cheecsemanager.model.ShelfLocation;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;

import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet2Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DeleteShelfStepDefinitions {


  private CheECSEManager cheeseManager = CheECSEManagerApplication.getCheecseManager();
  private List<Map<String, String>> shelfList;
  private List<Map<String, String>> purchaseListMap;
  private List<Purchase> purchaseList = new ArrayList<>();
  private String error;


  /**
   * This method creates a shelf in the system with the given ID. Team Member responsible to the
   * method: 7, Hector Giraud
   * 
   * @param dataTable a table giving the info for the shelves
   * @return void
   */

  @Given("the following shelf exists in the system \\(p12)")
  public void the_following_shelf_exists_in_the_system_p12(
      io.cucumber.datatable.DataTable dataTable) {


    shelfList = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> shelf : shelfList) {
      String id = shelf.get("id");
      Shelf newShelf = new Shelf(id, cheeseManager);

    }
  }

  /**
   * This method creates cheese wheels in the system and puts them on the correct shelves. Team
   * Member responsible to the method: 7, Hector Giraud
   * 
   * @param dataTable a table giving the info for the cheese wheels
   * @return void
   */

  @Given("the following cheese wheel exists on shelf {string} \\(p12)")
  public void the_following_cheese_wheel_exists_on_shelf_p12(String shelfID,
      io.cucumber.datatable.DataTable dataTable) {

    Shelf shelf = Shelf.getWithId(shelfID);

    List<Map<String, String>> wheels = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> wheel : wheels) {

      String idStr = wheel.get("id");
      String columnStr = wheel.get("column");
      String rowStr = wheel.get("row");
      Integer id = Integer.parseInt(idStr) - 1;
      Integer column = Integer.parseInt(columnStr);
      Integer row = Integer.parseInt(rowStr);
      CheeseWheel cheeseWheel = cheeseManager.getCheeseWheel(id);

      for (ShelfLocation location : shelf.getLocations()) {
        if (location.getColumn() == column && location.getRow() == row) {
          location.setCheeseWheel(cheeseWheel);
          break;
        }
      }
    }
  }

  /**
   * This method creates locations and assigns them to the correct shelves. Team Member responsible
   * to the method: 1, Grayden Donaldson
   * 
   * @param dataTable a table giving the info for the locations
   * @return void
   */


  @Given("all locations are created for shelf {string} \\(p12)")
  public void all_locations_are_created_for_shelf_p12(String shelfID) {
    Map<String, String> shelfData = null;

    for (Map<String, String> data : shelfList) {
      if (shelfID.equals(data.get("id"))) {
        shelfData = data;
        break;
      }
    }
    String nrColumns_str = shelfData.get("nrColumns");
    String nrRowsStr = shelfData.get("nrRows");
    int nrColumns = Integer.parseInt(nrColumns_str);
    int nrRows = Integer.parseInt(nrRowsStr);

    Shelf shelf = Shelf.getWithId(shelfID);

    // Add locations
    for (int i = 0; i < nrColumns; i++) {
      for (int j = 0; j < nrRows; j++) {
        shelf.addLocation(i, j);
      }
    }
  }

  /**
   * This method creates a farmer and assigns them to the correct shelves. Team Member responsible
   * to the method: 1, Grayden Donaldson
   * 
   * @param dataTable a table giving the info for the farmers
   * @return void
   */

  @Given("the following farmer exists in the system \\(p12)")
  public void the_following_farmer_exists_in_the_system_p12(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> farmers = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> farmer : farmers) {
      Farmer newFarmer = new Farmer(farmer.get("email"), farmer.get("password"),
          farmer.get("address"), cheeseManager);
      newFarmer.setName(farmer.get("name"));
    }
  }

  /**
   * This method creates a purchase with the correct info. Team Member responsible to the method: 4,
   * Lucas Bitbol
   * 
   * @param dataTable a table giving the info for the purchases
   * @return void
   */
  @Given("the following purchase exists in the system \\(p12)")
  public void the_following_purchase_exists_in_the_system_p12(
      io.cucumber.datatable.DataTable dataTable) {
    purchaseListMap = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> purchase : purchaseListMap) {
      purchaseList.add(new Purchase(Date.valueOf(purchase.get("purchaseDate")), cheeseManager,
          (Farmer) Farmer.getWithEmail(purchase.get("farmerEmail"))));

    }
  }

  /*
   * Method creates all cheese wheels specified in purchase {int1} Gherkin statement: all cheese
   * wheels from purchase {int} are created Team members responsible for this method: Alexandre
   * Pitié
   * 
   * @param int1: the purchase int
   * 
   * @return void
   */
  @Given("all cheese wheels from purchase {int} are created \\(p12)")

  public void all_cheese_wheels_from_purchase_are_created_p12(Integer purchaseIndex) {
    Integer internalIndex = purchaseIndex - 1;
    if (internalIndex >= 0 || internalIndex < purchaseListMap.size()) {
      Purchase purchase = purchaseList.get(internalIndex);
      MaturationPeriod maturationTime =
          MaturationPeriod.valueOf((purchaseListMap.get(internalIndex)).get("monthsAged"));
      for (int i = 0; i < Integer
          .valueOf(purchaseListMap.get(internalIndex).get("nrCheeseWheels")); i++) {
        purchase.addCheeseWheel(maturationTime, false, cheeseManager);
      }

    }
  }

  /*
   * Method attempts to delete the shelf from the system with id {string}, and stores the output in
   * error Gherkin statement: the facility manager attempts to delete from the system the shelf with
   * id {string} Team members responsible for this method: Alexandre Pitie
   * 
   * @param string: the id of the shelf to be removed
   * 
   * @return void
   */
  @When("the facility manager attempts to delete from the system the shelf with id {string} \\(p12)")
  public void the_facility_manager_attempts_to_delete_from_the_system_the_shelf_with_id_p12(
      String shelfID) {
    error = CheECSEManagerFeatureSet2Controller.deleteShelf(shelfID);
  }

  /*
   * Method confirms there are {int} number of shelves, asserts using junit Gherkin statement: the
   * number of shelves in the system shall be {int} Team members responsible for this method:
   * Alexandre Danon
   * 
   * @param expectedCount the expected number of shelves
   * 
   * @return void
   */
  @Then("the number of shelves in the system shall be {int} \\(p12)")
  public void the_number_of_shelves_in_the_system_shall_be_p12(Integer expectedCount) {
    int actualCount = cheeseManager.getShelves().size();
    assertEquals(expectedCount.intValue(), actualCount, String
        .format("Expected %d shelves in the system, but found %d.", expectedCount, actualCount));
  }

  /**
   * Description : Verifies that the shelves listed in the provided Cucumber data table exist in the
   * system. Gherkin statement: Then the following shelves shall exist in the system (p12)
   * 
   * @param dataTable
   * @return void
   * @Team Member : Joey Atie
   */
  @Then("the following shelves shall exist in the system \\(p12)")
  public void the_following_shelves_shall_exist_in_the_system_p12(
      io.cucumber.datatable.DataTable dataTable) {

    Boolean ok = false;

    List<Map<String, String>> shelfs = dataTable.asMaps(String.class, String.class);

    List<Shelf> shelves = cheeseManager.getShelves();


    for (Map<String, String> shelf : shelfs) {
      ok = false;
      String id = shelf.get("id");
      Shelf shelfy = Shelf.getWithId(id);
      for (Shelf shelve : shelves) {
        if ((shelve.getId()).equals(id)) {
          ok = true;
          break;
        }
      }

      assertTrue(ok, "Shelf with id " + id + " should exist but was not found");
    }

  }

  /**
   * Description : This Cucumber step verifies that the number of locations associated with a
   * specific shelf matches the expected count. Gherkin statement: Then the number of locations
   * associated with the shelf {string} shall be {int} (p12)
   * 
   * @param shelfId
   * @param expectedCount
   * @return void Team Member : Alexandre Danon
   */
  @Then("the number of locations associated with the shelf {string} shall be {int} \\(p12)")
  public void the_number_of_locations_associated_with_the_shelf_shall_be_p12(String shelfId,
      Integer expectedCount) {
    Shelf shelf = Shelf.getWithId(shelfId);
    int actualCount = (shelf == null) ? 0 : shelf.getLocations().size();
    assertEquals(expectedCount.intValue(), actualCount,
        String.format("Expected %d locations for shelf '%s', but found %d.", expectedCount, shelfId,
            actualCount));
  }

  /**
   * Description : This Cucumber step verifies that a specific error message is raised during the
   * execution of a test scenario. Gherkin statement: Then the error {string} shall be raised (p12)
   * 
   * @param string
   * @return void Team Member : Joey Atie
   */
  @Then("the error {string} shall be raised \\(p12)")
  public void the_error_shall_be_raised_p12(String expectedError) {
    assertEquals(expectedError, error, "The error raised was: '" + error
        + "' but the expected error was '" + expectedError + "'.");
  }

  /**
   * Description :This Cucumber step verifies that the specified cheese wheels exist in the system
   * and that their attributes (purchase, maturation period, and spoilage status) match the expected
   * values. Gherkin statement: Then the following cheese wheels shall exist in the system (p12)
   * Team Member : Rafael Laburthe-tolra
   * 
   * @param dataTable
   * @return void
   */
  @Then("the following cheese wheels shall exist in the system \\(p12)")
  public void the_following_cheese_wheels_shall_exist_in_the_system_p12(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> wheels = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> wheel : wheels) {
      int id = Integer.parseInt(wheel.get("id"));
      Purchase purchase =
          (Purchase) cheeseManager.getTransaction(Integer.parseInt(wheel.get("purchaseId")) - 1);
      MaturationPeriod mPeriod = MaturationPeriod.valueOf(wheel.get("monthsAged"));
      Boolean spoiled = Boolean.parseBoolean(wheel.get("isSpoiled"));
      CheeseWheel cw = cheeseManager.getCheeseWheel(id - 1);
      assertNotNull(cw == null, "No cheese wheel found with id " + id);
      assertEquals(purchase, cw.getPurchase());
      assertEquals(mPeriod, cw.getMonthsAged());
      assertEquals(spoiled, cw.getIsSpoiled());
    }
  }
}
