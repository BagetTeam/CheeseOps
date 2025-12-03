package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet5Controller;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Input;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent.ToastType;
import ca.mcgill.ecse.cheecsemanager.fxml.store.OrdersProvider;
import ca.mcgill.ecse.cheecsemanager.fxml.store.WholesaleCompanyDataProvider;
import java.sql.Date;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Popup controller used to capture a wholesale company order. */
public class CompanyOrderPlacementController {
  private final OrdersProvider ordersProvider = OrdersProvider.getInstance();

  public static String companyName;

  @FXML private VBox root;
  @FXML private ComboBox<String> maturationPeriodComboBox;
  @FXML private Input quantityInput;
  @FXML private DatePicker transactionDatePicker;
  @FXML private DatePicker deliveryDatePicker;

  @FXML private Label errorLabel;

  /** Populates the maturation period combo box with supported values. */
  @FXML
  public void initialize() {
    maturationPeriodComboBox.setItems(FXCollections.observableArrayList(
        "Six", "Twelve", "TwentyFour", "ThirtySix"));
  }

  /** Closes the popup shell when the user cancels. */
  @FXML
  private void handleClose() {
    root.fireEvent(new HidePopupEvent());
  }

  /**
   * Validates order inputs and creates the order through the backend.
   * Checks for valid maturation period, positive quantity, and delivery date.
   * Order date defaults to today and delivery must be after maturation date.
   * Missing wheels are tracked if insufficient inventory is available.
   */
  @FXML
  private void handleSave() {
    hideError();

    String maturationPeriod = maturationPeriodComboBox.getValue();
    String quantityText = quantityInput.getText();
    LocalDate deliveryDate = deliveryDatePicker.getValue();
    LocalDate transactionDate = transactionDatePicker.getValue();

    // Validate inputs
    if (maturationPeriod == null || maturationPeriod.isEmpty()) {
      showError("Please select a maturation period.");
      return;
    }

    Integer quantity;
    try {
      quantity = Integer.parseInt(quantityText);
      if (quantity <= 0) {
        showError("Quantity must be greater than zero.");
        return;
      }
    } catch (NumberFormatException e) {
      showError("Please enter a valid quantity.");
      return;
    }

    if (deliveryDate == null) {
      showError("Please select a delivery date.");
      return;
    }

    // Convert LocalDate to java.sql.Date

    Date sqlTransactionDate;
    Date sqlDeliveryDate;
    try {

      sqlTransactionDate = Date.valueOf(transactionDate);
      sqlDeliveryDate = Date.valueOf(deliveryDate);
    } catch (Exception e) {
      showError("Invalid date format.");
      return;
    }
    // Call controller method
    String error = CheECSEManagerFeatureSet5Controller.sellCheeseWheels(
        companyName, sqlTransactionDate, quantity, maturationPeriod,
        sqlDeliveryDate);

    if (error.isEmpty()) {
      this.root.fireEvent(
          new ToastEvent("Order placed successfully", ToastType.SUCCESS));
      ordersProvider.refresh();
      WholesaleCompanyDataProvider.getInstance().refresh();
      handleClose();
    } else {
      showError(error);
    }
  }

  /**
   * Displays an error message beneath the form fields.
   * @param message human-readable error
   */
  private void showError(String message) {
    errorLabel.setText(message);
    errorLabel.setVisible(true);
    errorLabel.setManaged(true);
  }

  /** Hides the currently displayed error label. */
  private void hideError() {
    errorLabel.setVisible(false);
    errorLabel.setManaged(false);
  }
}
