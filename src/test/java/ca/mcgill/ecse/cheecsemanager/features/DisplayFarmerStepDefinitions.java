package ca.mcgill.ecse.cheecsemanager.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.sql.Date;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.User;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;

/**
 * Step definitions for Display Farmer feature (p5)
 * <p>
 * Defines Cucumber acceptance tests to verify the display farmer features, including success and
 * error conditions
 * </p>
 * 
 * @author Dominic Cloutier, Rahma Ammari, Kaiyan Chu, Mohamed Amine Gabsi, Sarah Bouziad, Bill
 *         Huynh-Lu, Aya Merdjaoui
 */

public class DisplayFarmerStepDefinitions {

  private List<TOFarmer> displayedFarmers = new ArrayList<>();
  private List<Map<String, String>> purchaseListData = new ArrayList<>();
  private List<Purchase> purchaseList = new ArrayList<>();
  private CheECSEManager cheecsemanager = CheECSEManagerApplication.getCheecseManager();

  /**
   * Initial adding of the list of farmers with their attributes from the dataTable into a
   * CheECSEManager
   * 
   * @param dataTable Cucumber data table that includes farmer's information: email, password,
   *        address, name
   * @author Kaiyan Chu
   */
  @Given("the following farmer exists in the system \\(p5)")
  public void the_following_farmer_exists_in_the_system_p5(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    for (Map<String, String> row : rows) {
      String email = row.get("email");
      String password = row.get("password");
      String address = row.get("address");
      String name = row.get("name");
      Farmer farmer = new Farmer(email, password, address, cheecsemanager);
      if (name != null && !name.trim().isEmpty()) {
        farmer.setName(name);
      }
    }
  }

  /**
   * Setup the purchases made in the system with their attributes in the dataTable
   * 
   * @param dataTable Cucumber dataTable containing Purchases: purchaseDate, nrCheeseWheels,
   *        monthsAged, farmerEmail
   * @author Mohamed Amine Gabsi
   */
  @Given("the following purchase exists in the system \\(p5)")
  public void the_following_purchase_exists_in_the_system_p5(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps();

    for (var row : rows) {
      purchaseListData.add(row);
      // Order Date
      Date date = Date.valueOf(row.get("purchaseDate"));
      // Farmer Email
      String farmerEmail = row.get("farmerEmail");
      // Get Farmer Instance with email
      Farmer farmer = (Farmer) User.getWithEmail(farmerEmail);
      // Create the purchase
      Purchase purchasing = new Purchase(date, cheecsemanager, farmer);
      purchaseList.add(purchasing);
    }
  }

  /**
   * Setup the cheeseWheel for each purchase with their attributes in the previous dataTable
   * 
   * @param integer
   * @author Mohamed Amine Gabsi, Aya Merdjaoui
   */
  @Given("all cheese wheels from purchase {int} are created \\(p5)")
  public void all_cheese_wheels_from_purchase_are_created_p5(Integer purchaseNumber) {

    Map<String, String> purchaseListRow = purchaseListData.get(purchaseNumber - 1);
    // Age in String
    String monthAge = purchaseListRow.get("monthsAged");
    MaturationPeriod age = null;
    // Switch case to convert the string of ageMonth into the MaturationPeriod Enum
    switch (monthAge) {
      case "Six":
        age = MaturationPeriod.Six;
        break;
      case "Twelve":
        age = MaturationPeriod.Twelve;
        break;
      case "TwentyFour":
        age = MaturationPeriod.TwentyFour;
        break;
      case "ThirtySix":
        age = MaturationPeriod.ThirtySix;
        break;
      default:
        break;
    }
    // Add the cheeses to the purchase
    for (int i = 0; i < Integer.parseInt(purchaseListRow.get("nrCheeseWheels")); i++) {
      new CheeseWheel(age, false, purchaseList.get(purchaseNumber - 1), cheecsemanager);
    }
  }

  /**
   * Assign the cheeseWheel with a certain ID to Spoiled True
   * 
   * @param int1
   * @author Dominic Cloutier
   */
  @Given("cheese wheel {int} is spoiled \\(p5)")
  public void cheese_wheel_is_spoiled_p5(Integer cheeseId) {
    // Write code here that turns the phrase above into concrete actions
    CheeseWheel cheeseWheelFound = null;
    for (CheeseWheel cheeseWheel : cheecsemanager.getCheeseWheels()) {
      if (cheeseWheel.getId() == cheeseId) {
        cheeseWheelFound = cheeseWheel;
        break;
      }
    }
    if (cheeseWheelFound != null) {
      cheeseWheelFound.setIsSpoiled(true);
    }
  }

