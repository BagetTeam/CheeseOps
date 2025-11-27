package ca.mcgill.ecse.cheecsemanager.fxml.store;

import java.util.List;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FarmerDataProvider {
  private static final FarmerDataProvider INSTANCE = new FarmerDataProvider();

  private final ObservableList<TOFarmer> farmers =
      FXCollections.observableArrayList();
  
  private FarmerDataProvider() { refresh(); }

  public static FarmerDataProvider getInstance() { return INSTANCE; }

  public ObservableList<TOFarmer> getFarmers() { return farmers; }

  public void refresh() {
    List<TOFarmer> latestFarmers =
        CheECSEManagerFeatureSet7Controller.getFarmers();
    farmers.setAll(latestFarmers);
  }
}