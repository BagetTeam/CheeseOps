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

  /** Builds the singleton instance and loads the initial wholesale company list. */
  private WholesaleCompanyDataProvider() { refresh(); }

  /** @return global provider instance */
  public static WholesaleCompanyDataProvider getInstance() { return INSTANCE; }

  /** @return observable list of wholesale companies */
  public ObservableList<TOWholesaleCompany> getCompanies() { return companies; }

  /** Reloads wholesale company data from the controller layer. */
  public void refresh() {
    List<TOWholesaleCompany> latestCompanies =
        CheECSEManagerFeatureSet6Controller.getWholesaleCompanies();
    companies.setAll(latestCompanies);
  }
}
