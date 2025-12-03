package ca.mcgill.ecse.cheecsemanager.fxml.store;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Centralized observable source of shelf data so that UI controllers can react
 * to model mutations without reloading the page.
 */
public class WholesaleCompanyDataProvider {
  private static final WholesaleCompanyDataProvider INSTANCE =
      new WholesaleCompanyDataProvider();

  private final ObservableList<TOWholesaleCompany> companies =
      FXCollections.observableArrayList();

  private WholesaleCompanyDataProvider() { refresh(); }

  public static WholesaleCompanyDataProvider getInstance() { return INSTANCE; }

  public ObservableList<TOWholesaleCompany> getCompanies() { return companies; }

  public void refresh() {
    List<TOWholesaleCompany> latestCompanies =
        CheECSEManagerFeatureSet6Controller.getWholesaleCompanies();
    companies.setAll(latestCompanies);
  }
}
