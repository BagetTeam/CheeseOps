package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;
import java.util.ArrayList;
import java.util.List;

public class CheECSEManagerFeatureSet6Controller {

  public static String deleteWholesaleCompany(String name) {
    // var app = CheECSEManagerApplication.getCheecseManager();
    // List<WholesaleCompany> companies = app.getCompanies();
    WholesaleCompany company = WholesaleCompany.getWithName(name);

    if (company == null) {
      return "The wholesale company " + name + " does not exist.";
    }

    if (company.hasOrders()) {
      return "Cannot delete a wholesale company that has ordered cheese.";
    }

    company.delete();
    return "";
  }

  public static TOWholesaleCompany getWholesaleCompany(String name) {
    // throw new UnsupportedOperationException("Implement me!");
    var app = CheECSEManagerApplication.getCheecseManager();
    var companies = app.getCompanies();

    for (WholesaleCompany company: companies) {
      if (company.getName().equals(name)) {
        return _toCompany(company);
      }
    }

    return null;
  }

  // returns all wholesale companies
  public static List<TOWholesaleCompany> getWholesaleCompanies() {
    var app = CheECSEManagerApplication.getCheecseManager();
    var companies = app.getCompanies();
    List<TOWholesaleCompany> retCompanies = new ArrayList<>();

    for (var company: companies) {
      var toCompany = _toCompany(company);
      retCompanies.add(toCompany);
    }

    return retCompanies;
  }

  private static TOWholesaleCompany _toCompany(WholesaleCompany company) {
    var ret = new TOWholesaleCompany(company.getName(), company.getAddress());
    var orders = company.getOrders();

    for (var order : orders) {
      ret.addOrderDate(order.getTransactionDate());
      ret.addMonthsAged(order.getMonthsAged().toString());
      ret.addNrCheeseWheelsOrdered(order.getNrCheeseWheels());
      ret.addNrCheeseWheelsMissing(order.getNrCheeseWheels() - order.getCheeseWheels().size());
      ret.addDeliveryDate(order.getDeliveryDate());
    }
    return ret;
  }

}
