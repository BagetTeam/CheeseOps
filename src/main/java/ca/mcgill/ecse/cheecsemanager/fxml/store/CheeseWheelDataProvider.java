
package ca.mcgill.ecse.cheecsemanager.fxml.store;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import java.util.List;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Centralized observable source of shelf data so that UI controllers can react
 * to model mutations without reloading the page.
 */
public class CheeseWheelDataProvider {
  private static final CheeseWheelDataProvider INSTANCE =
      new CheeseWheelDataProvider();

  private final ObservableList<TOCheeseWheel> cheesewheels =
      FXCollections.observableArrayList();

  private final ReadOnlyIntegerWrapper cheeseInventory =
      new ReadOnlyIntegerWrapper(0);

  private CheeseWheelDataProvider() { refresh(); }

  public static CheeseWheelDataProvider getInstance() { return INSTANCE; }

  public ObservableList<TOCheeseWheel> getCheesewheels() {
    return cheesewheels;
  }

  public ReadOnlyIntegerProperty cheeseInventoryProperty() {
    return cheeseInventory.getReadOnlyProperty();
  }

  public int getCheeseInventory() { return cheeseInventory.get(); }

  public void refresh() {
    List<TOCheeseWheel> latestShelves =
        CheECSEManagerFeatureSet3Controller.getCheeseWheels();
    cheesewheels.setAll(latestShelves);

    int cheeseCount =
        CheECSEManagerFeatureSet3Controller.getCheeseWheels().size();
    cheeseInventory.set(cheeseCount);
  }
}
