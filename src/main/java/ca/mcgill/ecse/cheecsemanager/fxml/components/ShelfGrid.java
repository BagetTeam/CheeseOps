package ca.mcgill.ecse.cheecsemanager.fxml.components;

import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf.AssignCheeseWheelController;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfCheeseWheelDataProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * A custom component that displays a grid of cheese wheels in a shelf.
 * @author Ming Li Liu
 * */
public class ShelfGrid extends BorderPane {
  private ShelfCheeseWheelDataProvider cheeseWheelsProvider =
      ShelfCheeseWheelDataProvider.getInstance();

  private static final double CELL_SIZE = 80;
  private static final double GAP = 5;
  private static final double LABEL_SIZE = 30;

  private final GridPane grid = new GridPane();

  private final Map<String, StackPane> locationNodes = new HashMap<>();
  private final Map<Integer, String> cheeseWheelsLocations = new HashMap<>();

  private final ObjectProperty<TOCheeseWheel> selectedCheeseWheel =
      new SimpleObjectProperty<>();

  private final VBox rowLabels = new VBox();
  private final HBox columnLabels = new HBox();
  private final ScrollPane scrollPane = new ScrollPane();
  private final VBox scrollContent = new VBox();
  private final Label cornerLabel = new Label();

  private TOShelf shelf;
  private Consumer<TOCheeseWheel> callback;

  private final ListChangeListener<TOCheeseWheel> onChangeDetectedListener =
      change -> onChangeDetected(change);

  private ChangeListener<? super Number> gridWidthCallback =
      (obs, oldVal, newVal) -> {
    columnLabels.setMinWidth(newVal.doubleValue());
    columnLabels.setPrefWidth(newVal.doubleValue());
  };

  public ShelfGrid(TOShelf shelf, Consumer<TOCheeseWheel> callback) {
    this.shelf = shelf;
    this.callback = callback;

    initialize();
  }

