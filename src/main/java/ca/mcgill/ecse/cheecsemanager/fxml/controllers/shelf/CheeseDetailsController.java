package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet4Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent.ToastType;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfCheeseWheelDataProvider;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfDataProvider;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CheeseDetailsController {
  @FXML private HBox root;
  @FXML private Label cheeseIdLabel;
  @FXML private Label shelfIdLabel;
  // @FXML private ComboBox<String> shelfIdComboBox;
  @FXML private ComboBox<Integer> rowComboBox;
  @FXML private ComboBox<Integer> columnComboBox;
  @FXML private ComboBox<String> ageComboBox;
  @FXML private Label purchaseDateLabel;
  @FXML private ComboBox<Boolean> isSpoiledComboBox;

  private Runnable onClosePressed;
  private TOCheeseWheel cheese;

  private ShelfCheeseWheelDataProvider provider =
      ShelfCheeseWheelDataProvider.getInstance();
  private ShelfDataProvider shelfDataProvider = ShelfDataProvider.getInstance();

  public void init(TOCheeseWheel cheese, Runnable onClosePressed) {
    this.onClosePressed = onClosePressed;
    this.cheese = cheese;

    cheeseIdLabel.setText("Cheese Wheel #" + cheese.getId());
    purchaseDateLabel.setText("Purchase data: " +
                              cheese.getPurchaseDate().toString());

    shelfIdLabel.setText("Shelf ID: " + cheese.getShelfID());
    // shelfIdComboBox.setValue(cheese.getShelfID());
    // shelfIdComboBox.getItems().clear();
    // shelfIdComboBox.getItems().setAll(
    //     CheECSEManagerFeatureSet1Controller.getShelves()
    //         .stream()
    //         .map(s -> s.getShelfID())
    //         .toList());
    //
    // shelfIdComboBox.setOnAction(
    //     e
    //     -> populateLocations(CheECSEManagerFeatureSet1Controller.getShelf(
    //         shelfIdComboBox.getValue())));

    var shelf =
        CheECSEManagerFeatureSet1Controller.getShelf(cheese.getShelfID());

    populateLocations(shelf);

    ageComboBox.setValue(cheese.getMonthsAged());
    ageComboBox.getItems().clear();
    ageComboBox.getItems().setAll("Six", "Twelve", "TwentyFour", "ThirtySix");

    isSpoiledComboBox.setValue(cheese.isIsSpoiled());
    isSpoiledComboBox.getItems().setAll(true, false);
  }

  @FXML
  public void onClosePressed() {
    this.onClosePressed.run();
  }

  @FXML
  public void onSavePressed() {
    var error = CheECSEManagerFeatureSet3Controller.updateCheeseWheel(
        this.cheese.getId(), this.ageComboBox.getValue(),
        this.isSpoiledComboBox.getValue(), this.columnComboBox.getValue(),
        this.rowComboBox.getValue(), this.cheese.getShelfID());

    if (error != null && !error.isEmpty()) {
      root.fireEvent(new ToastEvent(error, ToastType.ERROR));
    } else {
      root.fireEvent(new ToastEvent("Success!", ToastType.SUCCESS));
      this.onClosePressed.run();
    }
  }

  @FXML
  public void onDeletePressed() {
    String error =
        CheECSEManagerFeatureSet4Controller.removeCheeseWheelFromShelf(
            this.cheese.getId());

    if (error.isEmpty()) {
      this.onClosePressed.run();
    } else {
      root.fireEvent(new ToastEvent(error, ToastType.ERROR));
    }
  }

  private void populateLocations(TOShelf shelf) {
    List<String> occupied = new ArrayList<>();
    for (int i = 0; i < shelf.numberOfCheeseWheelIDs(); i++) {
      occupied.add(shelf.getRowNr(i) + "-" + shelf.getColumnNr(i));
    }

    List<Integer> availableRows = new ArrayList<>();
    List<Integer> availableCols = new ArrayList<>();
    for (int r = 1; r <= shelf.getMaxRows(); r++) {
      boolean rowHasFree = false;
      for (int c = 1; c <= shelf.getMaxColumns(); c++) {
        if (!occupied.contains(r + "-" + c)) {
          rowHasFree = true;
          if (!availableCols.contains(c))
            availableCols.add(c);
        }
      }
      if (rowHasFree)
        availableRows.add(r);
    }

    availableRows.sort(Integer::compareTo);
    availableCols.sort(Integer::compareTo);

    rowComboBox.setValue(cheese.getRow());
    rowComboBox.getItems().clear();
    rowComboBox.getItems().setAll(availableRows);

    columnComboBox.setValue(cheese.getColumn());
    columnComboBox.getItems().clear();
    columnComboBox.getItems().setAll(availableCols);
  }
}
