package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.*;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent.ToastType;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfCheeseWheelDataProvider;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfDataProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class AssignCheeseWheelController {
  private ShelfCheeseWheelDataProvider dataProvider =
      ShelfCheeseWheelDataProvider.getInstance();

  private ShelfDataProvider shelfDataProvider = ShelfDataProvider.getInstance();

  @FXML private ComboBox<String> cheeseCombo;
  @FXML private ComboBox<Integer> rowCombo;
  @FXML private ComboBox<Integer> colCombo;
  @FXML private StyledButton assignButton;
  @FXML private VBox root;

  private TOShelf shelf = ViewShelfController.shelfToView;

  @FXML
  public void initialize() {
    List<String> unassigned =
        CheECSEManagerFeatureSet3Controller.getCheeseWheels()
            .stream()
            .filter(c -> c.getShelfID() == null)
            .map(c -> c.getId() + " - " + c.getMonthsAged() + " months")
            .collect(Collectors.toList());
    cheeseCombo.setItems(FXCollections.observableArrayList(unassigned));

    cheeseCombo.setOnAction(e -> checkEnableAssign());
    rowCombo.setOnAction(e -> checkEnableAssign());
    colCombo.setOnAction(e -> checkEnableAssign());
    assignButton.setOnAction(e -> assignCheeseWheel());

    this.populateRowsCols();
  }

  private void populateRowsCols() {
    rowCombo.getItems().clear();
    colCombo.getItems().clear();

    String shelfID = shelf.getShelfID();
    if (shelfID == null)
      return;

    TOShelf shelf = CheECSEManagerFeatureSet1Controller.getShelves()
                        .stream()
                        .filter(s -> s.getShelfID().equals(shelfID))
                        .findFirst()
                        .orElse(null);
    if (shelf == null)
      return;

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

    rowCombo.setItems(FXCollections.observableArrayList(availableRows));
    colCombo.setItems(FXCollections.observableArrayList(availableCols));

    checkEnableAssign();
  }

  private void checkEnableAssign() {
    assignButton.setDisable(cheeseCombo.getValue() == null ||
                            rowCombo.getValue() == null ||
                            colCombo.getValue() == null);
  }

  @FXML
  private void assignCheeseWheel() {
    String selected = cheeseCombo.getValue();
    if (selected == null)
      return;

    int cheeseId = Integer.parseInt(selected.split(" - ")[0].trim());
    Integer row = rowCombo.getValue();
    Integer col = colCombo.getValue();

    String error = CheECSEManagerFeatureSet4Controller.assignCheeseWheelToShelf(
        cheeseId, shelf.getShelfID(), col, row);

    if (!error.isEmpty()) {
      root.fireEvent(new ToastEvent(error, ToastType.ERROR));
      return;
    }

    dataProvider.refresh();
    shelfDataProvider.refresh();
    closePopup();
  }

  private void closePopup() { root.fireEvent(new HidePopupEvent()); }
}
