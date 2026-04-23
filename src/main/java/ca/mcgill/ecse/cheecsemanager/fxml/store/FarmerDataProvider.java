package ca.mcgill.ecse.cheecsemanager.fxml.store;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;


/**
 * Data provider for the farmer data
 * Handles the farmer data and cheese wheel data
 * @author Ewen Gueguen
 */
public class FarmerDataProvider {
  private static final FarmerDataProvider INSTANCE = new FarmerDataProvider();

  private final ObservableList<TOFarmer> farmers =
      FXCollections.observableArrayList();

  private final ObservableMap<String, ObservableList<TOCheeseWheel>> cheeseWheels =
      FXCollections.observableHashMap();

  /** Builds the singleton instance and loads the initial farmer roster. */
  private FarmerDataProvider() { refresh(); }

  /** @return shared provider instance */
  public static FarmerDataProvider getInstance() { return INSTANCE; }

  /** @return observable list of farmers that UI lists bind to */
  public ObservableList<TOFarmer> getFarmers() { return farmers; }

  /**
   * @param email farmer email used as the lookup key
   * @return observable list of cheese wheels owned by the requested farmer,
   *     allocating it on demand
   */
  public ObservableList<TOCheeseWheel> getCheeseWheelsForFarmer(String email) {
    return cheeseWheels.computeIfAbsent(email, key -> FXCollections.observableArrayList());
  }

  /** Re-synchronizes farmers and per-farmer cheese wheels with the controllers. */
  public void refresh() {
    List<TOFarmer> latestFarmers =
        CheECSEManagerFeatureSet7Controller.getFarmers();
    farmers.setAll(latestFarmers);

    Set<String> freshestEmails =
        latestFarmers.stream().map(TOFarmer::getEmail).collect(Collectors.toSet());
    cheeseWheels.keySet().removeIf(email -> !freshestEmails.contains(email));

    for (TOFarmer farmer : latestFarmers) {
      List<TOCheeseWheel> latestCheeseWheels =
        Arrays.stream(farmer.getCheeseWheelIDs())
        .map(CheECSEManagerFeatureSet3Controller::getCheeseWheel)
        .collect(Collectors.toList());

      ObservableList<TOCheeseWheel> wheelList =
          cheeseWheels.computeIfAbsent(farmer.getEmail(), key -> FXCollections.observableArrayList());
      wheelList.setAll(latestCheeseWheels);
    }
  }
}
