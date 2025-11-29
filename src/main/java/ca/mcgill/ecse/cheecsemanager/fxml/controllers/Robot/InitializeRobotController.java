package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Robot;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Dropdown;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse.cheecsemanager.controller.RobotController.*;

import javafx.fxml.FXML;

public class InitializeRobotController {
  @FXML private Dropdown ShelfIdDropdown;
  @FXML private StyledButton startBtn;

  @FXML
  public void initialize() {
    bindings();

    List<String> items = List.of("67", "2", "3", "4", "5");
    ShelfIdDropdown.setItems(items);

  }

  private void bindings() {
    // Disable Start until fields are filled
    startBtn.disableProperty().bind(
        ShelfIdDropdown.getComboBox().valueProperty().isNull());

    startBtn.setOnAction(e -> submit());
  }

  private void submit() {
    String shelfIdVal = ShelfIdDropdown.getSelectedValue();

    System.out.println("YESSSIIIRIRRRR Shelf ID: " + shelfIdVal);
  }
}
