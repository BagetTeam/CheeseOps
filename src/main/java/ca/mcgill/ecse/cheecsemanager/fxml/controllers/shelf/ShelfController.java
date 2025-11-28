package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Animation.AnimationManager;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Animation.EasingInterpolators;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfDataProvider;
import java.util.Arrays;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ShelfController {

  private final ShelfDataProvider shelfDataProvider =
      ShelfDataProvider.getInstance();

  public static String selectedShelfId = null;

  @FXML private StackPane root;

  @FXML private TableView<TOShelf> shelfTable;
  @FXML private TableColumn<TOShelf, String> idColumn;
  @FXML private TableColumn<TOShelf, Integer> rowsColumn;
  @FXML private TableColumn<TOShelf, Integer> colsColumn;
  @FXML private TableColumn<TOShelf, Long> numCheeseColumn;
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
        -> new SimpleLongProperty(
               Arrays.stream(c.getValue().getCheeseWheelIDs())
                   .filter(cw
                           -> !CheECSEManagerFeatureSet3Controller
                                   .getCheeseWheel(cw)
                                   .isIsSpoiled())
                   .count())
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

  private void showDeleteConfirmPopupForRow(TOShelf shelf) {
    try {
      selectedShelfId = shelf.getShelfID();

      this.root.fireEvent(new ShowPopupEvent(
          "view/components/Shelf/ConfirmDelete.fxml", "Confirm Delete"));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void showViewShelfPopup(TOShelf shelf) {
    FXMLLoader loader = new FXMLLoader(CheECSEManagerApplication.getResource(
        "view/components/Shelf/ViewShelf.fxml"));
    try {
      StackPane node = loader.load();

      ViewShelfController controller = loader.getController();

      var width = root.getWidth();

      controller.init(shelf, () -> {
        AnimationManager.numericBuilder()
            .target(node.translateXProperty())
            .from(0)
            .to(width)
            .durationMillis(500)
            .easing(EasingInterpolators.CUBIC_OUT)
            .onFinished(() -> { this.root.getChildren().remove(node); })
            .play();
      });

      this.root.getChildren().add(node);

      AnimationManager.numericBuilder()
          .target(node.translateXProperty())
          .from(width)
          .to(0)
          .durationMillis(500)
          .easing(EasingInterpolators.CUBIC_OUT)
          .play();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void refreshTable() { shelfDataProvider.refresh(); }

  public StackPane getRoot() { return root; }

  private void bindInventoryLabel() {
    inventoryLabel.textProperty().bind(Bindings.createStringBinding(
        ()
            -> "Current Cheese Inventory: " +
                   shelfDataProvider.getCheeseInventory(),
        shelfDataProvider.cheeseInventoryProperty()));
  }
}
