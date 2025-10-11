package ca.mcgill.ecse.cheecsemanager.features;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet4Controller;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.CheECSEManager;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.Transaction;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BuyCheeseWheelsFromFarmerStepDefinitions {

  private String error; // store error message

  // store purchase info for Given all cheese wheels from purchase <int> are created to use
  private Map<Integer, Integer> nrCheeseWheelsPurchase = new HashMap<>(); //
  private Map<Integer, CheeseWheel.MaturationPeriod> maturationPeriodPurchase = new HashMap<>();

  /**
   * Step definition to create existing farmers in the system (Given).
   *
   * @author Jennifer You (jenni4u)
   * @param dataTable cucumber datatable with headers email, password, address and
   *                  name
   */
  @Given("the following farmer exists in the system \\(p8)")
  public void the_following_farmer_exists_in_the_system_p8(
          io.cucumber.datatable.DataTable dataTable) {
    CheECSEManager cheecseManager = CheECSEManagerApplication.getCheecseManager();
    List<Map<String, String>> rows = dataTable.asMaps();

    for (var row : rows) {
      // initialize farmer attributes for each listed instance
      String email = row.get("email");
      String password = row.get("password");
      String address = row.get("address");
      String name = row.get("name");

      // create farmer instance
      Farmer farmer = new Farmer(email, password, address, cheecseManager);

      // set farmer name if given
      if (name != null && !name.isEmpty()) {
        farmer.setName(name);
      }
    }
  }

  /**
   * Step definition to create existing purchases in the system (Given).
   *
   * @author Flavie Qin (flavieq88)
   * @param dataTable cucumber datatable with headers purchaseDate,
   *                  nrCheeseWheels, monthsAged and farmerEmail
   */
  @Given("the following purchase exists in the system \\(p8)")
  public void the_following_purchase_exists_in_the_system_p8(
          io.cucumber.datatable.DataTable dataTable) {
    CheECSEManager cheecseManager = CheECSEManagerApplication.getCheecseManager();
    List<Map<String, String>> rows = dataTable.asMaps();

    // create each purchase in the CheECSEManager system
    for (var row : rows) {
      // get field values from datatable
      Date purchaseDate = Date.valueOf(row.get("purchaseDate"));
      int nrCheeseWheels = Integer.parseInt(row.get("nrCheeseWheels"));
      CheeseWheel.MaturationPeriod maturationPeriod = CheeseWheel.MaturationPeriod.valueOf(row.get("monthsAged"));
      String farmerEmail = row.get("farmerEmail");
      Farmer farmer = (Farmer) Farmer.getWithEmail(farmerEmail);

      // create purchase
      Purchase purchase = new Purchase(purchaseDate, cheecseManager, farmer);

      // store information about purchase for next step
      nrCheeseWheelsPurchase.put(purchase.getId(), nrCheeseWheels);
      maturationPeriodPurchase.put(purchase.getId(), maturationPeriod);
    }
  }

  /**
   * Step definition to create cheese wheels associated with a certain purchase
   * (Given).
   *
   * @author Flavie Qin (flavieq88)
   * @param purchaseId integer id of the purchase
   */
  @Given("all cheese wheels from purchase {int} are created \\(p8)")
  public void all_cheese_wheels_from_purchase_are_created_p8(Integer purchaseId) {
    CheECSEManager cheecseManager = CheECSEManagerApplication.getCheecseManager();
    List<Transaction> transactions = cheecseManager.getTransactions();

    for (Transaction transaction : transactions) {
      if (transaction instanceof Purchase && transaction.getId() == purchaseId) { // find purchase by ID
        Purchase purchase = (Purchase) transaction;
        // create cheese wheels
        for (int i = 0; i < nrCheeseWheelsPurchase.get(purchaseId); i++) {
          new CheeseWheel(maturationPeriodPurchase.get(purchaseId), false, purchase, cheecseManager);
        }
      }
    }
  }

  /**
   * Step definition for imitating a facility manager attempting a purchase of
   * cheese wheels from a farmer (When).
   *
   * @author Qiuyu Huang (redacted24)
   * @param date           date of purchase
   * @param nrCheeseWheels number of cheese wheel(s) in the purchase
   * @param monthsAged     maturation period of cheese wheel(s)
   * @param email          email of the farmer
   */
  @When("the facility manager attempts to add a purchase in the system with purchaseDate {string}, {int} cheese wheels, monthsAged {string}, and farmerEmail {string} \\(p8)")
  public void the_facility_manager_attempts_to_add_a_purchase_in_the_system_with_purchase_date_cheese_wheels_months_aged_and_farmer_email_p8(
          String date, Integer nrCheeseWheels, String monthsAged, String email) {
    Date purchaseDate = Date.valueOf(date);
    // call controller and save return message
    error = CheECSEManagerFeatureSet4Controller.buyCheeseWheels(email, purchaseDate, nrCheeseWheels, monthsAged);
  }

  /**
   * Step definition to confirm that the system has the correct number of
   * purchases (Then).
   *
   * @author Cyrus Fung (cfung89)
   * @param nrOfPurchases number of purchases expected to be in the system
   */
  @Then("the number of purchases in the system shall be {int} \\(p8)")
  public void the_number_of_purchases_in_the_system_shall_be_p8(Integer nrOfPurchases) {
    CheECSEManager cheecseManager = CheECSEManagerApplication.getCheecseManager();
    List<Transaction> transactions = cheecseManager.getTransactions();

    // count number of purchases in transactions since a transaction can be either
    // a Purchase or an Order
    int count = 0;
    for (Transaction transaction : transactions) {
      if (transaction instanceof Purchase) {
        count++;
      }
    }

    assertEquals(nrOfPurchases, count);
  }

  /**
   * Step definition to confirm that the system has the correct number of
   * purchases (Then).
   *
   * @author Cyrus Fung (cfung89)
   * @param id    integer id of the purchase
   * @param date  date of the purchase
   * @param email email address of the farmer who is being purchased from.
   */
  @Then("the purchase {int} with purchaseDate {string} and farmerEmail {string} shall exist in the system \\(p8)")
  public void the_purchase_with_purchase_date_and_farmer_email_shall_exist_in_the_system_p8(
          Integer id, String date, String email) {
    CheECSEManager cheecseManager = CheECSEManagerApplication.getCheecseManager();
    List<Transaction> transactions = cheecseManager.getTransactions();
    Purchase purchase = null;

    for (Transaction transaction : transactions) {
      if (transaction instanceof Purchase && transaction.getId() == id) { // match purchase by id
        purchase = (Purchase) transaction;
        break;
      }
    }

    assertNotNull(purchase, "Purchase with id " + id + " was not found."); // check that the purchase was found

    Date actualDate = Date.valueOf(date); // convert String date into a Date type
    Date purchaseDate = purchase.getTransactionDate();
    String purchaseEmail = purchase.getFarmer().getEmail();

    assertEquals(actualDate, purchaseDate); // verify purchase date
    assertEquals(email, purchaseEmail); // verify purchase email
  }

  /**
   * Step definition to verify that a purchase has the expected number of cheese
   * wheels (Then).
   *
   * @author Aurore Zhang (shengggaoyun)
   * @param id                  integer id of the purchase
   * @param expectedWheelsCount the expected number of cheese wheels
   */
  @Then("the purchase {int} shall have {int} cheese wheels \\(p8)")
  public void the_purchase_shall_have_cheese_wheels_p8(Integer id, Integer expectedWheelsCount) {
    CheECSEManager cheecseManager = CheECSEManagerApplication.getCheecseManager();

    List<Transaction> transactions = cheecseManager.getTransactions();
    Purchase purchase = null;

    for (Transaction transaction : transactions) {
      if (transaction instanceof Purchase && transaction.getId() == id) {
        purchase = (Purchase) transaction;
      }
    }
    assertNotNull(purchase, "Purchase with id " + id + " was not found.");
    assertEquals(expectedWheelsCount.intValue(), purchase.getCheeseWheels().size());
  }

  /**
   * Step definition to verify that all cheese wheels of a given purchase have the
   * expected monthsAged value (Then).
   *
   * @author Aurore Zhang (shengggaoyun)
   * @param id                 integer id of the purchase
   * @param expectedMonthsAged the expected monthsAged value
   */

  @Then("all cheese wheels for purchase {int} shall have monthsAged {string} \\(p8)")
  public void all_cheese_wheels_for_purchase_shall_have_months_aged_p8(Integer id,
                                                                       String expectedMonthsAged) {
    CheECSEManager cheecseManager = CheECSEManagerApplication.getCheecseManager();

    List<Transaction> transactions = cheecseManager.getTransactions();
    Purchase purchase = null;

    for (Transaction transaction : transactions) {
      if (transaction instanceof Purchase && transaction.getId() == id) {
        purchase = (Purchase) transaction;
      }
    }
    assertNotNull(purchase, "Purchase with id " + id + " was not found.");
    CheeseWheel.MaturationPeriod expectedMaturationPeriod = CheeseWheel.MaturationPeriod.valueOf(expectedMonthsAged);
    for (CheeseWheel cheeseWheel : purchase.getCheeseWheels()) {
      assertEquals(expectedMaturationPeriod, cheeseWheel.getMonthsAged());
    }
  }

  /**
   * Step definition to confirm number of cheese wheels in system is correct
   * (Then).
   *
   * @author Kenneth Wang (KennethWang6)
   * @param nrOfCheese the number of cheese wheels
   */
  @Then("the number of cheese wheels in the system shall be {int} \\(p8)")
  public void the_number_of_cheese_wheels_in_the_system_shall_be_p8(Integer nrOfCheese) {
    CheECSEManager cheecseManager = CheECSEManagerApplication.getCheecseManager();
    assertEquals(nrOfCheese, cheecseManager.numberOfCheeseWheels());
  }

  /**
   * Step definition to assert valid purchases are created and in the system
   * (Then).
   *
   * @author Kenneth Wang (KennethWang6)
   * @param dataTable cucumber datatable with headers purchaseDate,
   *                  nrCheeseWheels, monthsAged and farmerEmail
   */
  @Then("the following purchases shall exist in the system \\(p8)")
  public void the_following_purchases_shall_exist_in_the_system_p8(
          io.cucumber.datatable.DataTable dataTable) {
    CheECSEManager cheecseManager = CheECSEManagerApplication.getCheecseManager();
    List<Transaction> transactions = cheecseManager.getTransactions();
    List<Map<String, String>> rows = dataTable.asMaps();

    for (var row : rows) {
      Date purchaseDate = Date.valueOf(row.get("purchaseDate"));
      int nrCheeseWheels = Integer.parseInt(row.get("nrCheeseWheels"));
      CheeseWheel.MaturationPeriod monthsAged = CheeseWheel.MaturationPeriod.valueOf(row.get("monthsAged"));
      String farmerEmail = row.get("farmerEmail");

      Purchase purchase = null;
      // compare every attribute of Transaction
      for (Transaction transaction : transactions) {
        // check only for purchases
        if (transaction instanceof Purchase) {
          Purchase p = (Purchase) transaction;
          String transactionEmail = p.getFarmer().getEmail();
          Date transactionDate = p.getTransactionDate();

          // transaction found
          if (transactionDate.equals(purchaseDate)
                  && p.numberOfCheeseWheels() == nrCheeseWheels
                  && p.getCheeseWheels().get(0).getMonthsAged() == monthsAged
                  && transactionEmail.equals(farmerEmail)) {
            purchase = (Purchase) transaction;
            break;
          }
        }
      }
      assertNotNull(purchase, "Purchase with purchase date " + purchaseDate.toString() + ", nrCheeseWheels " +
              nrCheeseWheels + ", maturation period " + monthsAged.toString() + " and farmer email " +
              farmerEmail + " was not found."); // check transaction found
    }
  }

  /**
   * Step definition to assert that invalid purchases are indeed not created
   * (Then).
   *
   * @author Carolyn Wu (cw118)
   * @param id the ID of the purchase
   */
  @Then("the purchase {int} shall not exist in the system \\(p8)")
  public void the_purchase_shall_not_exist_in_the_system_p8(Integer id) {
    CheECSEManager cheecseManager = CheECSEManagerApplication.getCheecseManager();

    List<Transaction> transactions = cheecseManager.getTransactions();
    Purchase purchase = null;

    for (Transaction transaction : transactions) {
      // no need to unbox the Integer (id) since it is being compared with a primitive
      // int
      if (transaction instanceof Purchase && transaction.getId() == id) {
        purchase = (Purchase) transaction;
      }
    }

    // check that no purchase matching the provided id was found
    assertNull(purchase, "A purchase with id " + id + " was found when it should not exist.");
  }

  /**
   * Step definition to confirm that an error with the correct message is raised
   * (Then).
   *
   * @author Carolyn Wu (cw118)
   * @param errorMessage the expected error message
   */
  @Then("the error {string} shall be raised \\(p8)")
  public void the_error_shall_be_raised_p8(String errorMessage) {
    assertEquals(errorMessage, error);
  }
}
