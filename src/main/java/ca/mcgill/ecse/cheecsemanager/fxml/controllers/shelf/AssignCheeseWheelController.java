package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.*;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
  private Set<String> occupiedCells = new HashSet<>();

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
    rowCombo.setOnAction(e -> onRowSelected());
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
      occupiedCells.clear();
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

    // Build occupied cells set
    occupiedCells.clear();
    if (selectedShelf != null) {
      for (int i = 0; i < selectedShelf.numberOfCheeseWheelIDs(); i++) {
        occupiedCells.add(selectedShelf.getRowNr(i) + "-" + selectedShelf.getColumnNr(i));
      }
    }

    populateAvailableRows();
    colCombo.getItems().clear();
    colCombo.setValue(null);
    checkEnableAssign();
  }

  private void populateAvailableRows() {
    rowCombo.getItems().clear();
    rowCombo.setValue(null);

    if (selectedShelf == null) {
      return;
    }

    List<Integer> availableRows = new ArrayList<>();
    for (int r = 1; r <= selectedShelf.getMaxRows(); r++) {
      // Check if this row has at least one free column
      for (int c = 1; c <= selectedShelf.getMaxColumns(); c++) {
        if (!occupiedCells.contains(r + "-" + c)) {
          availableRows.add(r);
          break;
        }
      }
    }

    availableRows.sort(Integer::compareTo);
    rowCombo.setItems(FXCollections.observableArrayList(availableRows));
  }

  private void onRowSelected() {
    Integer selectedRow = rowCombo.getValue();
    colCombo.getItems().clear();
    colCombo.setValue(null);

    if (selectedRow == null || selectedShelf == null) {
      checkEnableAssign();
      return;
    }

    // Only show columns that are available for the selected row
    List<Integer> availableCols = new ArrayList<>();
    for (int c = 1; c <= selectedShelf.getMaxColumns(); c++) {
      if (!occupiedCells.contains(selectedRow + "-" + c)) {
        availableCols.add(c);
      }
    }

    availableCols.sort(Integer::compareTo);
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

    // Double-check the cell is available
    if (occupiedCells.contains(row + "-" + col)) {
      root.fireEvent(new ToastEvent(
          "Cell at row " + row + ", column " + col + " is already occupied.",
          ToastEvent.ToastType.ERROR));
      return;
    }

    String error = CheECSEManagerFeatureSet4Controller.assignCheeseWheelToShelf(
        cheeseId, shelfId, col, row);

    if (!error.isEmpty()) {
      root.fireEvent(new ToastEvent(error, ToastEvent.ToastType.ERROR));
      return;
    }

    root.fireEvent(new ToastEvent("Cheese wheel assigned successfully!",
                                  ToastEvent.ToastType.SUCCESS));
    closePopup();
  }

  private void closePopup() { root.fireEvent(new HidePopupEvent()); }
}
