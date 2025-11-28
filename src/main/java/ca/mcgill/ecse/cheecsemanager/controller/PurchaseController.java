package ca.mcgill.ecse.cheecsemanager.controller;
import java.util.List;
import ca.mcgill.ecse.cheecsemanager.model.Farmer;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;

public class PurchaseController {
  public static List<String> getAllPurchaseIds() {
    var app = CheECSEManagerApplication.getCheecseManager();
    List<Farmer> farmers = app.getFarmers();
    return farmers.stream().flatMap(farmer -> farmer.getPurchases().stream()).map(purchase -> String.valueOf(purchase.getId())).toList();
  }
}