package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.fxml.components.ShelfGrid;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ViewShelfController {
  @FXML private Label shelfNameLabel;
  @FXML private VBox root;
  @FXML private VBox content;

  public static TOShelf shelfToView;
  private Runnable onBackPressed;

  public void init(TOShelf shelf, Runnable onBackPressed) {
    shelfToView = shelf;
    this.onBackPressed = onBackPressed;

    shelfNameLabel.setText("Shelf " + shelf.getShelfID());
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
    ShelfGrid grid = new ShelfGrid(shelf);
    content.getChildren().add(grid);
  }
}
