package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfDataProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class ConfirmDeletePopUpController {
  private final ShelfDataProvider shelfDataProvider =
      ShelfDataProvider.getInstance();

  @FXML private Button yesBtn;
  @FXML private Button noBtn;
  @FXML private BorderPane root;
  @FXML private Label warningLabel;

  @FXML
  private void initialize() {
    warningLabel.setText("Are you sure you want to delete shelf " +
                         ShelfController.selectedShelfId +
                         "? This action cannot be undone.");

    yesBtn.setOnAction(e -> confirmDelete());
    noBtn.setOnAction(e -> closePopup());
  }

  private void closePopup() { root.fireEvent(new HidePopupEvent()); }

  private void confirmDelete() {
    String shelfId = ShelfController.selectedShelfId;

    if (shelfId != null) {
      String result =
          ca.mcgill.ecse.cheecsemanager.controller
              .CheECSEManagerFeatureSet2Controller.deleteShelf(shelfId);

      if (result != null) {
        System.err.println("Delete Shelf Error: " + result);
      }
    }

    shelfDataProvider.refresh();
    closePopup();
  }
}
