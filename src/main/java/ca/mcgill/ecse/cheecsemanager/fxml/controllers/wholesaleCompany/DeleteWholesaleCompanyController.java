package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
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

  private String companyName;
  private Runnable onCloseCallback;
  private MainController mainController;

  public void setOnClose(Runnable callback) {
    this.onCloseCallback = callback;
  }
  
  public void setMainController(MainController controller) {
    this.mainController = controller;
  }

  /**
   * Set the company to be deleted and populate the dialog with company info
   * @param companyName The name of the company to delete
   */
  public void setCompany(String companyName) {
    this.companyName = companyName;
    
    // Fetch company details from backend
    TOWholesaleCompany company = CheECSEManagerFeatureSet6Controller.getWholesaleCompany(companyName);
    
    if (company != null) {
      companyNameLabel.setText(company.getName());
      companyAddressLabel.setText(company.getAddress());
      
      // Get number of orders
      int ordersCount = company.numberOfOrderDates();
      ordersCountLabel.setText(String.valueOf(ordersCount));
    }
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
    String error = CheECSEManagerFeatureSet6Controller.deleteWholesaleCompany(companyName);

    if (error.isEmpty()) {
      if (mainController != null) {
        mainController.showSuccessToast("✓ Company \"" + companyName + "\" deleted successfully!");
      }
      handleClose();
    } else {
      handleClose();
      showErrorDialog(error);
    }
  }

  private void showErrorDialog(String error) {
    try {
      FXMLLoader loader = new FXMLLoader(
          CheECSEManagerApplication.class.getResource(
              "/ca/mcgill/ecse/cheecsemanager/view/page/wholesaleCompany/DeleteError.fxml"
          )
      );
      Parent dialog = loader.load();
      
      DeleteErrorController controller = loader.getController();
      controller.setErrorMessage(error);
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
