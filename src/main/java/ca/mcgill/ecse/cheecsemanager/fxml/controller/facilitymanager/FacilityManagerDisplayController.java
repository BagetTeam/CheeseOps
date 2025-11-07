package ca.mcgill.ecse.cheecsemanager.fxml.controller.facilitymanager;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.fxml.basecontroller.BaseDisplayController;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class FacilityManagerDisplayController
    extends BaseDisplayController implements Initializable {
  @FXML public VBox parentContainer, childContainer;
  @FXML private Button buttonAdd;

  @Override
  protected Pane getChildContainer() {
    return childContainer;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    super.loadFXML("view/page/facilitymanager/FacilityManagerDisplayOne.fxml");
    // TODO Toggle Button Visibility based on availability of FacilityManager
    if (CheECSEManagerFeatureSet1Controller.getFacilityManager() != null) {
      buttonAdd.setVisible(false);
      buttonAdd.setManaged(false);
    }
  }

  public void onAdd(ActionEvent event) {
    NavigationState state = new NavigationState<>(
        "Add FacilityManager", PageType.ADD, "view/page/facilitymanager/FacilityManagerForm.fxml");
    parentContainer.fireEvent(new PageSwitchEvent(state));
  }
}
