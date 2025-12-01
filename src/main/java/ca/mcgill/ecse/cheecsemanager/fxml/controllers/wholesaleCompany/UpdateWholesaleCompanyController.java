package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet5Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Input;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent.ToastType;
import ca.mcgill.ecse.cheecsemanager.fxml.store.WholesaleCompanyDataProvider;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Controller for updating an existing wholesale company's details.
 * Handles form validation and company modification through the backend
 * controller.
 */
public class UpdateWholesaleCompanyController {
  private final WholesaleCompanyDataProvider dataProvider =
      WholesaleCompanyDataProvider.getInstance();

  public static String currentCompanyName;

  @FXML private VBox root;
  @FXML private Label errorLabel;

  @FXML private Input nameInput;
  @FXML private Input addressInput;

  private Consumer<String> onCloseCallback;

  /**
   * Registers a callback to execute when the dialog closes.
   * Callback receives the updated company name or null if unchanged.
   *
   * @param callback the action to run on dialog close, accepting updated name
   */
  public void setOnClose(Consumer<String> callback) {
    this.onCloseCallback = callback;
  }

  /**
   * Set the company to be deleted and populate the dialog with company info
   * @param companyName The name of the company to delete
   */
  public void setCompany(String companyName) {
    // currentCompanyName = companyName;

    // Fetch company details from backend
  }

  @FXML
  private void initialize() {
    TOWholesaleCompany company =
        CheECSEManagerFeatureSet6Controller.getWholesaleCompany(
            currentCompanyName);

    if (company != null) {
      this.nameInput.setText(company.getName());
      this.addressInput.setText(company.getAddress());
    }
  }

  @FXML
  private void handleClose() {
    this.root.fireEvent(new HidePopupEvent());
  }

  /**
   * Validates form inputs and updates the wholesale company.
   * Shows error messages for validation failures or displays success toast.
   * Passes updated company name to callback for view refresh.
   */
  @FXML
  private void handleSave() {
    hideError();

    String newName = this.nameInput.getText();
    String newAddress = this.addressInput.getText();

    String error = CheECSEManagerFeatureSet5Controller.updateWholesaleCompany(
        currentCompanyName, newName, newAddress);

    if (error.isEmpty()) {
      root.fireEvent(new ToastEvent("Company " + currentCompanyName +
                                        " updated successfully!",
                                    ToastType.SUCCESS));

      dataProvider.refresh();
      handleClose();
    } else {
      showError(error);
    }
  }

  private void showError(String message) {
    errorLabel.setText(message);
    errorLabel.setVisible(true);
    errorLabel.setManaged(true);
  }

  private void hideError() {
    errorLabel.setVisible(false);
    errorLabel.setManaged(false);
  }
}
