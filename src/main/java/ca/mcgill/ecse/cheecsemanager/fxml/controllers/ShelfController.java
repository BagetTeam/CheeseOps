package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet2Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;

import ca.mcgill.ecse.cheecsemanager.fxml.controllers.AddShelfPopUpController;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.ConfirmDeletePopUpController;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.ViewShelfPopUpController;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

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

/**
 * ShelfController — handles shelves and popup UI with background blur.
 */
public class ShelfController {

    @FXML private AnchorPane root;            // whole scene root
    @FXML private AnchorPane contentRoot;     // ONLY this blurs (popup stays sharp)
    @FXML private VBox mainContainer;

    @FXML private TableView<TOShelf> shelfTable;
    @FXML private TableColumn<TOShelf, String> idColumn;
    @FXML private TableColumn<TOShelf, Integer> rowsColumn;
    @FXML private TableColumn<TOShelf, Integer> colsColumn;
    @FXML private TableColumn<TOShelf, Integer> numCheeseColumn;
    @FXML private TableColumn<TOShelf, Void> actionColumn;

    @FXML private StyledButton showPopupBtn;
    @FXML private StyledButton backBtn;

    @FXML private Label inventoryLabel;

    @FXML
    private void initialize() {

        // Column bindings
        idColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getShelfID()));
        rowsColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getMaxRows()).asObject());
        colsColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getMaxColumns()).asObject());
        numCheeseColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().numberOfCheeseWheelIDs()).asObject());

        // Padding style
        String pad = "-fx-padding: 0 10 0 10;";
        idColumn.setStyle(pad);
        rowsColumn.setStyle(pad);
        colsColumn.setStyle(pad);
        numCheeseColumn.setStyle(pad);
        actionColumn.setStyle(pad);

        // Fit columns
        shelfTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        shelfTable.getColumns().forEach(tc -> tc.setMinWidth(tc.getPrefWidth()));

        // Row action buttons
        setupActionButtons();

        // Popup button
        showPopupBtn.setOnAction(e -> showAddShelfPopup());

        // Back button
        backBtn.setOnAction(e -> handleBack());

        // Load inventory + table
        refreshTable();
    }

    private void setupActionButtons() {
        actionColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<TOShelf, Void> call(TableColumn<TOShelf, Void> param) {
                return new TableCell<>() {

                    private final StyledButton viewBtn = new StyledButton(
                            StyledButton.Variant.PRIMARY,
                            StyledButton.Size.SM,
                            "View",
                            null
                    );

                    private final StyledButton deleteBtn = new StyledButton(
                            StyledButton.Variant.DESTRUCTIVE,
                            StyledButton.Size.SM,
                            "Delete",
                            null
                    );

                    private final HBox box = new HBox(10, viewBtn, deleteBtn);

                    {
                        viewBtn.setOnAction(e -> {
                            int i = getIndex();
                            if (i >= 0 && i < getTableView().getItems().size()) {
                                showViewShelfPopup(getTableView().getItems().get(i));
                            }
                        });

                        deleteBtn.setOnAction(e -> {
                            int i = getIndex();
                            if (i >= 0 && i < getTableView().getItems().size()) {
                                showDeleteConfirmPopupForRow(getTableView().getItems().get(i));
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : box);
                    }
                };
            }
        });
    }

    // ===============================================================
    //                       POPUP HELPERS
    // ===============================================================

    private void showAddShelfPopup() {
        applyBlur();
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/components/Shelf/AddShelfPopUp.fxml"
            ));
            AnchorPane popup = loader.load();

            AnchorPane overlay = buildOverlay(popup);

            AddShelfPopUpController controller = loader.getController();
            controller.setMainController(this);
            controller.setPopupOverlay(overlay);

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

            AnchorPane overlay = buildOverlay(popup);

            ConfirmDeletePopUpController controller = loader.getController();
            controller.setMainController(this);
            controller.setPopupOverlay(overlay);
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

            AnchorPane overlay = buildOverlay(popup);

            ViewShelfPopUpController controller = loader.getController();
            controller.setMainController(this);
            controller.setPopupOverlay(overlay);
            controller.setShelfToView(shelf.getShelfID());

        } catch (Exception ex) {
            ex.printStackTrace();
            removeBlur();
        }
    }

    // ===============================================================
    //                  OVERLAY + CENTERING LOGIC
    // ===============================================================

    private AnchorPane buildOverlay(AnchorPane popup) {

        AnchorPane overlay = new AnchorPane();
        overlay.setPrefSize(root.getWidth(), root.getHeight());
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");

        // 🔥 your original exact centering logic preserved
        popup.setMaxWidth(popup.getPrefWidth());
        popup.setMaxHeight(popup.getPrefHeight());
        popup.setLayoutX((overlay.getPrefWidth() - popup.getPrefWidth()) / 2);
        popup.setLayoutY((overlay.getPrefHeight() - popup.getPrefHeight()) / 2);

        overlay.getChildren().add(popup);
        root.getChildren().add(overlay);

        return overlay;
    }

    public void removePopup(AnchorPane overlay) {
        root.getChildren().remove(overlay);
        removeBlur();
        refreshTable();
    }

    // ===============================================================
    //                          BLUR
    // ===============================================================

    private void applyBlur() {
        if (contentRoot != null) {
            contentRoot.setEffect(new BoxBlur(5, 5, 3));
        } else if (mainContainer != null) {
            mainContainer.setEffect(new BoxBlur(5, 5, 3));
        }
    }

    private void removeBlur() {
        if (contentRoot != null) {
            contentRoot.setEffect(null);
        } else if (mainContainer != null) {
            mainContainer.setEffect(null);
        }
    }

    // ===============================================================
    //                     TABLE + INVENTORY
    // ===============================================================

    public void refreshTable() {
        List<TOShelf> shelves = CheECSEManagerFeatureSet1Controller.getShelves();
        shelfTable.getItems().setAll(shelves);

        int cheese = CheECSEManagerFeatureSet3Controller.getCheeseWheels().size();
        inventoryLabel.setText("Current Cheese Inventory: " + cheese);

        adjustTableHeight();
    }

    private void adjustTableHeight() {
        int rows = shelfTable.getItems().size();
        double height = 28 + rows * 35 + 5;

        shelfTable.setPrefHeight(height);
        shelfTable.setMinHeight(height);
        shelfTable.setMaxHeight(height);
    }

    private void handleBack() {
        System.out.println("Back button pressed");
    }
}