  private void initialize() {
    cornerLabel.setPrefSize(LABEL_SIZE, LABEL_SIZE);
    cornerLabel.getStyleClass().add("corner-label");

    // Setup row labels (frozen, left side)
    rowLabels.setSpacing(GAP);
    for (int row = 1; row <= this.shelf.getMaxRows(); row++) {
      Label rowLabel = new Label(String.valueOf(row));
      rowLabel.getStyleClass().add("row-label");
      rowLabel.setPrefSize(LABEL_SIZE, CELL_SIZE);
      rowLabel.setAlignment(Pos.CENTER);
      rowLabels.getChildren().add(rowLabel);
    }

    // Set column constraints
    grid.getColumnConstraints().clear();
    for (int col = 1; col <= this.shelf.getMaxColumns(); col++) {
      ColumnConstraints colConstraints = new ColumnConstraints(CELL_SIZE);
      grid.getColumnConstraints().add(colConstraints);
      Label colLabel = new Label(String.valueOf(col));
      colLabel.getStyleClass().add("column-label");
      colLabel.setPrefWidth(CELL_SIZE);
      colLabel.setAlignment(Pos.CENTER);
      columnLabels.getChildren().add(colLabel);
    }

    // Setup column labels (scrolls horizontally)
    columnLabels.setSpacing(GAP);
    columnLabels.setPadding(new Insets(GAP, 0, GAP, 0));

    // Setup grid
    grid.setPadding(Insets.EMPTY);
    grid.setHgap(GAP);
    grid.setVgap(GAP);

    for (int row = 0; row < this.shelf.getMaxRows(); row++) {
      RowConstraints rowConstraints = new RowConstraints(CELL_SIZE);
      grid.getRowConstraints().add(rowConstraints);
    }

    // Setup scrollable content (column labels + grid)
    scrollContent.setSpacing(0);
    scrollContent.getChildren().addAll(columnLabels, grid);

    // Configure ScrollPane
    scrollPane.setContent(scrollContent);
    scrollPane.setFitToHeight(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    VBox leftPane = new VBox(cornerLabel, rowLabels);
    leftPane.setSpacing(5);
    leftPane.setPadding(new Insets(0, 4, 0, 0));
    setLeft(leftPane);
    setCenter(scrollPane);

    // Sync column labels width with grid
    grid.widthProperty().removeListener(gridWidthCallback);
    grid.widthProperty().addListener(gridWidthCallback);

    var wheels = cheeseWheelsProvider.getWheels();
    this.setCheeseWheels(wheels);

    wheels.removeListener(onChangeDetectedListener);
    wheels.addListener(onChangeDetectedListener);

    this.sceneProperty().addListener((obs, oldScene, newScene) -> {
      if (newScene == null) {
        wheels.removeListener(onChangeDetectedListener);
        grid.widthProperty().removeListener(gridWidthCallback);
      }
    });
  }

  private void
  onChangeDetected(ListChangeListener.Change<? extends TOCheeseWheel> change) {
    System.out.println("============== onChangeDetected ==============");
    javafx.application.Platform.runLater(() -> {
      while (change.next()) {
        if (change.wasRemoved()) {
          for (var cw : change.getRemoved()) {
            String key = cw.getColumn() + "," + cw.getRow();

            var addButtonNode =
                createAddButtonNode(cw.getColumn(), cw.getRow());

            var container = locationNodes.get(key);
            container.getChildren().clear();
            container.getChildren().add(addButtonNode);

            cheeseWheelsLocations.remove(cw.getId());
          }
        }

        if (change.wasAdded()) {
          for (var cw : change.getAddedSubList()) {
            String key = cw.getColumn() + "," + cw.getRow();
            var cheeseWheelNode = createCheeseWheelNode(cw);

            var container = locationNodes.get(key);
            container.getChildren().clear();
            container.getChildren().add(cheeseWheelNode);

            cheeseWheelsLocations.put(cw.getId(), key);
          }
        }

        if (change.wasUpdated()) {
          var cheeseWheels = cheeseWheelsProvider.getWheels();
          for (int i = change.getFrom(); i < change.getTo(); i++) {
            var cw = cheeseWheels.get(i);
            String newKey = cw.getColumn() + "," + cw.getRow();
            String oldKey = cheeseWheelsLocations.get(cw.getId());

            if (newKey.equals(oldKey)) {
              continue;
            }

            // swap nodes
            var oldCheeseWheelNode = locationNodes.get(oldKey);

            var newContainer = locationNodes.get(newKey);
            newContainer.getChildren().clear();
            newContainer.getChildren().add(oldCheeseWheelNode);

            var oldKeyArr = oldKey.split(",");
            var oldColumn = Integer.parseInt(oldKeyArr[0]);
            var oldRow = Integer.parseInt(oldKeyArr[1]);

            var oldContainer = locationNodes.get(oldKey);
            oldContainer.getChildren().clear();
            oldContainer.getChildren().add(
                createAddButtonNode(oldColumn, oldRow));

            cheeseWheelsLocations.put(cw.getId(), newKey);
          }
        }
      }
    });
  }

  public void setCheeseWheels(ObservableList<TOCheeseWheel> cheeseWheels) {
    grid.getChildren().clear();
    locationNodes.clear();
    cheeseWheelsLocations.clear();

    // Track max column for sizing
    int maxColumn = this.shelf.getMaxColumns();
    int maxRow = this.shelf.getMaxRows();

    for (int row = 1; row <= maxRow; row++) {
      for (int col = 1; col <= maxColumn; col++) {
        String key = col + "," + row;
        var region = createEmptyLocationNode();
        region.getChildren().add(createAddButtonNode(col, row));

        locationNodes.put(key, region);
        grid.add(region, col - 1, row - 1);
      }
    }

    for (TOCheeseWheel cheese : cheeseWheels) {
      Node cheeseNode = createCheeseWheelNode(cheese);
      int col = cheese.getColumn();
      int row = cheese.getRow();

      if (col < 0 || row < 0) {
        continue;
      }

      String key = col + "," + row;

      StackPane locationNode = locationNodes.get(key);
      locationNode.getChildren().clear();
      locationNode.getChildren().add(cheeseNode);

      cheeseWheelsLocations.put(cheese.getId(), key);
    }
  }

  private Node createCheeseWheelNode(TOCheeseWheel cheese) {
    VBox container = new VBox(5);
    container.setAlignment(Pos.CENTER);
    container.getStyleClass().add("cheese-wheel-cell");

    Icon cheeseIcon = new Icon("SmolCheeseWheel");

    Label nameLabel = new Label("#" + cheese.getId());
    nameLabel.getStyleClass().addAll("text-bold", "text-fg", "text-sm");

    // Add location indicator
    Label locationLabel =
        new Label("(" + cheese.getColumn() + ", " + cheese.getRow() + ")");
    locationLabel.getStyleClass().addAll("text-fg", "text-sm");

    container.getChildren().addAll(cheeseIcon, nameLabel, locationLabel);
    container.setUserData(cheese);

    container.setOnMouseClicked(e -> this.callback.accept(cheese));

    return container;
  }

  private Node createAddButtonNode(int col, int row) {
    VBox container = new VBox();
    container.setAlignment(Pos.CENTER);
    container.getStyleClass().add("cheese-wheel-cell");

    Icon icon = new Icon("Plus");
    container.getChildren().add(icon);

    container.setOnMouseClicked(e -> {
      AssignCheeseWheelController.context.row = row;
      AssignCheeseWheelController.context.col = col;
      this.fireEvent(new ShowPopupEvent(
          "view/components/Shelf/AssignCheeseWheelPopUp.fxml",
          "Assign Cheese Wheel in Shelf " + shelf.getShelfID()));
    });

    return container;
  }

  private StackPane createEmptyLocationNode() {
    var region = new Region();
    region.getStyleClass().add("cheese-wheel-cell");

    StackPane pane = new StackPane(region);
    pane.setPrefSize(CELL_SIZE, CELL_SIZE);
    return pane;
  }

  public void searchAndSelect(String query) {
    String key = query.toLowerCase().trim();
    Node targetNode = locationNodes.get(key);

    if (targetNode != null) {
      grid.getChildren().forEach(
          node -> node.getStyleClass().remove("selected"));
      targetNode.getStyleClass().add("selected");

      TOCheeseWheel cheese = (TOCheeseWheel)targetNode.getUserData();
      scrollToLocation(cheese.getColumn());
      selectedCheeseWheel.set(cheese);
    }
  }

  private void scrollToLocation(int column) {
    double cellWidth = CELL_SIZE + GAP;
    double targetX = column * cellWidth;

    double contentWidth = grid.getWidth();
    double viewportWidth = scrollPane.getViewportBounds().getWidth();

    if (contentWidth > viewportWidth) {
      double hValue = targetX / (contentWidth - viewportWidth);
      hValue = Math.max(0, Math.min(1, hValue));

      Timeline timeline = new Timeline(
          new KeyFrame(Duration.millis(300),
                       new KeyValue(scrollPane.hvalueProperty(), hValue)));
      timeline.play();
    }
  }

  public ObjectProperty<TOCheeseWheel> selectedCheeseWheelProperty() {
    return selectedCheeseWheel;
  }
}
