package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Animation.AnimationManager;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Animation.EasingInterpolators;
import ca.mcgill.ecse.cheecsemanager.fxml.components.ShelfGrid;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfCheeseWheelDataProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ViewShelfController {
  private ShelfCheeseWheelDataProvider cheeseWheelsProvider =
      ShelfCheeseWheelDataProvider.getInstance();

  @FXML private Label shelfNameLabel;
  @FXML private StackPane root;
  @FXML private VBox content;

  public static TOShelf shelfToView;
  private Runnable onBackPressed;

  public void init(TOShelf shelf, Runnable onBackPressed) {
    shelfToView = shelf;
    this.onBackPressed = onBackPressed;

    shelfNameLabel.setText("Shelf " + shelf.getShelfID());
    this.cheeseWheelsProvider.setShelf(shelf);

    AssignCheeseWheelController.context.shelfId = shelf.getShelfID();

    initShelfGrid(shelf);
  }

  @FXML
  public void initialize() {
    shelfNameLabel.getStyleClass().add("text-fg");
    shelfNameLabel.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(shelfNameLabel, Priority.ALWAYS);
  }

  @FXML
  private void onBackPressed() {
    if (onBackPressed != null) {
      onBackPressed.run();
    }
  }

  @FXML
  public void onAddCheesePressed() {
    root.fireEvent(new ShowPopupEvent(
        "view/components/Shelf/AssignCheeseWheelPopUp.fxml",
        "Assign Cheese Wheel in Shelf " + shelfToView.getShelfID()));
  }

  private void initShelfGrid(TOShelf shelf) {
    ShelfGrid grid = new ShelfGrid(shelf, (cheese) -> showCheeseDetail(cheese));
    content.getChildren().add(grid);
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
