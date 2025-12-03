package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet2Controller;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Input;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.ShelfDataProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
/*
 * @author Ayush, Ming Li Liu
 * */
import javafx.scene.layout.VBox;

/** Popup controller that creates a new shelf with user-specified dimensions. */
public class AddShelfPopUpController {

  @FXML private Input shelfIdInput;
  @FXML private Input rowsInput;
  @FXML private Input colsInput;

  private TextField idField;
  private TextField rowsField;
  private TextField colsField;

  @FXML private Button addBtn;

  @FXML private Label errorLabel;
  @FXML private VBox root;

  /** Wires the add button and caches the underlying text fields. */
  @FXML
  public void initialize() {
    addBtn.setOnAction(e -> submit());

    idField = shelfIdInput.getTextField();
    rowsField = rowsInput.getTextField();
    colsField = colsInput.getTextField();
  }

  /** Closes the popup when the shelf is created or the user cancels. */
  private void closePopup() { root.fireEvent(new HidePopupEvent()); }

  /** Validates the form and calls the controller to create a shelf. */
  private void submit() {
    if (errorLabel != null) {
      errorLabel.setText("");
    }

    String id = idField.getText();
    String rowsText = rowsField.getText();
    String colsText = colsField.getText();

    if (id == null || id.isBlank() || rowsText == null || colsText == null) {
      showError("Please fill in all fields.");
      return;
    }

    Integer rows;
    Integer cols;

    try {
      rows = Integer.parseInt(rowsText);
      cols = Integer.parseInt(colsText);
    } catch (NumberFormatException e) {
      showError("Rows and Columns must be integers.");
      return;
    }

    String result =
        CheECSEManagerFeatureSet2Controller.addShelf(id, cols, rows);

    if (result != null) {
      showError(result);
    } else {
      ShelfDataProvider.getInstance().refresh();
      closePopup();
    }
  }

  /**
   * Renders a validation or controller error underneath the form.
   * @param message error text for the user
   */
  private void showError(String message) {
    if (errorLabel != null) {
      errorLabel.setText(message);
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);
    } else {
      System.err.println("Add Shelf Error: " + message);
    }
  }
}
