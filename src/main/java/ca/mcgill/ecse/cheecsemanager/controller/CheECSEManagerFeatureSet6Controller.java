package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Benjamin Curis-Friedman
 * */
public class CheECSEManagerFeatureSet6Controller {

  /**
   * Delete the wholesale company from name
   * @param name wholesale company name
   * @return empty instance of {@link string}
   * @throws if wholesale company with provided name does not exist
   * */
  public static String deleteWholesaleCompany(String name) {
    WholesaleCompany company = WholesaleCompany.getWithName(name);

    if (company == null) {
      return "The wholesale company " + name + " does not exist.";
    }

    if (company.hasOrders()) {
      return "Cannot delete a wholesale company that has ordered cheese.";
    }

    // If no orders are associated, delete the company
    company.delete();
    return "";
  }

  /**
   * Get the wholesale company from name
   * @param name company name
   * @return instance of {@link TOWholesaleCompany} associated with the company name
   * */
  public static TOWholesaleCompany getWholesaleCompany(String name) {
    // throw new UnsupportedOperationException("Implement me!");
    WholesaleCompany company = WholesaleCompany.getWithName(name);

    if (company == null) {
      return null;
    }

    // Convert the found company to a transfer object
    return _toCompany(company);
  }

  /**
   * Get the shelf from shelf Id
   * @return List object of {@link TOWholesaleCompany} for all wholesale companies in the system.
   * */
  // returns all wholesale companies
  public static List<TOWholesaleCompany> getWholesaleCompanies() {
    var app = CheECSEManagerApplication.getCheecseManager();
    var companies = app.getCompanies();
    List<TOWholesaleCompany> retCompanies = new ArrayList<>();

    // Iterate through all companies and convert them to transfer objects
    for (var company: companies) {
      var toCompany = _toCompany(company);
      retCompanies.add(toCompany);
    }

    return retCompanies;
  }

  /**
   * Helper function to convert to TO object
   * @param company wholesale company object
   * @return instance of {@link WholesaleCompany}
   * */
  private static TOWholesaleCompany _toCompany(WholesaleCompany company) {
    // Create a new transfer object with company details
    var ret = new TOWholesaleCompany(company.getName(), company.getAddress());
    var orders = company.getOrders();

    // Populate the transfer object with order information
    for (var order : orders) {
      ret.addOrderDate(order.getTransactionDate());
      ret.addMonthsAged(order.getMonthsAged().toString());
      ret.addNrCheeseWheelsOrdered(order.getNrCheeseWheels());
      // Calculate the number of missing cheese wheels for the order
      ret.addNrCheeseWheelsMissing(order.getNrCheeseWheels() - order.getCheeseWheels().size());
      ret.addDeliveryDate(order.getDeliveryDate());
    }
    // Return the fully populated transfer object
    return ret;
  }

}
