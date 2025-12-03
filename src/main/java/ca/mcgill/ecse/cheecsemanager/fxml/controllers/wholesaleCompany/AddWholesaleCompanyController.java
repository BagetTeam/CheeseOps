package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet5Controller;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Input;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.WholesaleCompanyDataProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Controller for adding a new wholesale company to the system.
 * Handles form validation and company creation through the backend controller.
 */
public class AddWholesaleCompanyController {
  private final WholesaleCompanyDataProvider dataProvider =
      WholesaleCompanyDataProvider.getInstance();

  @FXML private VBox root;
  @FXML private Input nameInput;
  @FXML private Input addressInput;
  @FXML private Label errorLabel;

  /**
   * Validates form inputs and creates a new wholesale company.
   * Shows error messages for validation failures or displays success toast.
   */
  @FXML
  private void handleSave() {
    hideError();

    String name = nameInput.getText();
    String address = addressInput.getText();

    String error =
        CheECSEManagerFeatureSet5Controller.addWholesaleCompany(name, address);

    if (error.isEmpty()) {
      dataProvider.refresh();
      root.fireEvent(new HidePopupEvent());
    } else {
      showError(error);
    }
  }

  /**
   * Displays a validation or controller error below the form inputs.
   * @param message error to show
   */
  private void showError(String message) {
    errorLabel.setText(message);
    errorLabel.setVisible(true);
    errorLabel.setManaged(true);
  }

  /** Hides any previously shown error message. */
  private void hideError() {
    errorLabel.setVisible(false);
    errorLabel.setManaged(false);
  }
}
