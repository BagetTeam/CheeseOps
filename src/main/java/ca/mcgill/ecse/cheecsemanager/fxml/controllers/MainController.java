package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainController {

  @FXML private StackPane contentArea;

  @FXML private SidebarController sidebarController;

  // Cache for loaded pages
  private Map<String, Pane> pageCache = new HashMap<>();

  @FXML
  public void initialize() {
    // Set up navigation callback
    sidebarController.setNavigationCallback(this::loadPage);

    // Load default page
    loadPage("farmers");
  }

  private void loadPage(String pageName) {
    try {
      // Check cache first
      if (pageCache.containsKey(pageName)) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(pageCache.get(pageName));
        return;
      }

      // Load new page
      FXMLLoader loader = new FXMLLoader(CheECSEManagerApplication.getResource(
          "view/page/" + pageName + "/page.fxml"));
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
