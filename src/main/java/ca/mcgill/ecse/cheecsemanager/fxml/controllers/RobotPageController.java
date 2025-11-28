package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.fxml.controllers.Robot.ShelfIDPopUpController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
// import javafx.scene.layout.*;
import ca.mcgill.ecse.cheecsemanager.controller.RobotController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import javafx.stage.Modality;

import java.util.List;
import ca.mcgill.ecse.cheecsemanager.controller.TOLogEntry; // Import your TO
import javafx.stage.Stage;
import javafx.stage.Window;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;

// removed unused imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ReadOnlyStringWrapper;


// log-change listening (replaces polling)

public class RobotPageController {


    public AnchorPane rootPane;
    @FXML private StyledButton activateTile;
    @FXML private StyledButton deactivateTile;
    @FXML private StyledButton initializeTile;
    @FXML private StyledButton startTile;
    @FXML private TableView<ca.mcgill.ecse.cheecsemanager.controller.TOLogEntry> telemetryLogTable;
    @FXML private TableColumn<ca.mcgill.ecse.cheecsemanager.controller.TOLogEntry, String> logColumn;

    // private final BooleanProperty robotActive = new SimpleBooleanProperty(false);

    // private final BooleanProperty treatmentActive = new SimpleBooleanProperty(false);

    // private final BooleanProperty robotInitialized = new SimpleBooleanProperty(false);

    private final SimpleStringProperty robotStatus = new SimpleStringProperty();

    @FXML
    private void initialize() {
        // Set initial value so bindings and debug prints don't see null
        robotStatus.set(RobotController.getRobotStatus());

        bindPowerTiles();
        wireTileInteractions();

        // TODO: check if robot is already active on load (e.g., from saved state)
        // robotActive.set(RobotController.isRobotActivated());
        // treatmentActive.set(RobotController.isTreatmentActive());
        // robotInitialized.set(RobotController.isRobotInitialized());

        // Listen for log updates (incremented by RobotController.logAction)
        RobotController.logVersionProperty().addListener((obs, oldV, newV) -> {
            refreshLogs();
            robotStatus.set(RobotController.getRobotStatus());
        });

        // Configure table column (show TOLogEntry.description)
        if (logColumn != null) {
            logColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getDescription()));
        }

        // initial load
        refreshLogs();
    }

    private void bindPowerTiles() {
        System.out.println("STATUS:" + robotStatus.getValue());
        // Activate button: visible only if robot is Deactivated
        var canActivate = robotStatus.isEqualTo("Deactivated");
        activateTile.disableProperty().bind(canActivate.not());
        activateTile.visibleProperty().bind(canActivate);

        // Deactivate button: visible if robot is NOT Deactivated (i.e., active and safe to deactivate)
        var canDeactivate = robotStatus.isEqualTo("Idle")
                     .or(robotStatus.isEqualTo("AtEntranceNotFacingAisle"));
        deactivateTile.disableProperty().bind(canDeactivate.not());
        deactivateTile.visibleProperty().bind(canDeactivate);

        // Initialize button: visible if robot is Idle (activated but not yet initialized)
        var canInitialize = robotStatus.isEqualTo("Idle");
        initializeTile.disableProperty().bind(canInitialize.not());
        // initializeTile.managedProperty().bind(canInitialize);

        // Start treatment button: visible if robot is at entrance facing aisle or at cheese wheel
        // var canStartTreatment = robotStatus.isNotEqualTo("AtEntranceFacingAisle")
        //         .or(robotStatus.isEqualTo("AtCheeseWheel"));
        var canStartTreatment = (robotStatus.isEqualTo("Idle")
                            .or(robotStatus.isEqualTo("AtEntranceFacingAisle")
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
            // robotStatus will update automatically via logVersionProperty listener
        } catch (Exception e) {
            makeAlert("Activation Error", e);
        }
    }

    @FXML
    private void handleDeactivate() {
        try {
            RobotController.deactivateRobot();
            System.out.println("Robot deactivated.");
            // robotStatus will update automatically via logVersionProperty listener
        } catch (Exception e) {
            makeAlert("Deactivation Error", e);
        }
    }

    @FXML
    private void handleInitialize() {
        rootPane.setDisable(true);
        try {
            String id = openPopUp();
            if (id == null) {
                rootPane.setDisable(false);
                return;
            }
            RobotController.initializeRobot(id);
            System.out.println("Initializing robot...");
            // robotStatus will update automatically via logVersionProperty listener
        } catch (Exception e) {
            makeAlert("Initialization Error", e);
        }
        rootPane.setDisable(false);
    }

    private void refreshLogs() {
        System.out.println("Refreshing robot's logs...");
        // 1. Get the list from the controller
        List<TOLogEntry> entries = RobotController.viewLog();
        // If the TableView is present in the scene, populate it with TOLogEntry items.
        if (telemetryLogTable != null) {
            ObservableList<TOLogEntry> items = FXCollections.observableArrayList(entries);
            telemetryLogTable.setItems(items);
            if (!items.isEmpty()) {
                telemetryLogTable.scrollTo(items.size() - 1);
            }
        }
    }
    
    @FXML
    private void handleTreatment() {
        // Show the in-page popup (handled by MainController -> PopupManager)
        if (rootPane != null) {
            rootPane.fireEvent(new ShowPopupEvent(
                "view/components/Robot/TreatmentPopUp.fxml", "Start Treatment"));
        }
    }

    // The treatment popup is now handled by the in-page popup system.
    // See `view/components/Robot/TreatmentPopUp.fxml` and
    // `TreatmentPopUpController` for the implementation.

    private void makeAlert(String title, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Initialization Error");
        alert.setContentText(e.getMessage());


        // Apply your CSS file
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/ca/mcgill/ecse/cheecsemanager/style/main.css").toExternalForm()

        );

        // Add a style class if you want specific styling
        //dialogPane.getStyleClass().add("popup-button");
        alert.showAndWait();
    }

    private String openPopUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/"
                            + "components/Robot/InitializeRobotPopUp.fxml"
            ));
            Parent popupRoot = loader.load();

            ShelfIDPopUpController controller = loader.getController();

            Stage popupStage = new Stage();
            popupStage.setTitle("Enter Shelf ID");
            popupStage.setScene(new Scene(popupRoot));

            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(rootPane.getScene().getWindow());

            controller.setStage(popupStage);

            //centering the popup
            popupStage.setOnShowing(e -> {
                Window parent = rootPane.getScene().getWindow();
                popupStage.setX(parent.getX() + parent.getWidth()/2 - popupStage.getWidth()/2);
                popupStage.setY(parent.getY() + parent.getHeight()/2 - popupStage.getHeight()/2);
            });

            popupStage.showAndWait();
            String shelfId = controller.getShelfIdEntered();
            return shelfId;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}