
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

  /** Builds the singleton instance and loads the first snapshot of data. */
  private CheeseWheelDataProvider() { refresh(); }

  /** @return global provider instance shared across the UI */
  public static CheeseWheelDataProvider getInstance() { return INSTANCE; }

  /**
   * @return observable list that UI tables bind to for cheese wheel state
   */
  public ObservableList<TOCheeseWheel> getCheesewheels() {
    return cheesewheels;
  }

  /**
   * @return read-only property exposing the total number of cheese wheels
   *     tracked by the provider
   */
  public ReadOnlyIntegerProperty cheeseInventoryProperty() {
    return cheeseInventory.getReadOnlyProperty();
  }

  /** @return cached total cheese wheel count */
  public int getCheeseInventory() { return cheeseInventory.get(); }

  /** Reloads the cheese wheel list and inventory count from the controllers. */
  public void refresh() {
    List<TOCheeseWheel> latestShelves =
        CheECSEManagerFeatureSet3Controller.getCheeseWheels();
    cheesewheels.setAll(latestShelves);

    int cheeseCount =
        CheECSEManagerFeatureSet3Controller.getCheeseWheels().size();
    cheeseInventory.set(cheeseCount);
  }
}
