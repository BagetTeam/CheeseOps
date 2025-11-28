package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet4Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.CheeseWheelDataProvider;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class CheeseWheelsController {
  private final CheeseWheelDataProvider cheeseWheelDataProvider =
      CheeseWheelDataProvider.getInstance();

  public static TOCheeseWheel selectedCheeseWheel;

  @FXML private BorderPane root;
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
  private void assignAllCheeseWheels() {}

  private void setupActionButtons() {
    actionColumn.setCellFactory(param -> new TableCell<>() {
      private final StyledButton assignBtn = new StyledButton(
          StyledButton.Variant.PRIMARY, StyledButton.Size.SM, "Assign", null);

      private final StyledButton unassignBtn =
          new StyledButton(StyledButton.Variant.DESTRUCTIVE,
                           StyledButton.Size.SM, "Unassign", null);

      private final StyledButton viewBtn = new StyledButton(
          StyledButton.Variant.MUTED, StyledButton.Size.SM, "View", null);

      private final HBox box = new HBox(10);

      {
        box.setAlignment(Pos.CENTER_RIGHT);
        box.setStyle("-fx-padding: 0 55 0 0;");

        assignBtn.setOnAction(e -> {
          TOCheeseWheel wheel = getTableView().getItems().get(getIndex());
          selectedCheeseWheel = wheel;

          root.fireEvent(new ShowPopupEvent(
              "view/components/Shelf/AssignCheeseWheelPopUp.fxml",
              "Assign Cheese Wheel"));
        });

        viewBtn.setOnAction(e -> {
          TOCheeseWheel wheel = getTableView().getItems().get(getIndex());
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
          setGraphic(null);
          return;
        }

        if (item.getShelfID() == null) {
          box.getChildren().add(assignBtn);
        } else {
          box.getChildren().addAll(viewBtn, unassignBtn);
        }

        setGraphic(box);

        setAlignment(Pos.CENTER_RIGHT);
      }
    });
  }
}
