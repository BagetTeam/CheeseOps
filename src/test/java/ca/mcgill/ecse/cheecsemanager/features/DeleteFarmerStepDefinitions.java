package ca.mcgill.ecse.cheecsemanager.features;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Map;
import java.sql.Date;
import java.util.List;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.User;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;

// new version import
import static org.junit.jupiter.api.Assertions.assertEquals;


public class DeleteFarmerStepDefinitions {
  // Private fields added
  private CheECSEManager cheeseManager;
  private String error;
  private Integer nrCheeseWheels;
  private String monthsAged;

  // Root class instance helper method
  private void theSystemExists() {
    cheeseManager = CheECSEManagerApplication.getCheecseManager();
    error = "";
  }

  // Finding Farmer instance private method
  private Farmer findFarmerByEmail(String email) {
    User u = User.getWithEmail(email);
    if (u instanceof Farmer) {
      Farmer targetFarmer = (Farmer) u;
      return targetFarmer;
    }
    return null; // no match or not a Farmer
  }

  /**
   * This method creates Farmer instances using given dataTable.
   * 
   * @author Lazarus Sarghi
   * @author George Zhou
   * @param dataTable
   */
  @Given("the following farmer exists in the system \\(p11)")
  public void the_following_farmer_exists_in_the_system_p11(
      io.cucumber.datatable.DataTable dataTable) {
    theSystemExists();

    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String aEmail = row.get("email");
      String aPassword = row.get("password");
      String aAddress = row.get("address");
      String aName = row.get("name");
      Farmer newFarmer = new Farmer(aEmail, aPassword, aAddress, cheeseManager);
      newFarmer.setName(aName);
    }
  }

  /**
   * This method creates purchases given information from given dataTable.
   * 
   * @author Lazarus Sarghi
   * @author George Zhou
   * @param dataTable
   */
  @Given("the following purchase exists in the system \\(p11)")
  public void the_following_purchase_exists_in_the_system_p11(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      Date aTransactionDate = java.sql.Date.valueOf(row.get("purchaseDate"));
      nrCheeseWheels = Integer.parseInt(row.get("nrCheeseWheels"));
      monthsAged = row.get("monthsAged");
      String farmerEmail = row.get("farmerEmail");
      Purchase aPurchase =
          new Purchase(aTransactionDate, cheeseManager, findFarmerByEmail(farmerEmail));
    }
  }

  /**
   * This method creates cheeseWheels for the given purchases.
   * 
   * @author Lazarus Sarghi
   * @author George Zhou
   * @param purchaseIndex
   */
  @Given("all cheese wheels from purchase {int} are created \\(p11)")
  public void all_cheese_wheels_from_purchase_are_created_p11(Integer purchaseIndex) {

    Purchase targetPurchase = (Purchase) cheeseManager.getTransaction(purchaseIndex - 1);

    for (int i = 0; i < nrCheeseWheels; i++) {
      targetPurchase.addCheeseWheel(MaturationPeriod.valueOf(monthsAged), false, cheeseManager);
    }
  }

  /**
   * This method attempts to delete a farmer with a given email.
   * 
   * @author Lazarus Sarghi
   * @author George Zhou
   * @param farmerEmail
   */
  @When("the facility manager attempts to delete the farmer with email {string} \\(p11)")
  public void the_facility_manager_attempts_to_delete_the_farmer_with_email_p11(
      String farmerEmail) {

    error = CheECSEManagerFeatureSet7Controller.deleteFarmer(farmerEmail);
  }

  /**
   * This method ensures the correct number of farmers will remain in the system after deletion attempt.
   * 
   * @author Lazarus Sarghi
   * @author George Zhou
   * @param expectedFarmerCount
   */
  @Then("the number of farmers in the system shall be {int} \\(p11)")
  public void the_number_of_farmers_in_the_system_shall_be_p11(Integer expectedFarmerCount) {

    assertEquals((int) expectedFarmerCount, cheeseManager.numberOfFarmers());
  }

  /**
   * This method ensures the correct farmers remain after a deletion attempt.
   * 
   * @author Lazarus Sarghi
   * @author George Zhou
   * @param dataTable
   */
  @Then("the following farmers shall exist in the system \\(p11)")
  public void the_following_farmers_shall_exist_in_the_system_p11(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String aEmail = row.get("email");
      String aPassword = row.get("password");
      String aAddress = row.get("address");
      String aName = row.get("name");
      Farmer targetFarmer = findFarmerByEmail(aEmail);
      assertEquals(aPassword, targetFarmer.getPassword());
      assertEquals(aAddress, targetFarmer.getAddress());
      assertEquals(aName, targetFarmer.getName());
    }
  }

  /**
   * This method ensures Farmer with a given farmerEmail will have the correct number of cheeses.
   * 
   * @author George Zhou
   * @author Lazarus Sarghi
   * @param farmerEmail
   * @param expectedCheeseWheelCount
   */
  @Then("the number of cheese wheels for farmer {string} shall be {int} \\(p11)")
  public void the_number_of_cheese_wheels_for_farmer_shall_be_p11(String farmerEmail,
      Integer expectedCheeseWheelCount) {

    Farmer targetFarmer = findFarmerByEmail(farmerEmail);
    Integer actualNumberOfCheese = 0;
    for (int i = 0; i < targetFarmer.getPurchases().size(); i++) {
      actualNumberOfCheese =
          actualNumberOfCheese + targetFarmer.getPurchases().get(i).numberOfCheeseWheels();
    }
    assertEquals(expectedCheeseWheelCount, actualNumberOfCheese);
  }

  /**
   * This method ensures the correct error will be raised on unsuccessful Farmer deletion.
   * 
   * @author Lazarus Sarghi
   * @author George Zhou
   * @param expectedErrorMessage
   */
  @Then("the error {string} shall be raised \\(p11)")
  public void the_error_shall_be_raised_p11(String expectedErrorMessage) {

    assertEquals(expectedErrorMessage, error);
  }
}
