package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
// import javafx.scene.layout.*;
import ca.mcgill.ecse.cheecsemanager.controller.RobotController;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ListView;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.beans.value.ChangeListener;
import java.util.Optional;
import java.util.List;
import ca.mcgill.ecse.cheecsemanager.controller.TOLogEntry; // Import your TO
import java.util.stream.Collectors;


// log-change listening (replaces polling)

public class RobotPageController {

    @FXML private StackPane activateTile;
    @FXML private StackPane deactivateTile;
    @FXML private StackPane initializeTile;
    @FXML private StackPane startTile;
    @FXML private ListView<String> telemetryLog;

    private record TreatmentRequest(int purchaseId, String maturationPeriod) {}

    private final BooleanProperty robotActive = new SimpleBooleanProperty(false);

    private final BooleanProperty treatmentActive = new SimpleBooleanProperty(false);

    private final BooleanProperty robotInitialized = new SimpleBooleanProperty(false);

    @FXML
    private void initialize() {
        bindPowerTiles();
        wireTileInteractions();
        
        // TODO: check if robot is already active on load (e.g., from saved state)
        robotActive.set(RobotController.isRobotActivated());
        treatmentActive.set(RobotController.isTreatmentActive());
        robotInitialized.set(RobotController.isRobotInitialized());

        // Listen for log updates (incremented by RobotController.logAction)
        RobotController.logVersionProperty().addListener((obs, oldV, newV) -> refreshLogs());

        // initial load
        refreshLogs();
    }

    private void bindPowerTiles() {
        activateTile.visibleProperty().bind(robotActive.not());
        activateTile.managedProperty().bind(robotActive.not());

        // LOGIC: Visible only if Robot is Active AND Treatment is NOT Active
        var canDeactivate = robotActive.and(treatmentActive.not());
        
        deactivateTile.disableProperty().bind(canDeactivate.not()); // disabled when cannot deactivate
        deactivateTile.visibleProperty().bind(robotActive);
        deactivateTile.managedProperty().bind(robotActive);

        var canInitialize = robotActive.and(treatmentActive.not());

        initializeTile.disableProperty().bind(canInitialize.not()); // disabled when cannot initialize

        var canStartTreatment = robotActive.and(robotInitialized).and(treatmentActive.not());

        startTile.disableProperty().bind(canStartTreatment.not()); // disabled when cannot start treatment
    }

    private void wireTileInteractions() {
        activateTile.setOnMouseClicked(event -> handleActivate());
        deactivateTile.setOnMouseClicked(event -> handleDeactivate());
        initializeTile.setOnMouseClicked(event -> handleInitialize());
        startTile.setOnMouseClicked(event -> handleTreatment());
    }

    private void handleActivate() {
        // TODO: send “power on” command to robot, log action, etc.
        RobotController.activateRobot();
        System.out.println("Robot activated.");
        robotActive.set(true);
    }

    private void handleDeactivate() {
        // TODO: send “power off” command, ensure safe shutdown, log action
        // RobotController.deactivateRobot();
        RobotController.deactivateRobot();
        System.out.println("Robot deactivated.");
        robotActive.set(false);
    }

    private void handleInitialize() {
        // TODO: 
        // RobotController.initializeRobot(pass something);
        System.out.println("Initializing robot...");
    }

    private void refreshLogs() {
        System.out.println("Refreshing robot's logs...");
        // 1. Get the list from the controller
        List<TOLogEntry> entries = RobotController.viewLog();
        
        // 2. Convert to easy-to-read strings (assuming TOLogEntry has getter)
        List<String> logMessages = entries.stream()
            .map(entry -> entry.getDescription()) 
            .collect(Collectors.toList());

        // 3. Update the ListView
        telemetryLog.getItems().setAll(logMessages);
        
        // 4. Auto-scroll to the newest entry
        // if (!logMessages.isEmpty()) {
        //     telemetryLog.scrollTo(logMessages.size() - 1);
        // }
    }

    private void handleTreatment() {
        showTreatmentDialog()
            .ifPresent(request -> {
                try {
                    RobotController.initializeTreatment(
                            request.purchaseId(), request.maturationPeriod());
                } catch (RuntimeException ex) {
                    // showError(ex.getMessage()); // your own alert helper
                    System.err.println("Error starting treatment: " + ex.getMessage());
                }
            });
    }

    private Optional<TreatmentRequest> showTreatmentDialog() {
        Dialog<TreatmentRequest> dialog = new Dialog<>();
        dialog.setTitle("Start Treatment");
        dialog.initModality(Modality.APPLICATION_MODAL);

        ButtonType startBtn = new ButtonType("Start", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(startBtn, ButtonType.CANCEL);

        TextField purchaseField = new TextField();
        purchaseField.setPromptText("Purchase ID");

        ComboBox<String> maturationBox = new ComboBox<>();
        maturationBox.getItems().setAll("Six", "Twelve", "TwentyFour", "ThirtySix");
        maturationBox.setPromptText("Maturation Period");

        GridPane content = new GridPane();
        content.setHgap(10);
        content.setVgap(12);
        content.addRow(0, new Label("Purchase ID:"), purchaseField);
        content.addRow(1, new Label("Maturation Period:"), maturationBox);
        dialog.getDialogPane().setContent(content);

        Node startButton = dialog.getDialogPane().lookupButton(startBtn);
        startButton.setDisable(true);

        ChangeListener<Object> validator = (obs, oldVal, newVal) -> {
            boolean ready = !purchaseField.getText().trim().isEmpty()
                    && maturationBox.getSelectionModel().getSelectedItem() != null;
            startButton.setDisable(!ready);
        };
        purchaseField.textProperty().addListener(validator);
        maturationBox.valueProperty().addListener(validator);

        dialog.setResultConverter(button -> {
            if (button == startBtn) {
                try {
                    int purchaseId = Integer.parseInt(purchaseField.getText().trim());
                    String period = maturationBox.getValue();
                    return new TreatmentRequest(purchaseId, period);
                } catch (NumberFormatException ex) {
                    // showError("Purchase ID must be a number.");
                    System.err.println("Invalid Purchase ID: " + ex.getMessage());
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }

}