  /**
   * @author: Kaiyan Chu
   */
  @When("the facility manager attempts to display from the system all the farmers \\(p5)")
  public void the_facility_manager_attempts_to_display_from_the_system_all_the_farmers_p5() {
    displayedFarmers = CheECSEManagerFeatureSet7Controller.getFarmers();
  }

  /**
   * @author: Kaiyan Chu
   */
  @When("the facility manager attempts to display from the system the farmer with email {string} \\(p5)")
  public void the_facility_manager_attempts_to_display_from_the_system_the_farmer_with_email_p5(
      String email) {
    displayedFarmers.add(CheECSEManagerFeatureSet7Controller.getFarmer(email));

  }

  /**
   * @author: Kaiyan Chu
   */
  @Then("the number of farmers in the system shall be {int} \\(p5)")
  public void the_number_of_farmers_in_the_system_shall_be_p5(Integer farmersNumber) {
    assertEquals(farmersNumber, cheecsemanager.getFarmers().size());
  }

  /**
   * Checking if the list of farmer is the compatible with the one created in the setup
   * 
   * @param dataTable Cucumber data table that includes farmer's information: email, password,
   *        address, name
   * 
   * @author Sarah Bouziad, Rahma Ammari, Mohamed Amine Gabsi
   */
  @Then("the following farmer details shall be presented \\(p5)")
  public void the_following_farmer_details_shall_be_presented_p5(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps();
    Integer inputTestSizeCounter = rows.size();
    Integer farmerCount = 0;
    for (Map<String, String> row : rows) {
      String email = row.get("email");
      String password = row.get("password");
      String address = row.get("address");
      String name = row.get("name");
      for (TOFarmer farmer : displayedFarmers) {
        if (farmer.getEmail().equals(email)) {
          assertEquals(email, farmer.getEmail());
          assertEquals(password, farmer.getPassword());
          assertEquals(address, farmer.getAddress());
          assertEquals(name, farmer.getName());
          farmerCount += 1;
        }
      }
    }
    assertEquals(inputTestSizeCounter, farmerCount);
  }


  /**
   * @author Mohamed Amine Gabsi
   */
  @Then("the following cheese wheels shall be presented for farmer {string} \\(p5)")
  public void the_following_cheese_wheels_shall_be_presented_for_farmer_p5(String email,
      io.cucumber.datatable.DataTable dataTable) {

    TOFarmer farmer = null;
    for (TOFarmer toFarmer : displayedFarmers) {
      if (toFarmer.getEmail().equals(email)) {
        farmer = toFarmer;
      }
    }
    assertNotNull(farmer, "The Farmer should not be null");
    List<Map<String, String>> rows = dataTable.asMaps();
    Integer[] cheesesIds = farmer.getCheeseWheelIDs();
    String[] cheesesSpoiling = farmer.getIsSpoileds();
    Date[] cheesesDates = farmer.getPurchaseDates();
    String[] cheesesAge = farmer.getMonthsAgeds();

    for (Map<String, String> row : rows) {

      // Get the ID from the Cheese Row input
      Integer id = Integer.parseInt(row.get("id"));

      // Get spoiling Status of the cheese
      Boolean isSpoiled = Boolean.parseBoolean(row.get("isSpoiled"));

      // Get the purchase date
      Date purchaseDate = Date.valueOf(row.get("purchaseDate"));

      // Get the age of the cheese and transform the string input into the right type (enum)
      String monthAge = row.get("monthsAged");


      // Go Over the cheese in the farmer
      for (int i = 0; i < farmer.numberOfCheeseWheelIDs(); i++) {
        if (id == cheesesIds[i]) {
          assertEquals(id, cheesesIds[i]);
          assertEquals(isSpoiled, Boolean.parseBoolean(cheesesSpoiling[i]));
          assertEquals(purchaseDate, cheesesDates[i]);
          assertEquals(cheesesAge[i], monthAge);
        }
      }
    }
  }

  /**
   * 
   * @author Bill Huynh-Lu
   */
  @Then("no cheese wheels shall be presented for farmer {string} \\(p5)")
  public void no_cheese_wheels_shall_be_presented_for_farmer_p5(String email) {
    TOFarmer displayedFarmer = null;
    for (TOFarmer toFarmer : displayedFarmers) {
      if (toFarmer.getEmail().equals(email)) {
        displayedFarmer = toFarmer;
      }
    }
    if (displayedFarmer == null) {
      // No farmer displayed -> treat as zero cheese wheels
      return;
    }
    // Assert that the farmer has zero cheese wheels
    assertEquals(0, displayedFarmer.numberOfCheeseWheelIDs());
  }

  /**
   * 
   * @author Mohamed Amine Gabsi
   */
  @Then("no farmers shall be presented \\(p5)")
  public void no_farmers_shall_be_presented_p5() {
    assertNull(displayedFarmers.get(0),
        "The result is supposed to be null because the farmer doesn't exist");
  }
}


