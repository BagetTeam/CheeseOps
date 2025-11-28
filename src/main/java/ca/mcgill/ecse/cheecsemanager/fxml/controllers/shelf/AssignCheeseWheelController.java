package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.*;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class AssignCheeseWheelController {

  @FXML private ComboBox<String> cheeseCombo;
  @FXML private ComboBox<String> shelfCombo;
  @FXML private ComboBox<Integer> rowCombo;
  @FXML private ComboBox<Integer> colCombo;
  @FXML private StyledButton assignButton;
  @FXML private VBox root;
  @FXML private VBox shelfSelectionBox;

  private TOShelf selectedShelf;

  @FXML
  public void initialize() {
    // Populate unassigned cheese wheels
    List<String> unassigned =
        CheECSEManagerFeatureSet3Controller.getCheeseWheels()
            .stream()
            .filter(c -> c.getShelfID() == null)
            .map(
                c -> "ID: " + c.getId() + " - " + c.getMonthsAged() + " months")
            .collect(Collectors.toList());
    cheeseCombo.setItems(FXCollections.observableArrayList(unassigned));

    // Populate shelves with available space
    populateShelves();

    cheeseCombo.setOnAction(e -> checkEnableAssign());
    shelfCombo.setOnAction(e -> onShelfSelected());
    rowCombo.setOnAction(e -> checkEnableAssign());
    colCombo.setOnAction(e -> checkEnableAssign());
    assignButton.setOnAction(e -> assignCheeseWheel());

    // Pre-select shelf if coming from shelf view
    TOShelf preselectedShelf = ViewShelfController.shelfToView;
    if (preselectedShelf != null) {
      shelfCombo.setValue(preselectedShelf.getShelfID());
      onShelfSelected();
    }
  }

  private void populateShelves() {
    List<String> shelves =
        CheECSEManagerFeatureSet1Controller.getShelves()
            .stream()
            .filter(s -> hasAvailableSpace(s))
            .map(s -> s.getShelfID())
            .collect(Collectors.toList());
    shelfCombo.setItems(FXCollections.observableArrayList(shelves));
  }

  private boolean hasAvailableSpace(TOShelf shelf) {
    int totalSlots = shelf.getMaxRows() * shelf.getMaxColumns();
    return shelf.numberOfCheeseWheelIDs() < totalSlots;
  }

  private void onShelfSelected() {
    String selectedShelfId = shelfCombo.getValue();
    if (selectedShelfId == null) {
      selectedShelf = null;
      rowCombo.getItems().clear();
      colCombo.getItems().clear();
      checkEnableAssign();
      return;
    }

    selectedShelf = CheECSEManagerFeatureSet1Controller.getShelves()
                        .stream()
                        .filter(s -> s.getShelfID().equals(selectedShelfId))
                        .findFirst()
                        .orElse(null);

    populateRowsCols();
  }

  private void populateRowsCols() {
    rowCombo.getItems().clear();
    colCombo.getItems().clear();

    if (selectedShelf == null) {
      checkEnableAssign();
      return;
    }

    List<String> occupied = new ArrayList<>();
    for (int i = 0; i < selectedShelf.numberOfCheeseWheelIDs(); i++) {
      occupied.add(selectedShelf.getRowNr(i) + "-" + selectedShelf.getColumnNr(i));
    }

    List<Integer> availableRows = new ArrayList<>();
    List<Integer> availableCols = new ArrayList<>();
    for (int r = 1; r <= selectedShelf.getMaxRows(); r++) {
      boolean rowHasFree = false;
      for (int c = 1; c <= selectedShelf.getMaxColumns(); c++) {
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
                            shelfCombo.getValue() == null ||
                            rowCombo.getValue() == null ||
                            colCombo.getValue() == null);
  }

  @FXML
  private void assignCheeseWheel() {
    String selected = cheeseCombo.getValue();
    if (selected == null)
      return;

    // Parse cheese ID - format is "ID: X - Y months"
    String idPart = selected.split(" - ")[0].replace("ID: ", "").trim();
    int cheeseId = Integer.parseInt(idPart);
    Integer row = rowCombo.getValue();
    Integer col = colCombo.getValue();
    String shelfId = shelfCombo.getValue();

    String error = CheECSEManagerFeatureSet4Controller.assignCheeseWheelToShelf(
        cheeseId, shelfId, col, row);

    if (!error.isEmpty()) {
      System.out.println("Error: " + error);
      return;
    }

    closePopup();
  }

  private void closePopup() { root.fireEvent(new HidePopupEvent()); }
}
