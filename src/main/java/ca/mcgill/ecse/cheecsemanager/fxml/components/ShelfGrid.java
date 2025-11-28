package ca.mcgill.ecse.cheecsemanager.fxml.components;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ShelfGrid extends BorderPane {
  private static final double CELL_SIZE = 80;
  private static final double CELL_PADDING = 5;
  private static final double GAP = 5;
  private static final double LABEL_SIZE = 30;

  private final GridPane grid = new GridPane();
  private final Map<Integer, Node> cheeseWheelNodes = new HashMap<>();
  private final Map<String, TOCheeseWheel> cheeseWheels = new HashMap<>();

  private final ObjectProperty<TOCheeseWheel> selectedCheeseWheel =
      new SimpleObjectProperty<>();

  private final VBox rowLabels = new VBox();
  private final HBox columnLabels = new HBox();
  private final ScrollPane scrollPane = new ScrollPane();
  private final VBox scrollContent = new VBox();
  private final Label cornerLabel = new Label();

  private TOShelf shelf;

  public ShelfGrid(TOShelf shelf) {
    this.shelf = shelf;
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
    grid.widthProperty().addListener((obs, oldVal, newVal) -> {
      columnLabels.setMinWidth(newVal.doubleValue());
      columnLabels.setPrefWidth(newVal.doubleValue());
    });

    setCheeseWheels(
        Arrays.stream(shelf.getCheeseWheelIDs())
            .map(CheECSEManagerFeatureSet3Controller::getCheeseWheel)
            .toList());
  }

  public void setCheeseWheels(List<TOCheeseWheel> cheeseWheels) {
    grid.getChildren().clear();
    cheeseWheelNodes.clear();

    // Track max column for sizing
    int maxColumn = this.shelf.getMaxColumns();
    int maxRow = this.shelf.getMaxRows();

    for (TOCheeseWheel cheese : cheeseWheels) {
      Node cheeseNode = createCheeseWheelNode(cheese);
      int col = cheese.getColumn() - 1;
      int row = cheese.getRow() - 1;
      grid.add(cheeseNode, col, row);
      cheeseWheelNodes.put(cheese.getId(), cheeseNode);
      this.cheeseWheels.put(col + "," + row, cheese);
    }

    for (int row = 0; row < maxRow; row++) {
      for (int col = 0; col < maxColumn; col++) {
        if (this.cheeseWheels.containsKey(col + "," + row)) {
          continue;
        }
        var region = new Region();
        region.getStyleClass().add("cheese-wheel-cell");
        grid.add(region, col, row);
      }
    }

    // Set column constraints
    grid.getColumnConstraints().clear();
    for (int col = 1; col <= maxColumn; col++) {
      ColumnConstraints colConstraints = new ColumnConstraints(CELL_SIZE);
      grid.getColumnConstraints().add(colConstraints);
      Label colLabel = new Label(String.valueOf(col));
      colLabel.getStyleClass().add("column-label");
      colLabel.setPrefWidth(CELL_SIZE);
      colLabel.setAlignment(Pos.CENTER);
      columnLabels.getChildren().add(colLabel);
    }
  }

  private Node createCheeseWheelNode(TOCheeseWheel cheese) {
    VBox container = new VBox(5);
    container.setAlignment(Pos.CENTER);
    container.getStyleClass().add("cheese-wheel-cell");

    Icon cheeseIcon = new Icon("SmolCheeseWheel");

    Label nameLabel = new Label("" + cheese.getId());
    nameLabel.getStyleClass().addAll("text-bold", "text-fg", "text-sm");

    // Add location indicator
    Label locationLabel =
        new Label("(" + cheese.getColumn() + ", " + cheese.getRow() + ")");
    locationLabel.getStyleClass().addAll("text-fg", "text-sm");

    container.getChildren().addAll(cheeseIcon, nameLabel, locationLabel);
    container.setUserData(cheese);

    return container;
  }

  public void searchAndSelect(String query) {
    String key = query.toLowerCase().trim();
    Node targetNode = cheeseWheelNodes.get(key);

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
