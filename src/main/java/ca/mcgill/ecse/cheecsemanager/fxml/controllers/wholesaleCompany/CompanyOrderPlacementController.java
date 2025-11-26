package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet5Controller;
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

public class CompanyOrderPlacementController {

    @FXML private ComboBox<String> maturationPeriodComboBox;
    @FXML private TextField quantityField;
    @FXML private TextField purchaseDateField;
    @FXML private DatePicker deliveryDatePicker;
    @FXML private HBox errorContainer;
    @FXML private Label errorLabel;

    private String companyName;
    private Runnable onCloseCallback;
    private ToastProvider mainController;
    private LocalDate purchaseDate;

    @FXML
    public void initialize() {
        maturationPeriodComboBox.setItems(
            FXCollections.observableArrayList("Six", "Twelve", "TwentyFour", "ThirtySix"));

        // Set purchase date to current date (read-only)
        purchaseDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        purchaseDateField.setText(purchaseDate.format(formatter));

        // Apply custom styles to ComboBox
        maturationPeriodComboBox.setStyle(
            "-fx-background-color: -color-bg; "
            + "-fx-text-fill: -color-fg; "
            + "-fx-prompt-text-fill: -color-muted; "
            + "-fx-padding: 8 12; "
            + "-fx-font-size: 14px;");

        // Apply custom styles to DatePicker
        deliveryDatePicker.setStyle(
            "-fx-background-color: -color-bg; "
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

    public void setCompany(String companyName) {
        this.companyName = companyName;
    }

    public void setMainController(ToastProvider controller) {
        this.mainController = controller;
    }

    public void setOnClose(Runnable callback) {
        this.onCloseCallback = callback;
    }

    @FXML
    private void handleClose() {
        if (onCloseCallback != null) {
            onCloseCallback.run();
        }
    }

    @FXML
    private void handleSave() {
        hideError();

        String maturationPeriod = maturationPeriodComboBox.getValue();
        String quantityText = quantityField.getText();
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
            companyName, sqlPurchaseDate, quantity, maturationPeriod, sqlDeliveryDate);

        if (error.isEmpty()) {
            if (mainController != null) {
                mainController.showSuccessToast(
                    "✓ Order placed successfully for " + companyName + "!");
            }
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