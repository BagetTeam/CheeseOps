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

public class FarmerDataProvider {
  private static final FarmerDataProvider INSTANCE = new FarmerDataProvider();

  private final ObservableList<TOFarmer> farmers =
      FXCollections.observableArrayList();

  private final ObservableMap<String, ObservableList<TOCheeseWheel>> cheeseWheels =
      FXCollections.observableHashMap();

  private FarmerDataProvider() { refresh(); }

  public static FarmerDataProvider getInstance() { return INSTANCE; }

  public ObservableList<TOFarmer> getFarmers() { return farmers; }

  public ObservableList<TOCheeseWheel> getCheeseWheelsForFarmer(String email) {
    return cheeseWheels.computeIfAbsent(email, key -> FXCollections.observableArrayList());
  }

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