package ca.mcgill.ecse.cheecsemanager.fxml.controllers;
import ca.mcgill.ecse.cheecsemanager.controller.RobotController;
import ca.mcgill.ecse.cheecsemanager.controller.TOLogEntry;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import java.util.List;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Controller for the Robot Page UI
 * Handles robot activation, deactivation, initialization, treatment start, and
 * log display
 * @author Benjamin & David
 */
public class RobotPageController {
  @FXML public StackPane rootPane;
  @FXML private StyledButton activateTile;
  @FXML private StyledButton deactivateTile;
  @FXML private StyledButton initializeTile;
  @FXML private StyledButton startTile;
  @FXML
  private TableView<ca.mcgill.ecse.cheecsemanager.controller.TOLogEntry>
      telemetryLogTable;
  @FXML
  private TableColumn<ca.mcgill.ecse.cheecsemanager.controller.TOLogEntry,
                      String> logColumn;

  private final SimpleStringProperty robotStatus = new SimpleStringProperty();

  /**
   * Initializes the controller, sets up bindings and event listeners for UI
   * components.
   * @author Benjamin
   */
  @FXML
  private void initialize() {
    // Set initial value so bindings and debug prints don't see null
    robotStatus.set(RobotController.getRobotStatus());

    bindPowerTiles();
    wireTileInteractions();

    // Listen for log updates (incremented by RobotController.logAction)
    RobotController.logVersionProperty().addListener((obs, oldV, newV) -> {
      refreshLogs();
      robotStatus.set(RobotController.getRobotStatus());
    });

    // Configure table column (show TOLogEntry.description)
    if (logColumn != null) {
      logColumn.setCellValueFactory(
          cell -> new ReadOnlyStringWrapper(cell.getValue().getDescription()));
      logColumn.setCellFactory(column -> new TableCell<>() {
        private final Text text = new Text();

        {
          text.wrappingWidthProperty().bind(
              column.widthProperty().subtract(16));
          // Use -fx-fill for Text nodes (not -fx-text-fill)
          text.setStyle("-fx-fill: -color-fg;");
          text.setTextAlignment(TextAlignment.LEFT);
          setGraphic(text);
          setAlignment(Pos.CENTER_LEFT);
          setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

          // Dynamically resize cell height based on text bounds (both grow AND
          // shrink)
          text.boundsInLocalProperty().addListener(
              (obs, oldBounds, newBounds) -> {
                if (newBounds == null) {
                  return;
                }
                double computedHeight = newBounds.getHeight() + 16;
                setPrefHeight(computedHeight);
              });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null || item.isEmpty()) {
            text.setText(null);
            setGraphic(null);
            setPrefHeight(Control.USE_COMPUTED_SIZE);
          } else {
            text.setText(item);
            setGraphic(text);
          }
        }
      });
    }

    // initial load
    refreshLogs();
  }

  /**
   * Binds the power tile buttons' visibility and disable properties based on
   * the robot's status.
   * @author Benjamin
   */
  private void bindPowerTiles() {
    System.out.println("STATUS:" + robotStatus.getValue());
    // Activate button: visible only if robot is Deactivated
    var canActivate = robotStatus.isEqualTo("Deactivated");
    activateTile.disableProperty().bind(canActivate.not());
    activateTile.visibleProperty().bind(canActivate);

    // Deactivate button: visible if robot is NOT Deactivated (i.e., active and
    // safe to deactivate)
    var canDeactivate = robotStatus.isEqualTo("Idle").or(
        robotStatus.isEqualTo("AtEntranceNotFacingAisle"));
    deactivateTile.disableProperty().bind(canDeactivate.not());
    deactivateTile.visibleProperty().bind(canDeactivate);

    // Initialize button: visible if robot is Idle (activated but not yet
    // initialized)
    var canInitialize = robotStatus.isEqualTo("Idle");
    initializeTile.disableProperty().bind(canInitialize.not());
    // initializeTile.managedProperty().bind(canInitialize);

    // Start treatment button: visible if robot is at entrance facing aisle or
    // at cheese wheel var canStartTreatment =
    // robotStatus.isNotEqualTo("AtEntranceFacingAisle")
    //         .or(robotStatus.isEqualTo("AtCheeseWheel"));
    var canStartTreatment =
        (robotStatus.isEqualTo("Idle").or(
             robotStatus.isEqualTo("AtEntranceFacingAisle")
                 .or(robotStatus.isEqualTo("AtCheeseWheel")
                         .or(robotStatus.isEqualTo("Deactivated")))))
            .not();
    startTile.disableProperty().bind(canStartTreatment.not());
    // startTile.managedProperty().bind(canStartTreatment);
  }

  private void wireTileInteractions() {
    activateTile.setOnMouseClicked(event -> handleActivate());
    deactivateTile.setOnMouseClicked(event -> handleDeactivate());
    initializeTile.setOnMouseClicked(event -> handleInitialize());
    startTile.setOnMouseClicked(event -> handleTreatment());
  }

  @FXML
  private void handleActivate() {
    try {
      RobotController.activateRobot();
      System.out.println("Robot activated.");
    } catch (RuntimeException e) {
      rootPane.fireEvent(new ToastEvent("Activation Error." + e.getMessage(),
                                        ToastEvent.ToastType.ERROR));
    }
  }

  @FXML
  private void handleDeactivate() {
    try {
      RobotController.deactivateRobot();
      System.out.println("Robot deactivated.");
    } catch (RuntimeException e) {
      rootPane.fireEvent(
          new ToastEvent("Deactivation Error.", ToastEvent.ToastType.ERROR));
    }
  }

  @FXML
  private void handleInitialize() {
    if (rootPane != null) {
      rootPane.fireEvent(
          new ShowPopupEvent("view/components/Robot/InitializeRobotPopUp.fxml",
                             "Initialize Robot"));
    }
  }

  /**
   * Refreshes the telemetry logs displayed in the TableView.
   * @author Benjamin
   */
  private void refreshLogs() {
    System.out.println("Refreshing robot's logs...");
    // 1. Get the list from the controller
    List<TOLogEntry> entries = RobotController.viewLog();
    // If the TableView is present in the scene, populate it with TOLogEntry
    // items.
    if (telemetryLogTable != null) {
      ObservableList<TOLogEntry> items =
          FXCollections.observableArrayList(entries);
      FXCollections.reverse(items);
      telemetryLogTable.setItems(items);
      if (!items.isEmpty()) {
        telemetryLogTable.scrollTo(0);
      }
    }
  }

  @FXML
  private void handleTreatment() {
    // Show the in-page popup (handled by MainController -> PopupManager)
    if (rootPane != null) {
      rootPane.fireEvent(new ShowPopupEvent(
          "view/components/Robot/TreatmentPopUp.fxml", "Initialize Robot"));
    }
  }
}
