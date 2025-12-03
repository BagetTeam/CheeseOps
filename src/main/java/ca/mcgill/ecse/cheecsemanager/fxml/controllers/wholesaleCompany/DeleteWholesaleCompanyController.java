package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent.ToastType;
import ca.mcgill.ecse.cheecsemanager.fxml.store.WholesaleCompanyDataProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Controller for deleting a wholesale company from the system.
 * Displays confirmation dialog and handles company removal through backend.
 */
public class DeleteWholesaleCompanyController {
  private final WholesaleCompanyDataProvider dataProvider =
      WholesaleCompanyDataProvider.getInstance();

  public static String companyName;
  public static Runnable onDeleteCallback;

  @FXML private Label companyNameLabel;
  @FXML private Label companyAddressLabel;
  @FXML private Label ordersCountLabel;
  @FXML private Label errorLabel;
  @FXML private VBox root;

  /**
   * Set the company to be deleted and populate the dialog with company info
   */
  @FXML
  public void initialize() {
    // Fetch company details from backend
    TOWholesaleCompany company =
        CheECSEManagerFeatureSet6Controller.getWholesaleCompany(companyName);

    if (company != null) {
      companyNameLabel.setText(company.getName());
      companyAddressLabel.setText(company.getAddress());

      // Get number of orders
      int ordersCount = company.numberOfOrderDates();
      ordersCountLabel.setText(String.valueOf(ordersCount));
    }
  }

  /** Closes the confirmation popup without deleting. */
  @FXML
  private void handleClose() {
    this.root.fireEvent(new HidePopupEvent());
  }

  /**
   * Executes company deletion through the backend controller.
   * Shows success toast on completion and triggers navigation callback.
   */
  @FXML
  private void handleDelete() {
    String companyName = companyNameLabel.getText();
    String error =
        CheECSEManagerFeatureSet6Controller.deleteWholesaleCompany(companyName);

    if (error.isEmpty()) {
      dataProvider.refresh();
      this.root.fireEvent(
          new ToastEvent("Company  " + companyName + " deleted successfully!",
                         ToastType.SUCCESS));
      this.root.fireEvent(new HidePopupEvent());
      if (onDeleteCallback != null) {
        onDeleteCallback.run();
      }
    } else {
      this.errorLabel.setText(error);
      this.errorLabel.setVisible(true);
      this.errorLabel.setManaged(true);
    }
  }
}
