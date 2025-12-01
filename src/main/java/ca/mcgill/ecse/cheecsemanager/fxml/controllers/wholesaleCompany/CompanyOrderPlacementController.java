package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet5Controller;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Input;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CompanyOrderPlacementController {
  public static String companyName;

  @FXML private VBox root;
  @FXML private ComboBox<String> maturationPeriodComboBox;
  @FXML private Input quantityInput;
  @FXML private TextField purchaseDateField;
  @FXML private DatePicker deliveryDatePicker;
  @FXML private HBox errorContainer;
  @FXML private Label errorLabel;

  private Runnable onCloseCallback;
  private LocalDate purchaseDate;

  @FXML
  public void initialize() {
    maturationPeriodComboBox.setItems(FXCollections.observableArrayList(
        "Six", "Twelve", "TwentyFour", "ThirtySix"));

    // Set purchase date to current date (read-only)
    purchaseDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    purchaseDateField.setText(purchaseDate.format(formatter));

    // Apply custom styles to DatePicker
    deliveryDatePicker.setStyle("-fx-background-color: -color-bg; "
                                + "-fx-text-fill: -color-fg; "
                                + "-fx-prompt-text-fill: -color-muted; "
                                + "-fx-padding: 8 12; "
                                + "-fx-font-size: 14px;");

    // Style the text field inside DatePicker
    deliveryDatePicker.getEditor().setStyle(
        "-fx-background-color: -color-bg; "
        + "-fx-text-fill: -color-fg; "
        + "-fx-prompt-text-fill: -color-muted; "
        + "-fx-padding: 8 12; "
        + "-fx-font-size: 14px;");
  }

  /**
   * Registers a callback to execute when the dialog closes.
   *
   * @param callback the action to run on dialog close
   */
  public void setOnClose(Runnable callback) { this.onCloseCallback = callback; }

  @FXML
  private void handleClose() {
    if (onCloseCallback != null) {
      onCloseCallback.run();
    }
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
    Date sqlPurchaseDate = Date.valueOf(purchaseDate);
    Date sqlDeliveryDate = Date.valueOf(deliveryDate);

    // Call controller method
    String error = CheECSEManagerFeatureSet5Controller.sellCheeseWheels(
        companyName, sqlPurchaseDate, quantity, maturationPeriod,
        sqlDeliveryDate);

    if (error.isEmpty()) {
      handleClose();
    } else {
      showError(error);
    }
  }

  private void showError(String message) {
    errorLabel.setText(message);
    errorContainer.setVisible(true);
    errorContainer.setManaged(true);
  }

  private void hideError() {
    errorContainer.setVisible(false);
    errorContainer.setManaged(false);
  }
}
