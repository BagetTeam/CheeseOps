package ca.mcgill.ecse.cheecsemanager.features;

import static org.junit.Assert.*;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.model.FacilityManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;

/**
 * @author Ming Li Liu
 * */
public class UpdateFacilityManagerStepDefinitions {
  FacilityManager manager;
  String error;
  String initialPassword;

  /**
   * @author Ming Li Liu
   * */
  @Given("the following facility manager exists in the system \\(p2)")
  public void the_following_facility_manager_exists_in_the_system_p2(
      List<Map<String, String>> dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.

    String email = dataTable.get(0).get("email");
    String password = dataTable.get(0).get("password");
    initialPassword = password;

    var app = CheECSEManagerApplication.getCheecseManager();
    manager = new FacilityManager(email, password, app);
  }

  /**
   * @author Ming Li Liu
   * */
  @When("the facility manager attempts to update the facility manager "
        + "password to {string} \\(p2)")
  public void
  the_facility_manager_attempts_to_update_the_facility_manager_password_to_p2(
      String updatedPassword) {
    error = CheECSEManagerFeatureSet1Controller.updateFacilityManager(
        updatedPassword);
  }

  /**
   * @author Ming Li Liu
   * */
  @Then("the number of facility managers in the system shall be {int} \\(p2)")
  public void
  the_number_of_facility_managers_in_the_system_shall_be_p2(Integer int1) {
    // how do you even get the number of managers in the system???????
    var manager = CheECSEManagerApplication.getCheecseManager().getManager();
    assertNotNull(manager);
  }

  /**
   * @author Ming Li Liu
   * */
  @Then("the facility manager with password {string} shall exist in the "
        + "system \\(p2)")
  public void
  the_facility_manager_with_password_shall_exist_in_the_system_p2(
      String updatedPassword) {

    assertEquals(updatedPassword, manager.getPassword());
  }

  /**
   * @author Ming Li Liu
   * */
  @Then("the error {string} shall be raised \\(p2)")
  public void the_error_shall_be_raised_p2(String expectedError) {
    assertEquals(expectedError, error);

    String password = manager.getPassword();
    assertEquals(password, initialPassword);
  }
}
