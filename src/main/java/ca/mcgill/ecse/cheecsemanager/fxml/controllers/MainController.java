package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.fxml.components.PopupManager;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class MainController {
  @FXML private StackPane popupRoot;
  @FXML private Region veil;
  @FXML private StackPane contentArea;
  @FXML private SidebarController sidebarController;

  // Cache for loaded pages
  private Map<String, Pane> pageCache = new HashMap<>();

  @FXML
  public void initialize() {
    // Set up navigation callback
    sidebarController.setNavigationCallback(this::loadPage);

    // Initialize PageNavigator with content area
    PageNavigator.getInstance().setContentArea(contentArea);

    // Load default page
    loadPage("shelves");

    PopupManager.getInstance().initialize(popupRoot, veil);
    popupRoot.addEventFilter(ShowPopupEvent.SHOW_POPUP, this::handleShowPopup);
    popupRoot.addEventFilter(HidePopupEvent.HIDE_POPUP, this::handleHidePopup);
  }

  private void handleShowPopup(ShowPopupEvent event) {
    try {
      // Load popup FXML
      FXMLLoader loader = new FXMLLoader(
          CheECSEManagerApplication.getResource("view/components/Popup.fxml"));
      Node popupContent = loader.load();

      // Configure popup controller
      PopupController controller = loader.getController();

      String fxml = event.getContent();
      String title = event.getTitle();

      if (title != null) {
        controller.setContent(fxml, title);
      } else {
        controller.setContent(fxml);
      }

      // Show popup
      PopupManager.getInstance().showPopup(popupContent);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void handleHidePopup(HidePopupEvent event) {
    PopupManager.getInstance().hidePopup();
  }

  private void loadPage(String pageName) {
    try {
      // Check cache first
      if (pageCache.containsKey(pageName)) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(pageCache.get(pageName));
        return;
      }

      String path = "view/page/" + pageName + "/page.fxml";
      // Load new page
      FXMLLoader loader =
          new FXMLLoader(CheECSEManagerApplication.getResource(path));
      Pane page = loader.load();

      // Cache the page
      pageCache.put(pageName, page);

      // Display the page
      contentArea.getChildren().clear();
      contentArea.getChildren().add(page);

    } catch (IOException e) {
      System.err.println("Error loading page: " + pageName);
      e.printStackTrace();

      // Show error page or placeholder
      showErrorPage(pageName);
    }
  }

  private void showErrorPage(String pageName) {
    javafx.scene.control.Label errorLabel =
        new javafx.scene.control.Label("Page not found: " + pageName);
    errorLabel.getStyleClass().add("error-label");
    contentArea.getChildren().clear();
    contentArea.getChildren().add(errorLabel);
  }
}
