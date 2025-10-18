package ca.mcgill.ecse.cheecsemanager.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel;
import ca.mcgill.ecse.cheecsemanager.model.CheeseWheel.MaturationPeriod;
import ca.mcgill.ecse.cheecsemanager.model.Order;
import ca.mcgill.ecse.cheecsemanager.model.Purchase;
import ca.mcgill.ecse.cheecsemanager.model.WholesaleCompany;


/**
 * @author Olivier Mao
 * */
public class CheECSEManagerFeatureSet5Controller {

  public static String sellCheeseWheels(String nameCompany, Date orderDate, Integer nrCheeseWheels,
      String monthsAged, Date deliveryDate) {
    var app = CheECSEManagerApplication.getCheecseManager();
    
    // nrCheeseWheels > 0
    if (nrCheeseWheels == null || nrCheeseWheels <= 0) {
      return "Number of cheese wheels must be greater than zero";
    } else if (deliveryDate == null) {
      return "Delivery date cannot be null";
    } else if ( orderDate != null && !deliveryDate.after(orderDate)) { // transactionDate < deliveryDate
      return "Delivery date must be after transaction date";
    } 

    // Find wholesale company
    WholesaleCompany company = null;
    for (WholesaleCompany wc: app.getCompanies()) {
      if (wc.getName().equals(nameCompany)) {
        company = wc;
        break;
      }
    }

    if (company == null) {
      return "Wholesale company not found";
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
        return "Invalid months aged value";
    }
    // Find cheese wheels that match monthsAged and maturation constraints
    List<CheeseWheel> availableWheels = new ArrayList<>();
    for (CheeseWheel wheel : app.getCheeseWheels()) {
      // all cheeseWheels must mature at monthsAged
      if (wheel.getMonthsAged().equals(maturationPeriod) && !wheel.getIsSpoiled() && wheel.getLocation() != null && wheel.getOrder() == null) {
        
        // delivery date must be after maturation date
        Purchase purchase = wheel.getPurchase();
        if (purchase != null) {
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(purchase.getTransactionDate());
          int monthsInt = 0;
          switch (maturationPeriod) {
            case Six: monthsInt = 6; break;
            case Twelve: monthsInt = 12; break;
            case TwentyFour: monthsInt = 24; break;
            case ThirtySix: monthsInt = 36; break;
          }
           calendar.add(Calendar.MONTH, monthsInt);
          Date maturationDate = new Date(calendar.getTimeInMillis());

          // check if delivery date is on or after maturation date
          if (deliveryDate.after(maturationDate) || deliveryDate.equals(maturationDate)) {
            availableWheels.add(wheel);
          }
        }
      }
    }
    
    try {
      // create order
      Order order = new Order(orderDate, app, nrCheeseWheels, maturationPeriod, deliveryDate, company);
      
      // assign cheese wheels
      int wheelsAssigned = 0;
      for (CheeseWheel wheel : availableWheels) {
        if (wheelsAssigned < nrCheeseWheels) {
          order.addCheeseWheel(wheel);
          wheel.setLocation(null); // Remove from shelf
          wheelsAssigned++;
        }
      }
      
      if (wheelsAssigned < nrCheeseWheels) {
      int missing = nrCheeseWheels - wheelsAssigned;
        return "Order created with " + wheelsAssigned + " cheese wheels. " + missing + " wheels missing due to insufficient aged inventory or maturation constraints.";
      } else {
        return "Order successfully created with " + wheelsAssigned + " cheese wheels aged " + monthsAged + " months.";
      }
  
    } catch (Exception e) {
      return e.getMessage();
    }

  }
  
  public static String addWholesaleCompany(String name, String address) {
    var app = CheECSEManagerApplication.getCheecseManager();

    // Constraint: name <> "" and name <> null
    if (name == null || name.equals("")) {
      return "Name cannot be empty or null";
    } else if  (address == null || address.equals("")) {
      return "Address cannot be empty or null";
    }

    try {
      new WholesaleCompany(name, address, app);
      return "";
    } catch (Exception e) {
      return e.getMessage();
    }
  }

  public static String updateWholesaleCompany(String name, String newName, String newAddress) {
    var app = CheECSEManagerApplication.getCheecseManager();

    // Constraint: newName <> "" and newName <> null
    if (newName == null || newName.equals("")) {
      return "New name cannot be empty or null";
    } else if  (newAddress == null || newAddress.equals("")) {
      return "New address cannot be empty or null";
    }
        
    WholesaleCompany company = null;
    for (WholesaleCompany wc : app.getCompanies()) {
      if (wc.getName().equals(name)) {
        company = wc;
        break;
      }
    }
        
    if (company == null) {
      return "Company not found";
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
