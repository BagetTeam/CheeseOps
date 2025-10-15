package ca.mcgill.ecse.cheecsemanager.features;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step definitions for the Register Farmer feature (p9)
 * <p>
 * Defines Cucumber bindings to verify farmer registration scenarios, including success and error
 * conditions
 * </p>
 *
 * @author Lina Guezi
 */

public class RegisterFarmerStepDefinitions {

  private CheECSEManager manager;
  private String error;

  /**
   * Initial setup of the system given a list of farmers with their attributes in a dataTable
   * 
   * @param dataTable Cucumber data table containing farmer info: email, password, address, and name
   * @author Lina Guezi
   */
  @Given("the following farmer exists in the system \\(p9)")
  public void the_following_farmer_exists_in_the_system_p9(
      io.cucumber.datatable.DataTable dataTable) {

    // initialization
    manager = CheECSEManagerApplication.getCheecseManager();
    error = "";

    List<Map<String, String>> rows = dataTable.asMaps();

    for (var row : rows) {
      String email = row.get("email");
      String password = row.get("password");
      String address = row.get("address");
      String name = row.get("name");

      Farmer farmer = new Farmer(email, password, address, manager);
      if (name != null && !name.trim().isEmpty()) {
        farmer.setName(name);
      }
    }
  }

  /**
   * Manager attempts to register a farmer using the controller. This step tests the controller and
   * captures any errors
   * 
   * @param email
   * @param password
   * @param address
   * @param name Can be empty or null
   * @author Lina Guezi
   */
  @When("the facility manager attempts to register a farmer in the system with email {string}, password {string}, address {string}, and name {string} \\(p9)")
  public void the_facility_manager_attempts_to_register_a_farmer_in_the_system_with_email_password_address_and_name_p9(
      String email, String password, String address, String name) {

    error = CheECSEManagerFeatureSet3Controller.registerFarmer(email, password, name, address);
  }

  /**
   * Verifies that the number of farmers currently stored in the system matches the expected count
   * 
   * @param expectedFarmerCount (expected number of farmers in the system)
   * @author Lina Guezi
   */
  @Then("the number of farmers in the system shall be {int} \\(p9)")
  public void the_number_of_farmers_in_the_system_shall_be_p9(Integer expectedFarmerCount) {
    int numberOfFarmers = manager.numberOfFarmers();
    Assertions.assertEquals(expectedFarmerCount, numberOfFarmers);
  }

  /**
   * Verifies that a farmer with the given email, password, address, and name exists in the system
   * 
   * @param email
   * @param password
   * @param address
   * @param name Can be empty or null
   * @author Lina Guezi
   */
  @Then("the farmer {string} with password {string}, address {string}, and name {string} shall exist in the system \\(p9)")
  public void the_farmer_with_password_address_and_name_shall_exist_in_the_system_p9(String email,
      String password, String address, String name) {

    List<Farmer> farmerList = manager.getFarmers();

    boolean farmerExists = farmerList.stream()
        .anyMatch(f -> f.getEmail().equals(email) && f.getPassword().equals(password)
            && f.getAddress().equals(address)
            && (name == null || name.isEmpty() || f.getName().equals(name)));
    Assertions.assertTrue(farmerExists,
        String.format("Farmer with email='%s', password='%s', address='%s', name='%s' not found.",
            email, password, address, name));
  }

  /**
   * Verifies that all farmers specified in the data table exist in the system
   * 
   * @param dataTable Cucumber data table containing expected farmer info: email, password, address,
   *        and name
   * @author Lina Guezi
   */
  @Then("the following farmers shall exist in the system \\(p9)")
  public void the_following_farmers_shall_exist_in_the_system_p9(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps();

    for (var row : rows) {
      String email = row.get("email");
      String password = row.get("password");
      String address = row.get("address");
      String name = row.get("name");

      the_farmer_with_password_address_and_name_shall_exist_in_the_system_p9(email, password,
          address, name);
    }
  }

  /**
   * Verifies that the error message returned by the controller matches the expected error string
   * 
   * @param expectedError String containing the error
   * @author Lina Guezi
   */
  @Then("the error {string} shall be raised \\(p9)")
  public void the_error_shall_be_raised_p9(String expectedError) {
    Assertions.assertEquals(expectedError, error,
        String.format("Expected error '%s' but got '%s'.", expectedError, error));
  }
}
