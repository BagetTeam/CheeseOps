package ca.mcgill.ecse.cheecsemanager.fxml.store;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Centralized observable source of shelf data so that UI controllers can react
 * to model mutations without reloading the page.
 * @author Ming Li Liu
 */
public class ShelfCheeseWheelDataProvider {
  private String shelfId;

  private final ObservableList<TOCheeseWheel> wheels =
      FXCollections.observableArrayList();

  private static ShelfCheeseWheelDataProvider INSTANCE =
      new ShelfCheeseWheelDataProvider();

  /** Builds the singleton instance and primes it with the current selection. */
  private ShelfCheeseWheelDataProvider() { refresh(); }

  /** @return shared provider instance */
  public static ShelfCheeseWheelDataProvider getInstance() { return INSTANCE; }

  /** @return observable list of non-spoiled cheese wheels on the active shelf */
  public ObservableList<TOCheeseWheel> getWheels() { return wheels; }

  /** Refreshes the shelf-specific wheel list from the controllers. */
  public void refresh() {
    if (this.shelfId == null) {
      this.wheels.setAll(new ArrayList<>());
      return;
    }

    var shelf = CheECSEManagerFeatureSet1Controller.getShelf(this.shelfId);

    List<TOCheeseWheel> latestCheeseWheels =
        Arrays.stream(shelf.getCheeseWheelIDs())
            .map(CheECSEManagerFeatureSet3Controller::getCheeseWheel)
            .filter(c -> !c.isIsSpoiled())
            .toList();

    this.wheels.setAll(latestCheeseWheels);
  }

  /**
   * Updates the shelf whose cheese wheels should be shown and reloads the list.
   * @param shelfId identifier of the selected shelf
   */
  public void setShelf(String shelfId) {
    this.shelfId = shelfId;
    refresh();
  }
}
