package ca.mcgill.ecse.cheecsemanager.controller;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import ca.mcgill.ecse.cheecsemanager.model.Order;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Olivier Mao
 * */
public class CheECSEManagerFeatureSet5Controller {

  private static int monthsToInt(MaturationPeriod period) {
    return switch (period) {
      case Six -> 6;
      case Twelve -> 12;
      case TwentyFour -> 24;
      case ThirtySix -> 36;
      default -> 0;
    };
  }
  public static String sellCheeseWheels(String nameCompany, Date orderDate,
                                        Integer nrCheeseWheels,
                                        String monthsAged, Date deliveryDate) {
    var app = CheECSEManagerApplication.getCheecseManager();

    // nrCheeseWheels > 0
    if (nrCheeseWheels == null || nrCheeseWheels <= 0) {
      return "nrCheeseWheels must be greater than zero.";
    } else if (deliveryDate == null) {
      return "Delivery date cannot be null";
    } else if (orderDate != null &&
               deliveryDate.before(
                   orderDate)) { // delivery date after transaction date
      return "The delivery date must be on or after the transaction date.";
    }

    // Find wholesale company
    WholesaleCompany company = null;
    for (WholesaleCompany wc : app.getCompanies()) {
      if (wc.getName().equals(nameCompany)) {
        company = wc;
        break;
      }
    }

    if (company == null) {
      return "The wholesale company " + nameCompany + " does not exist.";
    }

    // convert string monthsAged to MaturationPeriod enum
    MaturationPeriod maturationPeriod;
    switch (monthsAged) {
    case "Six":
      maturationPeriod = MaturationPeriod.Six;
      break;
    case "Twelve":
      maturationPeriod = MaturationPeriod.Twelve;
      break;
    case "TwentyFour":
      maturationPeriod = MaturationPeriod.TwentyFour;
      break;
    case "ThirtySix":
      maturationPeriod = MaturationPeriod.ThirtySix;
      break;
    default:
      return "The monthsAged must be Six, Twelve, TwentyFour, or ThirtySix.";
    }
    // Find cheese wheels that match monthsAged and maturation constraints
    List<CheeseWheel> availableWheels = new ArrayList<>();
    for (CheeseWheel wheel : app.getCheeseWheels()) {
      // all cheeseWheels must mature at monthsAged
      if (wheel.getMonthsAged().equals(maturationPeriod) &&
          !wheel.getIsSpoiled() && wheel.getOrder() == null) {

        // delivery date must be after maturation date
        Purchase purchase = wheel.getPurchase();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(purchase.getTransactionDate());

        calendar.add(Calendar.MONTH, monthsToInt(maturationPeriod));
        Date maturationDate = new Date(calendar.getTimeInMillis());

        // check if delivery date is on or after maturation date
        if (deliveryDate.after(maturationDate) ||
            deliveryDate.equals(maturationDate)) {
          availableWheels.add(wheel);
        }
      }
    }

    try {
      // create order and add to transactions
      Order order = new Order(orderDate, app, nrCheeseWheels, maturationPeriod,
                              deliveryDate, company);

      app.addTransaction(order);

      // assign cheese wheels
      int wheelsAssigned = 0;
      for (CheeseWheel wheel : availableWheels) {
        if (wheelsAssigned < nrCheeseWheels) {
          order.addCheeseWheel(wheel);
          wheel.setLocation(null); // Remove from shelf
          wheelsAssigned++;
        }
      }

      return "";

    } catch (Exception e) {
      return e.getMessage();
    }
  }

  public static String addWholesaleCompany(String name, String address) {
    var app = CheECSEManagerApplication.getCheecseManager();

    // Constraint: name <> "" and name <> null
    if (name == null || name.equals("")) {
      return "Name must not be empty.";
    } else if (address == null || address.equals("")) {
      return "Address must not be empty.";
    }

    for (WholesaleCompany wc : app.getCompanies()) {
      if (wc.getName().equals(name)) {
        return "The wholesale company already exists.";
      }
    }
    try {
      new WholesaleCompany(name, address, app);
      return "";
    } catch (Exception e) {
      return e.getMessage();
    }
  }

  public static String updateWholesaleCompany(String name, String newName,
                                              String newAddress) {
    var app = CheECSEManagerApplication.getCheecseManager();

    // Constraint: newName <> "" and newName <> null
    if (newName == null || newName.trim().isEmpty()) {
      return "The name must not be empty.";
    } else if (newAddress == null || newAddress.trim().isEmpty()) {
      return "The address must not be empty.";
    }

    WholesaleCompany company = null;
    for (WholesaleCompany wc : app.getCompanies()) {
      if (wc.getName().equals(name)) {
        company = wc;
        break;
      }
    }

    if (company == null) {
      return "The wholesale company " + name + " does not exist.";
    }

    for (WholesaleCompany wc : app.getCompanies()) {
      if (wc.getName().equals(newName) && wc != company) {
        return "The wholesale company " + newName + " already exists.";
      }
    }

    try {
      company.setName(newName);
      company.setAddress(newAddress);
      return "";
    } catch (Exception e) {
      return e.getMessage();
    }
  }
}
