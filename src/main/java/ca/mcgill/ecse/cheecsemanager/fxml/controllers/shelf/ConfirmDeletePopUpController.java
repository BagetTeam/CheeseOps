package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet2Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class ConfirmDeletePopUpController {

  @FXML private Button yesBtn;
  @FXML private Button noBtn;

  private AnchorPane popupOverlay;
  private ShelfController mainController;
  private TOShelf shelfToDelete;

  public void setPopupOverlay(AnchorPane overlay) {
    this.popupOverlay = overlay;
  }
  public void setMainController(ShelfController controller) {
    this.mainController = controller;
  }
  public void setShelfToDelete(TOShelf shelf) { this.shelfToDelete = shelf; }

  @FXML
  private void initialize() {
    yesBtn.setOnAction(e -> confirmDelete());
    noBtn.setOnAction(e -> closePopup());
  }

  private void closePopup() {}

  private void confirmDelete() {
    if (shelfToDelete != null) {
      CheECSEManagerFeatureSet2Controller.deleteShelf(
          shelfToDelete.getShelfID());
      if (mainController != null) {
        mainController.refreshTable();
      }
    }
    closePopup();
  }
}
