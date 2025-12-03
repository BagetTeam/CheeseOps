package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfDataProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Confirmation dialog controller for deleting a shelf. */
public class ConfirmDeletePopUpController {
  private final ShelfDataProvider shelfDataProvider =
      ShelfDataProvider.getInstance();

  @FXML private Button yesBtn;
  @FXML private Button noBtn;
  @FXML private VBox root;
  @FXML private Label warningLabel;
  @FXML private Label errorLabel;


  /** Configures the confirmation message and button actions. */
  @FXML
  private void initialize() {
    warningLabel.setText("Are you sure you want to delete shelf " +
                         ShelfController.selectedShelfId +
                         "? This action cannot be undone.");

    yesBtn.setOnAction(e -> confirmDelete());
    noBtn.setOnAction(e -> closePopup());
  }

  /** Closes the dialog without deleting. */
  private void closePopup() { root.fireEvent(new HidePopupEvent()); }

  /** Invokes the controller to delete the selected shelf and refreshes data. */
  private void confirmDelete() {
    String shelfId = ShelfController.selectedShelfId;

    if (shelfId != null) {
      String result =
          ca.mcgill.ecse.cheecsemanager.controller
              .CheECSEManagerFeatureSet2Controller.deleteShelf(shelfId);

      if (result != null) {
        errorLabel.setText(result);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
        return;
      }
    }

    shelfDataProvider.refresh();
    closePopup();
  }
}
