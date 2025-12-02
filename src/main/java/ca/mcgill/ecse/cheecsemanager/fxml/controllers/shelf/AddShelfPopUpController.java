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

  @FXML
  public void initialize() {
    addBtn.setOnAction(e -> submit());

    idField = shelfIdInput.getTextField();
    rowsField = rowsInput.getTextField();
    colsField = colsInput.getTextField();
  }

  private void closePopup() { root.fireEvent(new HidePopupEvent()); }

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
