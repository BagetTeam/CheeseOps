package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Farmer;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class DeleteFarmerPopup {
    @FXML
    private Button yesBtn;

    @FXML
    private Button noBtn;

    private AnchorPane popupOverlay;
    private FarmerController mainController;

    public void setPopupOverlay(AnchorPane overlay) {
        this.popupOverlay = overlay;
    }

    public void setMainController(FarmerController controller) {
        this.mainController = controller;
    }

    @FXML
    public void initialize() {
        yesBtn.setOnAction(e -> confirmDelete());
        noBtn.setOnAction(e -> closePopup());
    }

    private void closePopup() {
        if (mainController != null && popupOverlay != null) {
            mainController.removePopup(popupOverlay);
        }
    }

    private void confirmDelete() {
        System.out.println("Confirmed delete!");
        // TODO: Add deletion logic here
        closePopup();
    }

}
