package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller class handles business logic related to wholesale companies,
 * including creation, retrieval, and deletion of company data.
 * @author Benjamin Curis-Friedman
 * */
public class CheECSEManagerFeatureSet6Controller {
  /**
   * Deletes a wholesale company from the system by its name. The company cannot be
   * deleted if it has existing orders.
   * 
   * @param name The name of the wholesale company to delete.
   * @return An empty string on successful deletion, or an error message if the
   *         company does not exist or has orders.
   */
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
   * Retrieves a specific wholesale company by its name and returns it as a
   * transfer object.
   * 
   * @param name The name of the company to retrieve.
   * @return A {@link TOWholesaleCompany} transfer object containing the company's
   *         details, or null if no company with that name is found.
   */
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
   * Retrieves a list of all wholesale companies currently in the system.
   * 
   * @return A list of {@link TOWholesaleCompany} objects, with each object
   *         representing a wholesale company.
   */
  public static List<TOWholesaleCompany> getWholesaleCompanies() {
    var app = CheECSEManagerApplication.getCheecseManager();
    var companies = app.getCompanies();
    List<TOWholesaleCompany> retCompanies = new ArrayList<>();

    // Iterate through all companies and convert them to transfer objects
    for (var company : companies) {
      var toCompany = _toCompany(company);
      retCompanies.add(toCompany);
    }

    return retCompanies;
  }

  /**
   * A private helper method to convert a {@link WholesaleCompany} model object
   * into a {@link TOWholesaleCompany} transfer object.
   * 
   * @param company The {@link WholesaleCompany} model object to convert.
   * @return The corresponding {@link TOWholesaleCompany} transfer object.
   */
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
