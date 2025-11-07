package ca.mcgill.ecse.cheecsemanager.fxml.basecontroller;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

import ca.mcgill.ecse.cheecsemanager.fxml.layout.BreadcrumbManager;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import java.io.IOException;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import org.controlsfx.control.BreadCrumbBar;

public abstract class BaseController {
  protected TreeItem<String> root = new TreeItem<>();
  protected BreadcrumbManager breadcrumbManager;

  protected abstract BreadCrumbBar<String> getBreadcrumbBar();

  protected abstract Pane getParentContainer();

  protected abstract Pane getChildContainer();

  public void initializeBreadcrumbNavigation(String rootPage) {
    this.root = new TreeItem<>(rootPage);
    breadcrumbManager = new BreadcrumbManager(getBreadcrumbBar(), getParentContainer());
  }

  public FXMLLoader loadPage(NavigationState<?> navigationState) throws IOException {
    if (!getChildContainer().getChildren().isEmpty()) {
      getChildContainer().getChildren().clear();
    }
    FXMLLoader loader =
        new FXMLLoader(getClass().getResource(PACKAGE_ID.concat(navigationState.getPageName())));
    Parent child = loader.load();
    getChildContainer().getChildren().add(child);
    breadcrumbManager.addNavItem(navigationState);
    return loader;
  }

  protected void handleDisplay() {
    getBreadcrumbBar().setSelectedCrumb(root);
  }

  protected void handleAdd(NavigationState<?> state, FXMLLoader loader) throws Exception {
    TreeItem<String> addItem = new TreeItem<>(state.getTitle());
    root.getChildren().add(addItem);
    getBreadcrumbBar().setSelectedCrumb(addItem);
    if (state.getData() == null)
      return;
    Object redirectAddController = loader.getController();
    Class<?> parameterType = state.getData().getClass();
    redirectAddController.getClass()
        .getMethod("setData", parameterType)
        .invoke(redirectAddController, state.getData());
  }

  protected void handleRedirectDisplay(NavigationState<?> state, FXMLLoader loader)
      throws Exception {
    TreeItem<String> redirectItem = new TreeItem<>(state.getTitle());
    getBreadcrumbBar().getSelectedCrumb().getChildren().add(redirectItem);
    getBreadcrumbBar().setSelectedCrumb(redirectItem);
    Object redirectController = loader.getController();
    /**
     * `setData` takes 3 params, Multiplicity, Attribute scope map, Actual data with templete
     * Different values of data are as follows
     * TOForm<?, ?> - non-root contained entities
     * Supplier<TO> - root-contained one multiplicity
     * Supplier<List<TO>> - root-contained many multiplicity
     */
    redirectController.getClass()
        .getMethod("setData", String.class, Map.class, Object.class)
        .invoke(redirectController, state.getMultiplicity(), state.getScope(), state.getData());
  }

  protected void handleUpdate(NavigationState<?> state, FXMLLoader loader) throws Exception {
    TreeItem<String> redirectUpdateItem = new TreeItem<>(state.getTitle());
    getBreadcrumbBar().getSelectedCrumb().getChildren().add(redirectUpdateItem);
    getBreadcrumbBar().setSelectedCrumb(redirectUpdateItem);
    Object redirectUpdateController = loader.getController();
    Class<?> parameterType = state.getData().getClass();
    redirectUpdateController.getClass()
        .getMethod("setData", parameterType)
        .invoke(redirectUpdateController, state.getData());
  }

  protected void handleBack() {
    // Removing 2 crumbs
    TreeItem<String> parent = getBreadcrumbBar().getSelectedCrumb().getParent();
    if (parent != null) {
      parent.getChildren().clear();
      if (parent.getParent() != null) {
        parent = parent.getParent();
        parent.getChildren().clear();
      }
    }
    breadcrumbManager.forceUpdateUi(parent);
    NavigationState<?> last = breadcrumbManager.removeLastAndGetSecondLast();
    // Adding new Crumb
    getParentContainer().fireEvent(new PageSwitchEvent(last));
  }
}
