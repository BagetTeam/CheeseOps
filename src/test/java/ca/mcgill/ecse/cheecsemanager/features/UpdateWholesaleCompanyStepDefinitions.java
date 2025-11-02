package ca.mcgill.ecse.cheecsemanager.features;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet5Controller;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateWholesaleCompanyStepDefinitions {

  private CheECSEManager cheecsemanager = CheECSEManagerApplication.getCheecseManager();
  private String error = "";

  /**
   * @author Ethan Tran
   */
  @Given("the following wholesale company exists in the system \\(p14)")
  public void the_following_wholesale_company_exists_in_the_system_p14(
          io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.

    List<Map<String, String>> companies = dataTable.asMaps();
    for (var row : companies) {
      String name = row.get("name");
      String address = row.get("address");
      cheecsemanager.addCompany(name, address);
    }
  }

  /**
   * @author Minh Vo
   */
  @When("the facility manager attempts to update wholesale company {string} in the system with name {string} and address {string} \\(p14)")
  public void the_facility_manager_attempts_to_update_wholesale_company_in_the_system_with_name_and_address_p14(
          String companyName, String updateName, String updateAddress) {
    callController(CheECSEManagerFeatureSet5Controller.updateWholesaleCompany(companyName, updateName, updateAddress));
  }

  /**
   * @author Yassine Saoucha
   */
  @Then("the number of wholesale companies in the system shall be {int} \\(p14)")
  public void the_number_of_wholesale_companies_in_the_system_shall_be_p14(Integer numCompanies) {
    assertEquals(numCompanies, cheecsemanager.getCompanies().size());
  }

  /**
   * @author Minh Vo
   */
  @Then("the wholesale company with name {string} and address {string} shall exist in the system \\(p14)")
  public void the_wholesale_company_with_name_and_address_shall_exist_in_the_system_p14(
          String name, String address) {
    // Write code here that turns the phrase above into concrete actions
    WholesaleCompany existCompany = WholesaleCompany.getWithName(name);
    assertNotNull(existCompany, "Wholesale company with name " + name + " does not exist in the system. ");
    assertEquals(address, existCompany.getAddress());
  }

  /**
   * @author Harry Yang
   */
  @Then("the wholesale company with name {string} and address {string} shall not exist in the system \\(p14)")
  public void the_wholesale_company_with_name_and_address_shall_not_exist_in_the_system_p14(
          String name, String address) {
    // Write code here that turns the phrase above into concrete actions
    WholesaleCompany wholesaleCompany = WholesaleCompany.getWithName(name);
    if (wholesaleCompany != null) {
      assertNotEquals(address, wholesaleCompany.getAddress());
    }
  }

  /**
   * @author Ethan Tran
   */
  @Then("the following wholesale companies shall exist in the system \\(p14)")
  public void the_following_wholesale_companies_shall_exist_in_the_system_p14(
          io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.

    List<Map<String, String>> expectedCompanies = dataTable.asMaps();
    for (var row : expectedCompanies) {
      String name = row.get("name");
      String address = row.get("address");
      WholesaleCompany company = WholesaleCompany.getWithName(name);
      assertNotNull(company, "Wholesale company with name " + name + " does not exist in the system. ");
      assertEquals(address, company.getAddress());
    }
  }

  /**
   * @author Harry Yang
   */
  @Then("the error {string} shall be raised \\(p14)")
  public void the_error_shall_be_raised_p14(String errorRaised) {
    assertTrue(error.contains(errorRaised), "The error " + errorRaised + " was not raised.");
  }

  /**
   * @author Harry Yang
   */
  private void callController(String result) {
    if (!result.isEmpty()) {
      error = result;
    }
  }
}
