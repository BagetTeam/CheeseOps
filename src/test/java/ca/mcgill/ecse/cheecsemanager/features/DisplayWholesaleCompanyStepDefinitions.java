package ca.mcgill.ecse.cheecsemanager.features;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Order;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;



public class DisplayWholesaleCompanyStepDefinitions {

  private CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
  private TOWholesaleCompany companyWithName;
  private int nrCheeseWheels;
  private MaturationPeriod maturationPeriod;
  private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private List<TOWholesaleCompany> companies = CheECSEManagerFeatureSet6Controller.getWholesaleCompanies();

  /**
   * This definition ensures the wholesale company exists in the system
   * @authot Robbin Bilaya
   * @param dataTable
   */
  @Given("the following wholesale company exists in the system \\(p4)")
  public void the_following_wholesale_company_exists_in_the_system_p4(
          io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> row : rows) {
      new WholesaleCompany(row.get("name"), row.get("address"), manager);
    }
  }

  /**
   * This step definition ensures that the farmer exists in the system.
   * @author Robbin Bilaya
   * @param dataTable
   */
  @Given("the following farmer exists in the system \\(p4)")
  public void the_following_farmer_exists_in_the_system_p4(
          io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> row : rows) {
      Farmer newFarmer = new Farmer(row.get("email"), row.get("password"), row.get("address"), manager);
      newFarmer.setName(row.get("name"));
    }
  }

  /**
   * This definition ensures that purchase exists in the system.
   * @author Robbin Bilaya
   * @param dataTable
   */
  @Given("the following purchase exists in the system \\(p4)")
  public void the_following_purchase_exists_in_the_system_p4(
          io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> row : rows) {
      Date purchaseDate = Date.valueOf(LocalDate.parse(row.get("purchaseDate"), DATE_FORMATTER));
      new Purchase(purchaseDate, manager, (Farmer) Farmer.getWithEmail(row.get("farmerEmail")));
      nrCheeseWheels = Integer.parseInt(row.get("nrCheeseWheels"));
      maturationPeriod = MaturationPeriod.valueOf(row.get("monthsAged"));
    }
  }

  /**
   * This definition ensures all cheese wheels from a certain purchase are created in the system
   * @author Robbin Bilaya
   * @param purchaseNr
   */
  @Given("all cheese wheels from purchase {int} are created \\(p4)")
  public void all_cheese_wheels_from_purchase_are_created_p4(Integer purchaseNr) {
    for (int i = 0; i < nrCheeseWheels; i++) {
      new CheeseWheel(maturationPeriod, false, (Purchase) manager.getTransaction(purchaseNr - 1), manager);
    }

  }

  /**
   * Ensures that the given order exists in the system
   * @author Robbin Bilaya
   * @param dataTable
   */
  @Given("the following order exists in the system \\(p4)")
  public void the_following_order_exists_in_the_system_p4(
          io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> row : rows) {
      Date orderDate = Date.valueOf(LocalDate.parse(row.get("orderDate"), DATE_FORMATTER));
      Date deliveryDate = Date.valueOf(LocalDate.parse(row.get("deliveryDate"), DATE_FORMATTER));
      new Order(orderDate, manager, Integer.parseInt(row.get("nrCheeseWheels")), MaturationPeriod.valueOf(row.get("monthsAged")), deliveryDate, WholesaleCompany.getWithName(row.get("companyName")));
    }
  }

  /**
   * Ensures that all non-spoilt cheese from a certain purchase are added to a certain order in the system
   * @author Robbin Bilaya
   * @param purchaseNr
   * @param orderNr
   */
  @Given("all non-spoiled cheese wheels from purchase {int} are added to order {int} \\(p4)")
  public void all_non_spoiled_cheese_wheels_from_purchase_are_added_to_order_p4(Integer purchaseNr,
                                                                                Integer orderNr) {
    Purchase purchase = (Purchase) manager.getTransaction(purchaseNr - 1);
    Order order = (Order) manager.getTransaction(orderNr - 1);
    for (CheeseWheel cheeseWheel : purchase.getCheeseWheels()) {

      if (!cheeseWheel.isIsSpoiled()) {
        order.addCheeseWheel(cheeseWheel);
      }
    }
  }

  /**
   * This definition calls a controller to the display all the wholesale companies in the system
   * @author Robbin Bilaya
   */
  @When("the facility manager attempts to display from the system all the wholesale companies \\(p4)")
  public void the_facility_manager_attempts_to_display_from_the_system_all_the_wholesale_companies_p4() {
    companies = CheECSEManagerFeatureSet6Controller.getWholesaleCompanies();
  }

  /**
   * This definition calls a controller to display a wholesale company with a certain name in the system
   * @author Robbin Bilaya
   * @param name
   */
  @When("the facility manager attempts to display from the system the wholesale company with name {string} \\(p4)")
  public void the_facility_manager_attempts_to_display_from_the_system_the_wholesale_company_with_name_p4(
          String name) {
    companyWithName = CheECSEManagerFeatureSet6Controller.getWholesaleCompany(name);
    companies.clear();
    companies.add(companyWithName);
  }

  /**
   * This definition verifies the number of wholesale companies in the system
   * @author Robbin Bilaya
   * @param assertCompanyNr
   */
  @Then("the number of wholesale companies in the system shall be {int} \\(p4)")
  public void the_number_of_wholesale_companies_in_the_system_shall_be_p4(Integer assertCompanyNr) {
    Integer systemCompanyNr = companies.size();
    assertEquals(assertCompanyNr, systemCompanyNr);
  }

  /**
   * This definition verifies what details about the wholesale companies shall be presented
   * @author Robbin Bilaya
   * @param dataTable
   */
  @Then("the following wholesale company details shall be presented \\(p4)")
  public void the_following_wholesale_company_details_shall_be_presented_p4(
          io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    int i = 0;

    for (Map<String, String> row : rows) {
      String name = companies.get(i).getName();
      String address = companies.get(i).getAddress();
      assertEquals(name, row.get("name"));
      assertEquals(address, row.get("address"));
      i++;

    }
  }

  /**
   * This definition verifies what order details shall be presented for a specific wholesale company in the system
   * @author Robbin Bilaya
   * @param companyName
   * @param dataTable
   */
  @Then("the following order details shall be presented for wholesale company {string} \\(p4)")
  public void the_following_order_details_shall_be_presented_for_wholesale_company_p4(String companyName, io.cucumber.datatable.DataTable dataTable) {
    int i = 0;
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (TOWholesaleCompany c : companies) {
      if (companyName.equals(c.getName())) {
        companyWithName = c;
      }
    }

    for (Map<String, String> row : rows) {
      Date orderDate = Date.valueOf(LocalDate.parse(row.get("orderDate"), DATE_FORMATTER));
      String maturationPeriod = row.get("monthsAged");
      Date deliveryDate = Date.valueOf(LocalDate.parse(row.get("deliveryDate"), DATE_FORMATTER));

      assertEquals(orderDate, companyWithName.getOrderDate(i));
      assertEquals(maturationPeriod, companyWithName.getMonthsAged(i));
      assertEquals(Integer.parseInt(row.get("nrCheeseWheelsOrdered")), companyWithName.getNrCheeseWheelsOrdered(i));
      assertEquals(Integer.parseInt(row.get("nrCheeseWheelsMissing")), companyWithName.getNrCheeseWheelsMissing(i));
      assertEquals(deliveryDate, companyWithName.getDeliveryDate(i));
      i++;
    }
  }

  /**
   * This definition verifies that no order details shall be presented for a specific wholesale company in the system
   * @author Rayyan Lodhi
   * @author Mohamed Elmasry
   * @param companyName
   */
  @Then("no order details shall be presented for wholesale company {string} \\(p4)")
  public void no_order_details_shall_be_presented_for_wholesale_company_p4(String companyName) {
    for (TOWholesaleCompany c : companies) {
      if (companyName.equals(c.getName())) {
        companyWithName = c;
      }
    }

    assertFalse(companyWithName.hasOrderDates(),
            "Found order details for wholesale company'" + companyName + "', expecting none");
  }

  /**
   * This definition verifies that no wholesale companies shall be presented in the system
   */
  @Then("no wholesale companies shall be presented \\(p4)")
  public void no_wholesale_companies_shall_be_presented_p4() {
    assertFalse(companies.isEmpty(),
            "Found wholesale companies but was expecting none");
  }
}
