package ca.mcgill.ecse.cheecsemanager.fxml.store;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import java.util.List;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Centralized observable source of shelf data so that UI controllers can react
 * to model mutations without reloading the page.
 */
public class ShelfDataProvider {
  private static final ShelfDataProvider INSTANCE = new ShelfDataProvider();

  private final ObservableList<TOShelf> shelves =
      FXCollections.observableArrayList();

  private final ReadOnlyIntegerWrapper cheeseInventory =
      new ReadOnlyIntegerWrapper(0);

  /** Builds the singleton instance and loads the initial snapshot of shelves. */
  private ShelfDataProvider() { refresh(); }

  /** @return global provider instance shared across the UI */
  public static ShelfDataProvider getInstance() { return INSTANCE; }

  /** @return observable list the UI grid binds to for shelf content */
  public ObservableList<TOShelf> getShelves() { return shelves; }

  /**
   * @return property exposing the total cheese count so labels can reactively
   *     update
   */
  public ReadOnlyIntegerProperty cheeseInventoryProperty() {
    return cheeseInventory.getReadOnlyProperty();
  }

  /** @return cached total number of cheese wheels stored on every shelf */
  public int getCheeseInventory() { return cheeseInventory.get(); }

  /** Reloads shelves and inventory figures from the controllers. */
  public void refresh() {
    List<TOShelf> latestShelves =
        CheECSEManagerFeatureSet1Controller.getShelves();
    shelves.setAll(latestShelves);

    int cheeseCount =
        CheECSEManagerFeatureSet3Controller.getCheeseWheels().size();
    cheeseInventory.set(cheeseCount);
  }
}
