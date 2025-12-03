package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet4Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent.ToastType;
import ca.mcgill.ecse.cheecsemanager.fxml.store.CheeseWheelDataProvider;
import ca.mcgill.ecse.cheecsemanager.fxml.store.OrdersProvider;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfCheeseWheelDataProvider;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfDataProvider;
import ca.mcgill.ecse.cheecsemanager.fxml.store.WholesaleCompanyDataProvider;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Drawer controller that edits an individual cheese wheel's information. */
public class CheeseDetailsController {
  private final ShelfCheeseWheelDataProvider dataProvider =
      ShelfCheeseWheelDataProvider.getInstance();

  private final ShelfDataProvider shelfDataProvider =
      ShelfDataProvider.getInstance();

  private final CheeseWheelDataProvider cheeseWheelDataProvider =
      CheeseWheelDataProvider.getInstance();

  @FXML private HBox root;
  @FXML private VBox mainContainer;
  @FXML private Label cheeseIdLabel;
  @FXML private ComboBox<String> shelfIdComboBox;
  @FXML private ComboBox<Integer> rowComboBox;
  @FXML private ComboBox<Integer> columnComboBox;
  @FXML private ComboBox<String> ageComboBox;
  @FXML private Label purchaseDateLabel;
  @FXML private ComboBox<Boolean> isSpoiledComboBox;
  @FXML private Label errorLabel;

  private Runnable onClosePressed;

  // private int cheeseId;
  private TOCheeseWheel cheese;

  /**
   * Loads the cheese wheel info and prepares bindings for the detail drawer.
   * @param cheeseId wheel identifier to inspect
   * @param onClosePressed callback when the drawer should close
   */
  public void init(int cheeseId, Runnable onClosePressed) {
    this.onClosePressed = onClosePressed;
    // this.cheeseId = cheeseId;

    this.cheese = CheECSEManagerFeatureSet3Controller.getCheeseWheel(cheeseId);

    cheeseIdLabel.setText("Cheese Wheel #" + cheese.getId());
    purchaseDateLabel.setText("Purchase data: " +
                              cheese.getPurchaseDate().toString());

    shelfIdComboBox.setValue(cheese.getShelfID());
    shelfIdComboBox.getItems().clear();
    shelfIdComboBox.getItems().setAll(
        CheECSEManagerFeatureSet1Controller.getShelves()
            .stream()
            .map(s -> s.getShelfID())
            .toList());

    shelfIdComboBox.setOnAction(
        e
        -> populateLocations(CheECSEManagerFeatureSet1Controller.getShelf(
            shelfIdComboBox.getValue())));

    var shelf =
        CheECSEManagerFeatureSet1Controller.getShelf(cheese.getShelfID());

    populateLocations(shelf);

    ageComboBox.setValue(cheese.getMonthsAged());
    ageComboBox.getItems().clear();
    ageComboBox.getItems().setAll("Six", "Twelve", "TwentyFour", "ThirtySix");

    isSpoiledComboBox.setValue(cheese.isIsSpoiled());
    isSpoiledComboBox.getItems().setAll(true, false);

    root.setOnMouseClicked(e -> { this.onClosePressed.run(); });
    mainContainer.setOnMouseClicked(e -> { e.consume(); });

    root.sceneProperty().addListener((obs, oldScene, newScene) -> {
      if (newScene == null) {
        root.setOnMouseClicked(null);
        mainContainer.setOnMouseClicked(null);
      }
    });
  }

  /** Handles the close button tap and runs the injected callback. */
  @FXML
  public void onClosePressed() {
    this.onClosePressed.run();
  }

  /** Persists location, aging, and spoilage changes through the controllers. */
  @FXML
  public void onSavePressed() {
    String error;

    if (this.columnComboBox.getValue() == null ||
        this.rowComboBox.getValue() == null ||
        this.shelfIdComboBox.getValue() == null) {
      showError("Please select a location.");
      return;
    }

    if (this.columnComboBox.getValue() != cheese.getColumn() ||
        this.rowComboBox.getValue() != cheese.getRow() ||
        !this.shelfIdComboBox.getValue().equals(cheese.getShelfID())) {
      error = CheECSEManagerFeatureSet4Controller.assignCheeseWheelToShelf(
          this.cheese.getId(), this.shelfIdComboBox.getValue(),
          this.columnComboBox.getValue(), this.rowComboBox.getValue());

      if (error != null && !error.isEmpty()) {
        showError(error);
        return;
      }
    }

    error = CheECSEManagerFeatureSet3Controller.updateCheeseWheel(
        this.cheese.getId(), this.ageComboBox.getValue(),
        this.isSpoiledComboBox.getValue());

    if (error != null && !error.isEmpty()) {
      showError(error);
    } else {
      root.fireEvent(new ToastEvent("Success!", ToastType.SUCCESS));

      shelfDataProvider.refresh();
      dataProvider.refresh();
      cheeseWheelDataProvider.refresh();
      WholesaleCompanyDataProvider.getInstance().refresh();
      OrdersProvider.getInstance().refresh();
      this.onClosePressed.run();
    }
  }

  /** Removes the cheese wheel from its shelf entirely. */
  @FXML
  public void onDeletePressed() {
    String error =
        CheECSEManagerFeatureSet4Controller.removeCheeseWheelFromShelf(
            this.cheese.getId());

    if (error == null || error.isEmpty()) {
      shelfDataProvider.refresh();
      dataProvider.refresh();
      cheeseWheelDataProvider.refresh();
      WholesaleCompanyDataProvider.getInstance().refresh();
      OrdersProvider.getInstance().refresh();
      this.onClosePressed.run();
    } else {
      showError(error);
    }
  }

  /** Displays an error beneath the form fields. */
  private void showError(String error) {
    this.errorLabel.setText(error);
    this.errorLabel.setVisible(true);
    this.errorLabel.setManaged(true);
  }

  /** Populates row/column combos based on the selected shelf's free slots. */
  private void populateLocations(TOShelf shelf) {
    if (shelf == null) {
      return;
    }

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
