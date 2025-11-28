package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet4Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Input;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent.ToastType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CheeseDetailsController {
  @FXML private HBox root;
  @FXML private Label cheeseIdLabel;
  @FXML private Input shelfIdInput;
  @FXML private Input rowInput;
  @FXML private Input columnInput;
  @FXML private Input ageInput;
  @FXML private Input purchaseDateInput;
  @FXML private Input isSpoiledInput;

  private Runnable onClosePressed;
  private TOCheeseWheel cheese;

  public void init(TOCheeseWheel cheese, Runnable onClosePressed) {
    cheeseIdLabel.setText("Cheese Wheel #" + cheese.getId());
    shelfIdInput.setText(cheese.getShelfID());
    rowInput.setText("" + cheese.getRow());
    columnInput.setText("" + cheese.getColumn());
    ageInput.setText(cheese.getMonthsAged());
    purchaseDateInput.setText(cheese.getPurchaseDate().toString());
    isSpoiledInput.setText("" + cheese.getIsSpoiled());

    this.onClosePressed = onClosePressed;
    this.cheese = cheese;
  }

  @FXML
  public void onClosePressed() {
    this.onClosePressed.run();
  }

  @FXML
  public void onSavePressed() {}

  @FXML
  public void onDeletePressed() {
    String error =
        CheECSEManagerFeatureSet4Controller.removeCheeseWheelFromShelf(
            this.cheese.getId());

    if (error.isEmpty()) {
      this.onClosePressed.run();
    } else {
      root.fireEvent(new ToastEvent(error, ToastType.ERROR));
    }
  }
}
