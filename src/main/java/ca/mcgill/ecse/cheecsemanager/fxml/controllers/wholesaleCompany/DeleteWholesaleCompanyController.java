package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.MainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class DeleteWholesaleCompanyController {
  @FXML
  private Label companyNameLabel;

  @FXML
  private Label companyAddressLabel;

  @FXML
  private Label ordersCountLabel;

  private Runnable onCloseCallback;
  private MainController mainController;
  

  public void setOnClose(Runnable callback) {
    this.onCloseCallback = callback;
  }
  
  public void setMainController(MainController controller) {
    this.mainController = controller;
  }

  @FXML
  private void handleClose() {
    if (onCloseCallback != null) {
      onCloseCallback.run();
    }
  }

  @FXML
  private void handleDelete() {
    String companyName = companyNameLabel.getText();
    String errorMessage = "Cannot remove company \"" + companyName + "\": there are orders left.";

    handleClose();

    showErrorDialog(errorMessage);
  }

  private void showErrorDialog(String errorMessage) {
    try {
      FXMLLoader loader = new FXMLLoader(
          CheECSEManagerApplication.class.getResource(
              "/ca/mcgill/ecse/cheecsemanager/view/page/wholesaleCompany/DeleteError.fxml"
          )
      );
      Parent dialog = loader.load();
      
      DeleteErrorController controller = loader.getController();
      controller.setErrorMessage(errorMessage);
      controller.setOnClose(() -> {
        if (mainController != null) {
          mainController.closeDialog();
        }
      });
      
      // Show error dialog in dialogController
      if (mainController != null) {
        mainController.showDialog(dialog);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Error loading ErrorDialog: " + e.getMessage());
    }
  }
}
