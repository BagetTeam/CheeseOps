package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;

public class AssignCheeseWheelController {

    @FXML private ComboBox<String> cheeseCombo;
    @FXML private ComboBox<String> shelfCombo;
    @FXML private ComboBox<Integer> rowCombo;
    @FXML private ComboBox<Integer> colCombo;
    @FXML private StyledButton assignButton;
    @FXML private StyledButton cancelButton;

    // References for closing the popup
    private ShelfController mainController;
    private ViewShelfPopUpController parentPopup;
    private AnchorPane overlay;

    public void setMainController(ShelfController controller) {
        this.mainController = controller;
    }

    public void setParentPopup(ViewShelfPopUpController popup) {
        this.parentPopup = popup;
    }

    public void setOverlay(AnchorPane overlay) {
        this.overlay = overlay;
    }

    @FXML
    public void initialize() {
        List<String> unassigned = CheECSEManagerFeatureSet3Controller.getCheeseWheels().stream()
                .filter(c -> c.getShelfID() == null)
                .map(c -> "ID: " + c.getId() + " - " + c.getMonthsAged() + " months")
                .collect(Collectors.toList());
        cheeseCombo.setItems(FXCollections.observableArrayList(unassigned));

        List<String> shelves = CheECSEManagerFeatureSet1Controller.getShelves().stream()
                .map(TOShelf::getShelfID)
                .collect(Collectors.toList());
        shelfCombo.setItems(FXCollections.observableArrayList(shelves));

        shelfCombo.setOnAction(e -> populateRowsCols());
        cheeseCombo.setOnAction(e -> checkEnableAssign());
        rowCombo.setOnAction(e -> checkEnableAssign());
        colCombo.setOnAction(e -> checkEnableAssign());

        assignButton.setDisable(true);

        cancelButton.setOnAction(e -> closePopup());
    }

    private void populateRowsCols() {
        rowCombo.getItems().clear();
        colCombo.getItems().clear();

        String shelfID = shelfCombo.getValue();
        if (shelfID == null) return;

        TOShelf shelf = CheECSEManagerFeatureSet1Controller.getShelves().stream()
                .filter(s -> s.getShelfID().equals(shelfID))
                .findFirst()
                .orElse(null);
        if (shelf == null) return;

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
                    if (!availableCols.contains(c)) availableCols.add(c);
                }
            }
            if (rowHasFree) availableRows.add(r);
        }

        availableRows.sort(Integer::compareTo);
        availableCols.sort(Integer::compareTo);

        rowCombo.setItems(FXCollections.observableArrayList(availableRows));
        colCombo.setItems(FXCollections.observableArrayList(availableCols));

        checkEnableAssign();
    }

    private void checkEnableAssign() {
        assignButton.setDisable(
                cheeseCombo.getValue() == null ||
                        shelfCombo.getValue() == null ||
                        rowCombo.getValue() == null ||
                        colCombo.getValue() == null
        );
    }

    @FXML
    private void assignCheeseWheel() {
        String selected = cheeseCombo.getValue();
        if (selected == null) return;

        int cheeseId = Integer.parseInt(selected.split(" - ")[0].trim());
        String shelf = shelfCombo.getValue();
        Integer row = rowCombo.getValue();
        Integer col = colCombo.getValue();

        String error = CheECSEManagerFeatureSet4Controller.assignCheeseWheelToShelf(
                cheeseId, shelf, col, row
        );

        if (!error.isEmpty()) {
            System.out.println("Error: " + error);
            return;
        }

        closePopup();

        if (parentPopup != null) {
            parentPopup.refreshShelfGrid();
        } else if (mainController != null) {
            mainController.refreshTable();
        }
    }

    private void closePopup() {
        if (overlay != null) {
            if (mainController != null) {
                mainController.removePopup(overlay);
            } else if (parentPopup != null) {
                parentPopup.getMainController().removePopup(overlay);
            }
        }
    }
}