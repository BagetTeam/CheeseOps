package ca.mcgill.ecse.cheecsemanager.fxml.controllers.Robot;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Dropdown;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.model.Shelf;
import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse.cheecsemanager.controller.RobotController;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;

import javafx.fxml.FXML;

public class InitializeRobotController {
  @FXML private Dropdown ShelfIdDropdown;
  @FXML private StyledButton startBtn;

  @FXML
  public void initialize() {
    bindings();

    List<String> items = CheECSEManagerFeatureSet1Controller.getAllShelfIds();
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
    
  }
}
