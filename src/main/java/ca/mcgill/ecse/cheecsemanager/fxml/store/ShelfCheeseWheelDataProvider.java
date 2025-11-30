package ca.mcgill.ecse.cheecsemanager.fxml.store;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Centralized observable source of shelf data so that UI controllers can react
 * to model mutations without reloading the page.
 */
public class ShelfCheeseWheelDataProvider {
  private TOShelf shelf;

  private final ObservableList<TOCheeseWheel> wheels =
      FXCollections.observableArrayList();

  private static ShelfCheeseWheelDataProvider INSTANCE =
      new ShelfCheeseWheelDataProvider();

  private ShelfCheeseWheelDataProvider() { refresh(); }

  public static ShelfCheeseWheelDataProvider getInstance() { return INSTANCE; }

  public ObservableList<TOCheeseWheel> getWheels() { return wheels; }

  public void refresh() {
    if (this.shelf == null) {
      this.wheels.setAll(new ArrayList<>());
      return;
    }

    List<TOCheeseWheel> latestCheeseWheels =
        Arrays.stream(this.shelf.getCheeseWheelIDs())
            .map(CheECSEManagerFeatureSet3Controller::getCheeseWheel)
            .filter(c -> !c.isIsSpoiled())
            .toList();

    this.wheels.setAll(latestCheeseWheels);
  }

  public void setShelf(TOShelf shelf) {
    this.shelf = shelf;
    refresh();
  }
}
