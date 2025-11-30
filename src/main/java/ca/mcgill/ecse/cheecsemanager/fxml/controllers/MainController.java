package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Animation.AnimationManager;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Animation.AnimationManager.MultiAnimationBuilder;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Animation.AnimationManager.NumericAnimationBuilder;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Animation.EasingInterpolators;
import ca.mcgill.ecse.cheecsemanager.fxml.components.PopupManager;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Toast;
import ca.mcgill.ecse.cheecsemanager.fxml.controllers.SidebarController.Page;
import ca.mcgill.ecse.cheecsemanager.fxml.events.HidePopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainController {
  @FXML private StackPane rootStackPane;
  @FXML private Region veil;
  @FXML private StackPane contentArea;
  @FXML private SidebarController sidebarController;
  @FXML private VBox toastContainer;

  // Cache for loaded pages
  private Map<Page, Pane> pageCache = new HashMap<>();
  private Page currentPage;
  private boolean isCurrentPageAnimating = false;
  private boolean isNewPageAnimating = false;

  private PopupManager popupManager = new PopupManager();

  private final Queue<ToastEvent> toastQueue = new LinkedList<>();

  @FXML
  public void initialize() {
    // Set up navigation callback
    sidebarController.setNavigationCallback(this::loadPage);

    // Initialize PageNavigator with content area
    PageNavigator.getInstance().setContentArea(contentArea);

    // Load default page
    loadPage(Page.SHELVES);

    this.popupManager.initialize(rootStackPane, veil);
    rootStackPane.addEventFilter(ShowPopupEvent.SHOW_POPUP,
                                 this::handleShowPopup);
    rootStackPane.addEventFilter(HidePopupEvent.HIDE_POPUP,
                                 this::handleHidePopup);

    /// === Toasts ===
    rootStackPane.addEventFilter(ToastEvent.TOAST_NOTIFICATION,
                                 this::handleToastEvent);
    // Configure toast container
    toastContainer.setPickOnBounds(false);    // Allow clicks to pass through
    toastContainer.setMouseTransparent(true); // Make container non-interactive
  }

  private void handleToastEvent(ToastEvent event) {
    event.consume(); // Prevent further propagation

    // Add to queue and try to show
    toastQueue.offer(event);
    showNextToast();
  }

  private void showNextToast() {
    if (toastQueue.isEmpty()) {
      return;
    }

    ToastEvent event = toastQueue.poll();

    // Create toast component
    Toast toast = new Toast(event.getMessage(), event.getType(), (t) -> {
      // Remove from container when animation finishes
      toastContainer.getChildren().remove(t);
      showNextToast(); // Show next in queue
    });

    // Add to container (at the top)
    toastContainer.getChildren().add(0, toast);

    // Start animation
    toast.show();
  }

  private void handleShowPopup(ShowPopupEvent event) {
    event.consume();
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
      this.popupManager.showPopup(popupContent);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void handleHidePopup(HidePopupEvent event) {
    event.consume();
    this.popupManager.hidePopup();
  }

  private boolean loadPage(Page pageName) {
    if (isCurrentPageAnimating || isNewPageAnimating) {
      return false;
    }
    try {
      // Check cache first
      Pane newPage;
      if (pageCache.containsKey(pageName)) {
        newPage = pageCache.get(pageName);
      } else {
        String path = "view/page/" + pageName + "/page.fxml";
        // Load new page
        FXMLLoader loader =
            new FXMLLoader(CheECSEManagerApplication.getResource(path));
        newPage = loader.load();

        // Cache the page
        pageCache.put(pageName, newPage);
      }
      contentArea.getChildren().add(newPage);

      double height = rootStackPane.getHeight();

      MultiAnimationBuilder currentPageAnimation = null;

      if (currentPage != null) {
        if (currentPage.ordinal() < pageName.ordinal()) {
          height *= -1;
        }
        Pane currentPagePane = pageCache.get(currentPage);

        currentPageAnimation =
            AnimationManager.multiBuilder()
                .addNumericTarget(currentPagePane.translateYProperty(), 0,
                                  height / 2)
                .addNumericTarget(currentPagePane.opacityProperty(), 1, 0)
                .durationMillis(300)
                .easing(EasingInterpolators.CUBIC_OUT)
                .onFinished(() -> {
                  contentArea.getChildren().remove(currentPagePane);
                  isCurrentPageAnimating = false;
                });
      }

      var newPageAnimation =
          AnimationManager.multiBuilder()
              .addNumericTarget(newPage.translateYProperty(), -height / 2, 0)
              .addNumericTarget(newPage.opacityProperty(), 0, 1)
              .durationMillis(300)
              .easing(EasingInterpolators.CUBIC_OUT)
              .onFinished(() -> {
                currentPage = pageName;
                isNewPageAnimating = false;
              });

      if (currentPageAnimation != null) {
        isCurrentPageAnimating = true;
        currentPageAnimation.play();
      }
      isNewPageAnimating = true;
      newPageAnimation.play();

      return true;
    } catch (IOException e) {
      System.err.println("Error loading page: " + pageName);
      e.printStackTrace();

      // Show error page or placeholder
      showErrorPage(pageName.name());
      return false;
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
