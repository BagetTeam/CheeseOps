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

  private ShelfDataProvider() { refresh(); }

  public static ShelfDataProvider getInstance() { return INSTANCE; }

  public ObservableList<TOShelf> getShelves() { return shelves; }

  public ReadOnlyIntegerProperty cheeseInventoryProperty() {
    return cheeseInventory.getReadOnlyProperty();
  }

  public int getCheeseInventory() { return cheeseInventory.get(); }

  public void refresh() {
    List<TOShelf> latestShelves =
        CheECSEManagerFeatureSet1Controller.getShelves();
    shelves.setAll(latestShelves);

    int cheeseCount = CheECSEManagerFeatureSet3Controller.getCheeseWheels()
                          .size();
    cheeseInventory.set(cheeseCount);
  }
}
