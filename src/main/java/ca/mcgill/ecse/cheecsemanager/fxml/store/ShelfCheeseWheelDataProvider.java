package ca.mcgill.ecse.cheecsemanager.fxml.store;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Centralized observable source of shelf data so that UI controllers can react
 * to model mutations without reloading the page.
 */
public class ShelfCheeseWheelDataProvider {
  private final TOShelf shelf;

  private final ObservableList<TOCheeseWheel> wheels =
      FXCollections.observableArrayList();

  private static ShelfCheeseWheelDataProvider INSTANCE;

  public ShelfCheeseWheelDataProvider(TOShelf shelf) {
    INSTANCE = this;
    this.shelf = shelf;
    refresh();
  }

  public static ShelfCheeseWheelDataProvider getInstance() { return INSTANCE; }

  public ObservableList<TOCheeseWheel> getWheels() { return wheels; }

  public void refresh() {
    List<TOCheeseWheel> latestShelves =
        Arrays.stream(shelf.getCheeseWheelIDs())
            .map(CheECSEManagerFeatureSet3Controller::getCheeseWheel)
            .filter(c -> !c.isIsSpoiled())
            .toList();
    wheels.setAll(latestShelves);
  }
}
