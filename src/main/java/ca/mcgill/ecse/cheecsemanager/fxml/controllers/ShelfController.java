package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.List;

public class ShelfController {

    @FXML private AnchorPane root;
    @FXML private VBox mainContainer;
    @FXML private Button showPopupBtn;
    @FXML private TableView<TOShelf> shelfTable;
    @FXML private TableColumn<TOShelf, String> idColumn;
    @FXML private TableColumn<TOShelf, Integer> rowsColumn;
    @FXML private TableColumn<TOShelf, Integer> colsColumn;
    @FXML private TableColumn<TOShelf, Integer> numCheeseColumn;
    @FXML private TableColumn<TOShelf, Void> actionColumn;
    @FXML private Label inventoryLabel;

    @FXML
    private void initialize() {
        // Setup column factories
        idColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getShelfID()));
        rowsColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getMaxRows()).asObject());
        colsColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getMaxColumns()).asObject());
        numCheeseColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().numberOfCheeseWheelIDs()).asObject());

        String padding = "-fx-padding: 0 10 0 10;";
        idColumn.setStyle(padding);
        rowsColumn.setStyle(padding);
        colsColumn.setStyle(padding);
        numCheeseColumn.setStyle(padding);
        actionColumn.setStyle(padding);

        shelfTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        shelfTable.getColumns().forEach(tc -> tc.setMinWidth(tc.getPrefWidth()));

        // Setup action buttons in cells
        setupActionButtons();

        // Add shelf button
        showPopupBtn.setOnAction(e -> showAddShelfPopup());

        // Populate table and inventory label
        refreshTable();
    }

    private void setupActionButtons() {
        actionColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<TOShelf, Void> call(TableColumn<TOShelf, Void> param) {
                return new TableCell<>() {
                    private final Button viewBtn = new Button("View");
                    private final Button deleteBtn = new Button("Delete");
                    private final HBox container = new HBox(10, viewBtn, deleteBtn);

                    {
                        viewBtn.setOnAction(e -> {
                            if (getIndex() >= 0 && getIndex() < getTableView().getItems().size()) {
                                TOShelf shelf = getTableView().getItems().get(getIndex());
                                showViewShelfPopup(shelf);
                            }
                        });

                        deleteBtn.setOnAction(e -> {
                            if (getIndex() >= 0 && getIndex() < getTableView().getItems().size()) {
                                TOShelf shelf = getTableView().getItems().get(getIndex());
                                showDeleteConfirmPopupForRow(shelf);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getIndex() >= getTableView().getItems().size()) {
                            setGraphic(null);
                        } else {
                            setGraphic(container);
                        }
                    }
                };
            }
        });
    }

    private void showAddShelfPopup() {
        applyBlur();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/components/Shelf/AddShelfPopUp.fxml"
            ));
            AnchorPane popup = loader.load();

            AnchorPane overlay = new AnchorPane();
            overlay.setPrefSize(root.getWidth(), root.getHeight());
            overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");

            popup.setMaxWidth(popup.getPrefWidth());
            popup.setMaxHeight(popup.getPrefHeight());
            popup.setLayoutX((overlay.getPrefWidth() - popup.getPrefWidth()) / 2);
            popup.setLayoutY((overlay.getPrefHeight() - popup.getPrefHeight()) / 2);

            overlay.getChildren().add(popup);
            root.getChildren().add(overlay);

            AddShelfPopUpController controller = loader.getController();
            controller.setPopupOverlay(overlay);
            controller.setMainController(this);

        } catch (Exception ex) {
            ex.printStackTrace();
            removeBlur();
        }
    }

    private void showDeleteConfirmPopupForRow(TOShelf shelf) {
        applyBlur();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/components/Shelf/ConfirmDelete.fxml"
            ));
            AnchorPane popup = loader.load();

            AnchorPane overlay = new AnchorPane();
            overlay.setPrefSize(root.getWidth(), root.getHeight());
            overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");

            popup.setMaxWidth(popup.getPrefWidth());
            popup.setMaxHeight(popup.getPrefHeight());
            popup.setLayoutX((overlay.getPrefWidth() - popup.getPrefWidth()) / 2);
            popup.setLayoutY((overlay.getPrefHeight() - popup.getPrefHeight()) / 2);

            overlay.getChildren().add(popup);
            root.getChildren().add(overlay);

            ConfirmDeletePopUpController controller = loader.getController();
            controller.setPopupOverlay(overlay);
            controller.setMainController(this);
            controller.setShelfToDelete(shelf);

        } catch (Exception ex) {
            ex.printStackTrace();
            removeBlur();
        }
    }

    private void showViewShelfPopup(TOShelf shelf) {
        applyBlur();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/components/Shelf/ViewShelfPopUp.fxml"
            ));
            AnchorPane popup = loader.load();

            AnchorPane overlay = new AnchorPane();
            overlay.setPrefSize(root.getWidth(), root.getHeight());
            overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");

            popup.setMaxWidth(popup.getPrefWidth());
            popup.setMaxHeight(popup.getPrefHeight());
            popup.setLayoutX((overlay.getPrefWidth() - popup.getPrefWidth()) / 2);
            popup.setLayoutY((overlay.getPrefHeight() - popup.getPrefHeight()) / 2);

            overlay.getChildren().add(popup);
            root.getChildren().add(overlay);

            ViewShelfPopUpController controller = loader.getController();
            controller.setPopupOverlay(overlay);
            controller.setMainController(this);
            controller.setShelfToView(shelf.getShelfID());

        } catch (Exception ex) {
            ex.printStackTrace();
            removeBlur();
        }
    }

    public void removePopup(AnchorPane overlay) {
        root.getChildren().remove(overlay);
        removeBlur();
        refreshTable();
    }

    private void applyBlur() { mainContainer.setEffect(new BoxBlur(5,5,3)); }
    private void removeBlur() { mainContainer.setEffect(null); }

    public void refreshTable() {
        // Update table items
        List<TOShelf> shelves = CheECSEManagerFeatureSet1Controller.getShelves();
        shelfTable.getItems().setAll(shelves);

        // Update inventory label
        int totalCheese = CheECSEManagerFeatureSet3Controller.getCheeseWheels().size();
        inventoryLabel.setText("Current Cheese Inventory: " + totalCheese);

        adjustTableHeight();
    }

    private void adjustTableHeight() {
        int rows = shelfTable.getItems().size();
        double rowHeight = 34;
        double headerHeight = 28;
        double padding = 5;

        shelfTable.setPrefHeight(headerHeight + rows * rowHeight + padding);
        shelfTable.setMinHeight(headerHeight + rows * rowHeight + padding);
        shelfTable.setMaxHeight(headerHeight + rows * rowHeight + padding);
    }

    public TableView<TOShelf> getShelfTable() { return shelfTable; }
}