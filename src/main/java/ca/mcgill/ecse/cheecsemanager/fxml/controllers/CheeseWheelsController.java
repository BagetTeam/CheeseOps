package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import java.util.List;
import org.checkerframework.checker.units.qual.C;
import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet4Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Animation.AnimationManager;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Animation.EasingInterpolators;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer.BuyCheesePopupController;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf.AssignCheeseWheelController;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf.CheeseDetailsController;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.CheeseWheelDataProvider;
import ca.mcgill.ecse.cheecsemanager.fxml.store.FarmerDataProvider;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfCheeseWheelDataProvider;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfDataProvider;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * Controller for the Cheese Wheels page.
 * @author Ming Li liu
 * */
public class CheeseWheelsController {
  private ShelfDataProvider shelfDataProvider = ShelfDataProvider.getInstance();

  private final CheeseWheelDataProvider cheeseWheelDataProvider =
      CheeseWheelDataProvider.getInstance();

  @FXML private StackPane root;
  @FXML private Label inventoryLabel;

  @FXML private TableView<TOCheeseWheel> table;
  @FXML private TableColumn<TOCheeseWheel, Integer> idColumn;
  @FXML private TableColumn<TOCheeseWheel, String> monthsAgedColumn;
  @FXML private TableColumn<TOCheeseWheel, Boolean> isSpoiledColumn;
  @FXML private TableColumn<TOCheeseWheel, String> purchaseDateColumn;
  @FXML private TableColumn<TOCheeseWheel, TOCheeseWheel> actionColumn;

  @FXML
  private void initialize() {
    idColumn.setCellValueFactory(
        c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());

    monthsAgedColumn.setCellValueFactory(
        c -> new SimpleStringProperty(c.getValue().getMonthsAged()));

    isSpoiledColumn.setCellValueFactory(
        c -> new SimpleBooleanProperty(c.getValue().getIsSpoiled()).asObject());
    purchaseDateColumn.setCellValueFactory(
        c
        -> new SimpleStringProperty(c.getValue().getPurchaseDate().toString()));

    // Hook the whole row into the action column so each cell knows its cheese
    // wheel
    actionColumn.setCellValueFactory(
        c -> new ReadOnlyObjectWrapper<>(c.getValue()));

    this.setupActionButtons();

    table.getColumns().forEach(tc -> tc.setMinWidth(tc.getPrefWidth()));
    table.setItems(cheeseWheelDataProvider.getCheesewheels());

    inventoryLabel.textProperty().bind(
        cheeseWheelDataProvider.cheeseInventoryProperty().asString().concat(
            " total cheese wheels"));
  }

  @FXML
  private void assignAllCheeseWheels() {
    int rem = BuyCheesePopupController.autoAssignCheeseWheels(CheECSEManagerFeatureSet3Controller.getCheeseWheels());

    FarmerDataProvider.getInstance().refresh();
    CheeseWheelDataProvider.getInstance().refresh();
    ShelfCheeseWheelDataProvider.getInstance().refresh();
    ShelfDataProvider.getInstance().refresh();
    
    root.fireEvent(new ToastEvent("Success!", ToastEvent.ToastType.SUCCESS));
    if (rem > 0) {
      root.fireEvent(new ToastEvent(rem + " could not be auto assigned.", ToastEvent.ToastType.WARNING));
    } 


  }

  private void setupActionButtons() {
    actionColumn.setCellFactory(param -> new TableCell<>() {
      private final StyledButton assignBtn = new StyledButton(
          StyledButton.Variant.PRIMARY, StyledButton.Size.SM, "Assign", null);

      private final StyledButton unassignBtn =
          new StyledButton(StyledButton.Variant.DESTRUCTIVE,
                           StyledButton.Size.SM, "Unassign", null);

      private final StyledButton viewBtn = new StyledButton(
          StyledButton.Variant.MUTED, StyledButton.Size.SM, "View", null);

      private final StyledButton notSpoiledBtn = new StyledButton(
          StyledButton.Variant.PRIMARY, StyledButton.Size.SM, "Unspoil", null);

      private final HBox box = new HBox(10);

      {
        box.setAlignment(Pos.CENTER_RIGHT);
        box.setStyle("-fx-padding: 0 55 0 0;");

        assignBtn.setOnAction(e -> {
          TOCheeseWheel wheel = getTableView().getItems().get(getIndex());
          AssignCheeseWheelController.context.cheeseId = wheel.getId();
          AssignCheeseWheelController.context.shelfId = null;
          AssignCheeseWheelController.context.row = null;
          AssignCheeseWheelController.context.col = null;

          root.fireEvent(new ShowPopupEvent(
              "view/components/Shelf/AssignCheeseWheelPopUp.fxml",
              "Assign Cheese Wheel"));
        });

        viewBtn.setOnAction(e -> {
          TOCheeseWheel wheel = getTableView().getItems().get(getIndex());
          showCheeseDetail(wheel);
        });

        notSpoiledBtn.setOnAction(e -> {
          TOCheeseWheel wheel = getTableView().getItems().get(getIndex());
          CheECSEManagerFeatureSet3Controller.updateCheeseWheel(
              wheel.getId(), wheel.getMonthsAged(), false);
          cheeseWheelDataProvider.refresh();
        });

        unassignBtn.setOnAction(e -> {
          TOCheeseWheel wheel = getTableView().getItems().get(getIndex());
          CheECSEManagerFeatureSet4Controller.removeCheeseWheelFromShelf(
              wheel.getId());
          cheeseWheelDataProvider.refresh();
        });
      }

      @Override
      protected void updateItem(TOCheeseWheel item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
          setGraphic(null);
          return;
        }

        box.getChildren().clear();

        if (item.getIsSpoiled()) {
          box.getChildren().addAll(notSpoiledBtn);
        } else {
          if (item.getShelfID() == null) {
            box.getChildren().add(assignBtn);
          } else {
            box.getChildren().addAll(viewBtn, unassignBtn);
          }
        }

        setGraphic(box);

        setAlignment(Pos.CENTER_RIGHT);
      }
    });
  }

  private void showCheeseDetail(TOCheeseWheel cheese) {
    FXMLLoader loader = new FXMLLoader(CheECSEManagerApplication.getResource(
        "view/components/Shelf/CheeseDetails.fxml"));
    try {
      Node node = loader.load();
      CheeseDetailsController controller = loader.getController();
      controller.init(cheese, () -> {
        AnimationManager.numericBuilder()
            .target(node.translateXProperty())
            .from(0)
            .to(384)
            .durationMillis(500)
            .easing(EasingInterpolators.CUBIC_OUT)
            .onFinished(() -> { this.root.getChildren().remove(node); })
            .play();
      });
      this.root.getChildren().add(node);

      AnimationManager.numericBuilder()
          .target(node.translateXProperty())
          .from(384)
          .to(0)
          .durationMillis(500)
          .easing(EasingInterpolators.CUBIC_OUT)
          .play();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
