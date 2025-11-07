package ca.mcgill.ecse.cheecsemanager.fxml.controller.facilitymanager;

import ca.mcgill.ecse.cheecsemanager.fxml.basecontroller.BaseController;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.BreadCrumbBar;

public class FacilityManagerController extends BaseController implements Initializable {
  @FXML public BreadCrumbBar<String> breadCrumbBar;

  @FXML public VBox parentContainer, childContainer;

  public FacilityManagerController() {}

  @Override
  protected BreadCrumbBar<String> getBreadcrumbBar() {
    return breadCrumbBar;
  }

  @Override
  protected Pane getChildContainer() {
    return childContainer;
  }

  @Override
  protected Pane getParentContainer() {
    return parentContainer;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    super.initializeBreadcrumbNavigation("FacilityManager");
    parentContainer.addEventHandler(PageSwitchEvent.PAGE_SWITCH, this::changePage);
    parentContainer.fireEvent(new PageSwitchEvent(new NavigationState<>("FacilityManager",
        PageType.DISPLAY, "view/page/facilitymanager/FacilityManagerDisplay.fxml")));
  }

  public void changePage(PageSwitchEvent event) {
    try {
      NavigationState<?> navigationState = event.getNavigationState();
      PageType type = navigationState.getPageType();
      if (type == PageType.BACK) {
        super.handleBack();
        return;
      }
      FXMLLoader loader = super.loadPage(navigationState);
      switch (type) {
        case DISPLAY -> super.handleDisplay();
        case ADD -> super.handleAdd(navigationState, loader);
        case UPDATE -> super.handleUpdate(navigationState, loader);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
