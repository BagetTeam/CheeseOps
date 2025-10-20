package ca.mcgill.ecse.cheecsemanager.features;

import java.util.List;
import java.util.Map;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Order;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.Transaction;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet5Controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Date;

public class SellCheeseWheelsToWholesaleCompanyStepDefinitions {

  private String lastErrorMessage;

  /**
   * Creates wholesale companies from the provided data table and adds them to the singleton
   * instance of the CheECSEManager. Each row must contain a name and address.
   * 
   * @param dataTable The DataTable containing wholesale company data
   * @author Yanni Klironomos
   */
  @Given("the following wholesale company exists in the system \\(p15)")
  public void the_following_wholesale_company_exists_in_the_system_p15(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();

    for (Map<String, String> row : rows) {
      String name = row.get("name");
      String address = row.get("address");

      // Check for duplicates if needed (constructor already throws if name is duplicate)
      new WholesaleCompany(name, address, manager);
    }
  }

  /**
   * Marks the specified cheese wheels as spoiled based on IDs provided in the data table. Retrieves
   * all cheese wheels from the singleton instance of CheECSEManager and updates their status.
   * 
   * @param dataTable The DataTable containing cheese wheel IDs to mark as spoiled (column "id")
   * @author Yanni Klironomos
   */
  @Given("the following cheese wheels are spoiled \\(p15)")
  public void the_following_cheese_wheels_are_spoiled_p15(
      io.cucumber.datatable.DataTable dataTable) {
    // Get all cheese wheels from the manager
    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager(); // get singleton
                                                                            // instance
    List<CheeseWheel> cheeseWheels = manager.getCheeseWheels(); // call instance method correctly

    // Parse IDs from the DataTable
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> row : rows) {
      int cheeseWheelId = Integer.parseInt(row.get("id"));

      // Find cheese wheel by id
      for (CheeseWheel cw : cheeseWheels) {
        if (cw.getId() == cheeseWheelId) {
          // Mark as spoiled
          cw.setIsSpoiled(true);
          break;
        }
      }
    }
  }

  /**
   * Creates farmer accounts from the provided data table and adds them to the singleton instance of
   * the CheECSEManager. Each row must contain an email, password, and address.
   * 
   * @param dataTable The DataTable containing farmer account data with columns "email", "password",
   *        and "address"
   * @author Yanni Klironomos
   */
  @Given("the following farmer exists in the system \\(p15)")
  public void the_following_farmer_exists_in_the_system_p15(
      io.cucumber.datatable.DataTable dataTable) {
    // Get singleton manager instance
    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();

    // Convert DataTable to list of maps (each map represents a farmer's properties)
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> row : rows) {
      String email = row.get("email");
      String password = row.get("password");
      String address = row.get("address");

      // Add farmer to the system
      Farmer farmer = new Farmer(email, password, address, manager);
      farmer.setName(row.get("name"));
    }
  }



  /**
   * @param dataTable The DataTable containing purchase data with columns "purchaseDate",
   *        "nrCheeseWheels", "monthsAged", and "farmerEmail"
   * @author Ben
   */
  @Given("the following purchase exists in the system \\(p15)")
  public void the_following_purchase_exists_in_the_system_p15(
      io.cucumber.datatable.DataTable dataTable) {

    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();

    List<Map<String, String>> rows = dataTable.asMaps();

    for (Map<String, String> row : rows) {
      int nrCheeseWheels = Integer.parseInt(row.get("nrCheeseWheels"));
      CheeseWheel.MaturationPeriod monthsAged =
          CheeseWheel.MaturationPeriod.valueOf(row.get("monthsAged"));
      Date purchaseDate = Date.valueOf(row.get("purchaseDate"));
      String farmerEmail = row.get("farmerEmail");

      Farmer farmer = null;
      for (Farmer f : manager.getFarmers()) {
        if (f.getEmail().equals(farmerEmail)) {
          farmer = f;
          break;
        }
      }

      Purchase purchase = new Purchase(purchaseDate, manager, farmer);

      for (int i = 0; i < nrCheeseWheels; i++) {
        new CheeseWheel(monthsAged, false, purchase, manager);
      }
    }
  }

  /**
   * @param dataTable The DataTable containing purchase data with column "purchaseId"
   * @author Ben
   */
  @Given("all cheese wheels for the following purchases are created \\(p15)")
  public void all_cheese_wheels_for_the_following_purchases_are_created_p15(
      io.cucumber.datatable.DataTable dataTable) {
    // We believe this is not needed as cheese wheels are made in above step def
  }

  /**
   * @param dataTable The DataTable containing order data with columns transactionDate,
   *        nrCheeseWheels, monthsAged, deliveryDate, company
   * @author Ben
   */
  @Given("the following order exists in the system \\(p15)")
  public void the_following_order_exists_in_the_system_p15(
      io.cucumber.datatable.DataTable dataTable) {

    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();

    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> row : rows) {
      Date transactionDate = Date.valueOf(row.get("transactionDate"));
      int nrCheeseWheels = Integer.parseInt(row.get("nrCheeseWheels"));
      CheeseWheel.MaturationPeriod monthsAged =
          CheeseWheel.MaturationPeriod.valueOf(row.get("monthsAged"));
      Date deliveryDate = Date.valueOf(row.get("deliveryDate"));
      String companyName = row.get("company");

      WholesaleCompany company = WholesaleCompany.getWithName(companyName);

      new Order(transactionDate, manager, nrCheeseWheels, monthsAged, deliveryDate, company);
    }
  }

  /**
   *
   * @param int1 purchase number
   * @param int2 order number
   * @author jane chattat
   */
  @Given("all non-spoiled cheese wheels from purchase {int} are added to order {int} \\(p15)")
  public void all_non_spoiled_cheese_wheels_from_purchase_are_added_to_order_p15(Integer purchaseId,
      Integer orderId) {
    // Write code here that turns the phrase above into concrete actions
    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();
    Order anOrder = null;
    for (Transaction transaction : manager.getTransactions()) {
      if (transaction instanceof Order && transaction.getId() == orderId) {
        anOrder = (Order) transaction;
        break;
      }
    }

    for (CheeseWheel wheel : manager.getCheeseWheels()) {
      if (wheel.getPurchase() != null && wheel.getPurchase().getId() == purchaseId
          && !wheel.isIsSpoiled()) {
        anOrder.addCheeseWheel(wheel);
        if (anOrder.numberOfCheeseWheels() == anOrder.getNrCheeseWheels()) {
          break;
        }
      }
    }
  }

  /**
   * @author Mahad, Revised by Yanni
   */
  @When("the facility manager attempts to add an order in the system with transaction date {string}, {int} cheese wheels, months aged {string}, delivery date {string}, and company {string} \\(p15)")
  public void the_facility_manager_attempts_to_add_an_order_in_the_system_with_transaction_date_cheese_wheels_months_aged_delivery_date_and_company_p15(
      String transactionDateStr, Integer nrCheeseWheels, String monthsAged, String deliveryDateStr,
      String companyName) {

    Date transactionDate = Date.valueOf(transactionDateStr);
    Date deliveryDate = Date.valueOf(deliveryDateStr);

    // now uses controller instead of model as above, required for @When
    lastErrorMessage = CheECSEManagerFeatureSet5Controller.sellCheeseWheels(companyName,
        transactionDate, nrCheeseWheels, monthsAged, deliveryDate);
  }


  /**
   * @author Matthew, revised by Yanni
   */
  @Then("the number of orders in the system shall be {int} \\(p15)")
  public void the_number_of_orders_in_the_system_shall_be_p15(Integer expectedCount) {
    ca.mcgill.ecse.cheecsemanager.model.CheECSEManager manager =
        ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.getCheecseManager();

    int actual = 0;
    for (WholesaleCompany company : manager.getCompanies()) {
      actual += company.getOrders().size();
    }

    // Compare against the method parameter
    assertEquals(expectedCount.intValue(), actual, "Unexpected number of orders in the system.");
  }

  /**
   * @author Ben
   */
  @Then("the order {int} with transaction date {string}, {int} cheese wheels, months aged {string}, delivery date {string}, and company {string} shall exist in the system \\(p15)")
  public void the_order_with_transaction_date_cheese_wheels_months_aged_delivery_date_and_company_shall_exist_in_the_system_p15(
      Integer orderId, String transactionDate, Integer cheeseWheels, String monthsAged,
      String deliveryDate, String companyName) {

    WholesaleCompany company = WholesaleCompany.getWithName(companyName);

    assertNotNull(company, "Company not found with name " + companyName);

    Order order = null;
    for (Order o : company.getOrders()) {
      if (o.getId() == orderId) {
        order = o;
        break;
      }
    }
    assertNotNull(order, "Order with ID " + order.getId() + " not found.");
    assertEquals(transactionDate, order.getTransactionDate().toString());
    assertEquals(cheeseWheels, order.getNrCheeseWheels());
    assertEquals(monthsAged, order.getMonthsAged().toString());
    assertEquals(deliveryDate, order.getDeliveryDate().toString());
    assertEquals(companyName, order.getCompany().getName());
  }


  /**
   * Checks that a given number of cheese wheels from a specific purchase have been added to a
   * particular order in the system (p15).
   * 
   * @param expectedCount the expected number of cheese wheels from the purchase
   * @param purchaseId the ID of the purchase
   * @param orderId the ID of the target order
   * 
   * @author Yanni Klironomos
   */
  @Then("{int} cheese wheels from purchase {int} shall be added to the order {int} \\(p15)")
  public void cheese_wheels_from_purchase_shall_be_added_to_the_order_p15(Integer expectedCount,
      Integer purchaseId, Integer orderId) {

    CheECSEManager manager = CheECSEManagerApplication.getCheecseManager();

    // Find the target order by ID
    Order targetOrder = null;
    for (Transaction transaction : manager.getTransactions()) {
      if (transaction instanceof Order && transaction.getId() == orderId) {
        targetOrder = (Order) transaction;
        break;
      }
    }

    // Fail if the order doesn't exist
    assertNotNull(targetOrder, "Order with ID " + orderId + " was not found.");


    // Count cheese wheels in the order that came from the target purchase
    int actualCount = 0;
    for (CheeseWheel cw : targetOrder.getCheeseWheels()) {
      if (cw.getPurchase() != null && cw.getPurchase().getId() == purchaseId) {
        actualCount++;
      }
    }
    // Compare expected vs actual
    assertEquals(expectedCount.intValue(), actualCount,
        "Unexpected number of cheese wheels from purchase " + purchaseId + " in order " + orderId
            + ".");
  }

  /**
   * Verifies that all orders listed in the provided DataTable exist in the system with the correct
   * transaction date, number of cheese wheels, maturation period, delivery date, and company.
   * 
   * The DataTable must contain columns: id, transactionDate, nrCheeseWheels, monthsAged,
   * deliveryDate, company
   * 
   * @param dataTable The Cucumber DataTable containing expected order information
   * @author Yanni Klironomos
   */
  @Then("the following orders shall exist in the system \\(p15)")
  public void the_following_orders_shall_exist_in_the_system_p15(
      io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> row : rows) {
      Date transactionDate = Date.valueOf(row.get("transactionDate"));
      int nrCheeseWheels = Integer.parseInt(row.get("nrCheeseWheels"));
      CheeseWheel.MaturationPeriod monthsAged =
          CheeseWheel.MaturationPeriod.valueOf(row.get("monthsAged"));
      Date deliveryDate = Date.valueOf(row.get("deliveryDate"));
      String companyName = row.get("company");

      WholesaleCompany company = WholesaleCompany.getWithName(companyName);
      assertNotNull(company, "Company not found with name " + companyName);


      Order order = null;
      for (Order o : company.getOrders()) {
        if (o.getTransactionDate().equals(transactionDate)
            && o.getNrCheeseWheels() == nrCheeseWheels && o.getMonthsAged() == monthsAged
            && o.getDeliveryDate().equals(deliveryDate)
            && o.getCompany().getName().equals(companyName)) {
          order = o;
        }
      }

      assertNotNull(order, "Order not found with specified attributes: " + row.toString());
    }
  }


  /**
   * Verifies that the specified error message is raised during the test scenario. This step checks
   * for the presence of the error string provided as input.
   * 
   * @param errorMessage The expected error message to be raised
   * @throws io.cucumber.java.PendingException Indicates that the implementation is still pending
   * @author Yanni Klironomos
   */
  @Then("the error {string} shall be raised \\(p15)")
  public void the_error_shall_be_raised_p15(String errorMessage) {
    assertEquals(errorMessage, lastErrorMessage);
  }

}
