package ca.mcgill.ecse.cheecsemanager.fxml.components;

import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ShelfGrid extends ScrollPane {
  private static final double CELL_SIZE = 80;
  private static final double CELL_PADDING = 5;

  private final GridPane grid = new GridPane();
  private final Map<Integer, Node> cheeseWheelNodes = new HashMap<>();
  private final ObjectProperty<TOCheeseWheel> selectedCheeseWheel =
      new SimpleObjectProperty<>();

  private TOShelf shelf;

  public ShelfGrid(TOShelf shelf) {
    this.shelf = shelf;
    initialize();
  }

  private void initialize() {
    // Configure ScrollPane
    setFitToHeight(true);
    setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
    setVbarPolicy(ScrollBarPolicy.NEVER);

    // Configure GridPane
    grid.setPadding(new Insets(10));
    grid.setHgap(5);
    grid.setVgap(5);

    // Create 10 rows
    for (int row = 0; row < this.shelf.getMaxRows(); row++) {
      RowConstraints rowConstraints = new RowConstraints(CELL_SIZE);
      grid.getRowConstraints().add(rowConstraints);
    }

    setContent(grid);
  }

  public void setCheeseWheels(List<TOCheeseWheel> cheeseWheels) {
    grid.getChildren().clear();
    cheeseWheelNodes.clear();

    // Track max column for sizing
    int maxColumn = this.shelf.getMaxColumns();

    for (TOCheeseWheel cheese : cheeseWheels) {
      Node cheeseNode = createCheeseWheelNode(cheese);
      grid.add(cheeseNode, cheese.getColumn(), cheese.getRow());
      cheeseWheelNodes.put(cheese.getId(), cheeseNode);
    }

    // Set column constraints
    grid.getColumnConstraints().clear();
    for (int col = 0; col <= maxColumn; col++) {
      ColumnConstraints colConstraints = new ColumnConstraints(CELL_SIZE);
      grid.getColumnConstraints().add(colConstraints);
    }
  }

  private Node createCheeseWheelNode(TOCheeseWheel cheese) {
    VBox container = new VBox(5);
    container.setAlignment(Pos.CENTER);
    container.getStyleClass().add("cheese-wheel-cell");

    Icon cheeseIcon = new Icon("SmolCheeseWheel");
    cheeseIcon.getStyleClass().add("cheese-icon");

    Label nameLabel = new Label("" + cheese.getId());
    nameLabel.getStyleClass().add("cheese-label");

    // Add location indicator
    Label locationLabel =
        new Label("(" + cheese.getColumn() + ", " + cheese.getRow() + ")");
    locationLabel.getStyleClass().add("location-label");

    container.getChildren().addAll(cheeseIcon, nameLabel, locationLabel);
    container.setUserData(cheese);

    return container;
  }

  public void searchAndSelect(String query) {
    String key = query.toLowerCase().trim();
    Node targetNode = cheeseWheelNodes.get(Integer.parseInt(key));

    if (targetNode != null) {
      // Remove previous selection
      grid.getChildren().forEach(
          node -> node.getStyleClass().remove("selected"));

      // Highlight selected
      targetNode.getStyleClass().add("selected");

      // Scroll to position
      TOCheeseWheel cheese = (TOCheeseWheel)targetNode.getUserData();
      scrollToColumn(cheese.getColumn());

      // Update property
      selectedCheeseWheel.set(cheese);
    }
  }

  private void scrollToColumn(int column) {
    double cellWidth = CELL_SIZE + CELL_PADDING;
    double targetX = column * cellWidth;

    // Animate scroll
    Timeline timeline = new Timeline(
        new KeyFrame(Duration.millis(300),
                     new KeyValue(this.hvalueProperty(),
                                  targetX / (grid.getWidth() -
                                             getViewportBounds().getWidth()))));
    timeline.play();
  }

  public ObjectProperty<TOCheeseWheel> selectedCheeseWheelProperty() {
    return selectedCheeseWheel;
  }
}
