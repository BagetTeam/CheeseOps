package ca.mcgill.ecse.cheecsemanager.fxml.controller.cheecsemanager;

import ca.mcgill.ecse.cheecsemanager.fxml.util.TabSwitchEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class CheECSEManagerTabController {
  @FXML private TabPane tabPane;

  @FXML
  public void initialize() {
    // Register event handler
    tabPane.addEventHandler(TabSwitchEvent.SWITCH_TAB, event -> {
      tabPane.getTabs()
          .stream()
          .filter(tab -> tab.getText().equals(event.getTabName()))
          .findFirst()
          .ifPresent(tab -> tabPane.getSelectionModel().select(tab));
    });
  }
}
