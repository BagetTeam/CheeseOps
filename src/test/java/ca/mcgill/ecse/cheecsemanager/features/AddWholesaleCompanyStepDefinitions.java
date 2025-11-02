package ca.mcgill.ecse.cheecsemanager.features;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet5Controller;

import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;

public class AddWholesaleCompanyStepDefinitions {

  private String lastError;
  private CheECSEManager cheecsemanager;
 
  /**
   * Ensure that a company exists in the system prior to running the
   * tests.
   * 
   * @author Arie Mednikov
   * @param companyTable A Cucumber table containing the name and address of each company
   * to be added by the test.
   */
  @Given("the following wholesale company exists in the system \\(p13)")
  public void the_following_wholesale_company_exists_in_the_system_p13(
      io.cucumber.datatable.DataTable companyTable) {
    cheecsemanager = CheECSEManagerApplication.getCheecseManager();
    List<Map<String, String>> companies = companyTable.asMaps();
    for (var row : companies) {
      String name = row.get("name");
      String address = row.get("address");
      cheecsemanager.addCompany(name, address);
    }
  }

  /**
   * Attempt to add a company to the model with a provided name and address using the
   * appropriate controller method.
   * 
   * @author Christian Pompa
   * @param name The name of the company to be added to the system.
   * @param address The address of the company to be added to the system.
   */
  @When("the facility manager attempts to add a wholesale company in the system with name {string} and address {string} \\(p13)")
  public void the_facility_manager_attempts_to_add_a_wholesale_company_in_the_system_with_name_and_address_p13(
  String name, String address) {
    lastError = CheECSEManagerFeatureSet5Controller.addWholesaleCompany(name, address);
  }
  
  /**
   * Ensure that the number of wholesale companies is correct after having run tests.
   * Will cause the test to fail of the number of companies is not what is expected.
   * 
   * @author Stephane Soldatenkov
   * @param numCompanies The expected number of wholesale companies in the system.
   */
  @Then("the number of wholesale companies in the system shall be {int} \\(p13)")
  public void the_number_of_wholesale_companies_in_the_system_shall_be_p13(Integer numCompanies) {
    int actualCount = cheecsemanager.getCompanies().size();
    assertEquals(numCompanies.intValue(), actualCount, "Expected " + numCompanies + " but got " + actualCount);
  }
  
  /**
   * Ensure that a wholesale company, with a provided name and address, exists in the system.
   * Will fail if this company does not exist in the system.
   * 
   * @author Ethan Valente
   * @param name
   * @param address
   */
  @Then("the wholesale company {string} with address {string} shall exist in the system \\(p13)")
  public void the_wholesale_company_with_address_shall_exist_in_the_system_p13(String name,
      String address) {
	  
	  WholesaleCompany company = WholesaleCompany.getWithName(name);
	  
	  // Assert that the company exists
	  assertNotNull(company, "The company with name " + name + " does not exist.");
	  
	  // Assert that the address matches
	  assertEquals(address, company.getAddress(), "The address for company " + name + " does not match.");
  }
  
  /**
   * Ensure that the provided companies exist in the system. Will fail if any of the provided
   * wholesale companies do not exist in the system.
   * 
   * @author Ed Perrault
   * @param companies A Cucumber datatable containing two columns and an
   * arbitrary amount of rows. One column is for the name of the company and
   * the other is for its address.
   */
  @Then("the following wholesale companies shall exist in the system \\(p13)")
  public void the_following_wholesale_companies_shall_exist_in_the_system_p13(
      io.cucumber.datatable.DataTable companies) {
    List<Map<String, String>> rows = companies.asMaps();
    
    for (var row : rows) {
      String name = row.get("name");
      String expectedAddress = row.get("address");
      WholesaleCompany company = WholesaleCompany.getWithName(name);
      assertNotNull(company, "The company " + name + " doesn't exist.");
      assertEquals(company.getAddress(), expectedAddress, "Company with name " + name +
    		  " has incorrect address. Expected " + expectedAddress + " but got " + company.getAddress() + ".");
    }
  }
  
  /**
   * Ensure that the previous tests have generated the expected errors with the provided
   * error messages. Will fail with the message "Expected an error, but none occurred"
   * if there is no error and "Unexpected error message" if the generated error message
   * is not the same as the expected error message.
   * 
   * @author Stephane Soldatenkov 
   * @param expectedErrorMessage The error expected by the system.
   */
  @Then("the error {string} shall be raised \\(p13)")
  public void the_error_shall_be_raised_p13(String expectedErrorMessage) {
    assertNotNull(lastError, "Expected an error, but none occurred");
    assertEquals(expectedErrorMessage, lastError, "Unexpected error message");
  }
}
