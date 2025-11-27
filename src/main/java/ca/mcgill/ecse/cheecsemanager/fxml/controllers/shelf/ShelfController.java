package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfDataProvider;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ShelfController {

  private final ShelfDataProvider shelfDataProvider =
      ShelfDataProvider.getInstance();

  public static String selectedShelfId = null;

  @FXML private BorderPane root;

  @FXML private TableView<TOShelf> shelfTable;
  @FXML private TableColumn<TOShelf, String> idColumn;
  @FXML private TableColumn<TOShelf, Integer> rowsColumn;
  @FXML private TableColumn<TOShelf, Integer> colsColumn;
  @FXML private TableColumn<TOShelf, Integer> numCheeseColumn;
  @FXML private TableColumn<TOShelf, Void> actionColumn;

  @FXML private StyledButton openPopupBtn;
  @FXML private Label inventoryLabel;

  @FXML
  private void initialize() {
    idColumn.setCellValueFactory(
        c -> new SimpleStringProperty(c.getValue().getShelfID()));
    rowsColumn.setCellValueFactory(
        c -> new SimpleIntegerProperty(c.getValue().getMaxRows()).asObject());
    colsColumn.setCellValueFactory(
        c
        -> new SimpleIntegerProperty(c.getValue().getMaxColumns()).asObject());
    numCheeseColumn.setCellValueFactory(
        c
        -> new SimpleIntegerProperty(c.getValue().numberOfCheeseWheelIDs())
               .asObject());

    shelfTable.getColumns().forEach(tc -> tc.setMinWidth(tc.getPrefWidth()));
    shelfTable.setItems(shelfDataProvider.getShelves());

    setupActionButtons();
    bindInventoryLabel();

    openPopupBtn.setOnAction(e -> {
      this.root.fireEvent(new ShowPopupEvent(
          "view/components/Shelf/AddShelfPopUp.fxml", "Add Shelf"));
    });

    refreshTable();
  }

  private void setupActionButtons() {
    actionColumn.setCellFactory(param -> new TableCell<>() {
      private final StyledButton viewBtn = new StyledButton(
          StyledButton.Variant.MUTED, StyledButton.Size.SM, "View", null);
      private final StyledButton deleteBtn =
          new StyledButton(StyledButton.Variant.DESTRUCTIVE,
                           StyledButton.Size.SM, "Delete", null);

      private final HBox box = new HBox(10, viewBtn, deleteBtn);

      {
        box.setAlignment(Pos.CENTER);

        viewBtn.setOnAction(e -> {
          TOShelf shelf = getTableView().getItems().get(getIndex());
          showViewShelfPopup(shelf);
        });
        deleteBtn.setOnAction(e -> {
          TOShelf shelf = getTableView().getItems().get(getIndex());
          showDeleteConfirmPopupForRow(shelf);
        });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : box);
        setAlignment(Pos.CENTER);
      }
    });
  }

  private void showAssignCheeseWheelPopup() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(
          "/ca/mcgill/ecse/cheecsemanager/view/components/Shelf/"
          + "AssignCheeseWheelPopUp.fxml"));
      Node popup = loader.load();
      AnchorPane overlay = buildOverlay(popup);

      AssignCheeseWheelController controller = loader.getController();
      controller.setOverlay(overlay);
      controller.setMainController(this); // set reference to this controller

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void showDeleteConfirmPopupForRow(TOShelf shelf) {
    try {
      selectedShelfId = shelf.getShelfID();

      this.root.fireEvent(new ShowPopupEvent(
          "view/components/Shelf/ConfirmDelete.fxml", "Confirm delete"));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void showViewShelfPopup(TOShelf shelf) {
    // applyBlur();
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/ca/mcgill/ecse/cheecsemanager/view/"
                                 + "components/Shelf/ViewShelfPopUp.fxml"));
      Node popup = loader.load();
      AnchorPane overlay = buildOverlay(popup);

      ViewShelfPopUpController controller = loader.getController();
      controller.setMainController(this);
      controller.setPopupOverlay(overlay);
      controller.setShelfToView(shelf.getShelfID());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public AnchorPane buildOverlay(Node popup) {
    AnchorPane overlay = new AnchorPane();
    overlay.setPrefSize(root.getWidth(), root.getHeight());
    overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");

    popup.setLayoutX((overlay.getPrefWidth() - popup.prefWidth(-1)) / 2);
    popup.setLayoutY((overlay.getPrefHeight() - popup.prefHeight(-1)) / 2);

    overlay.getChildren().add(popup);
    root.getChildren().add(overlay);

    return overlay;
  }

  public void refreshTable() { shelfDataProvider.refresh(); }

  public BorderPane getRoot() { return root; }

  private void bindInventoryLabel() {
    inventoryLabel.textProperty().bind(Bindings.createStringBinding(
        ()
            -> "Current Cheese Inventory: " +
                   shelfDataProvider.getCheeseInventory(),
        shelfDataProvider.cheeseInventoryProperty()));
  }
}
