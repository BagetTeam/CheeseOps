package ca.mcgill.ecse.cheecsemanager.fxml.controller.order;

import ca.mcgill.ecse.cheecsemanager.fxml.basecontroller.BaseDisplayController;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class OrderDisplayController extends BaseDisplayController implements Initializable {
  @FXML public VBox parentContainer, childContainer;
  @FXML private Button buttonAdd;

  @Override
  protected Pane getChildContainer() {
    return childContainer;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {}

  public void onAdd(ActionEvent event) {
    NavigationState state =
        new NavigationState<>("Add Order", PageType.ADD, "view/page/order/OrderForm.fxml");
    parentContainer.fireEvent(new PageSwitchEvent(state));
  }
}
