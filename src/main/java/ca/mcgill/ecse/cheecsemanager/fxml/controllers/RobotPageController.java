package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.fxml.controllers.Robot.ShelfIDPopUpController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
// import javafx.scene.layout.*;
import ca.mcgill.ecse.cheecsemanager.controller.RobotController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.beans.value.ChangeListener;
import java.util.Optional;
import java.util.List;
import ca.mcgill.ecse.cheecsemanager.controller.TOLogEntry; // Import your TO
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.stream.Collectors;


// log-change listening (replaces polling)

public class RobotPageController {


    public AnchorPane rootPane;
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

    @FXML
    private void handleActivate() {
        // TODO: send “power on” command to robot, log action, etc.
        try {
            RobotController.activateRobot();
            System.out.println("Robot activated.");
            robotActive.set(true);
        } catch (Exception e) {
            makeAlert("Activation Error", e);
        }
    }
    @FXML
    private void handleDeactivate() {
        // TODO: send “power off” command, ensure safe shutdown, log action
        try {
            RobotController.deactivateRobot();
            System.out.println("Robot deactivated.");
            robotActive.set(false);
            System.out.println("Robot deactivated.");
        } catch (Exception e) {
            makeAlert("Deactivation Error", e);
        }

    }
    @FXML
    private void handleInitialize() {
        // TODO:
        rootPane.setDisable(true);
        try {
            String id = openPopUp();
            if (id == null) {
                rootPane.setDisable(false);
                return;}
            RobotController.initializeRobot(id);
            System.out.println("Initializing robot...");
        } catch (Exception e) {
            makeAlert("Initialization Error", e);
        }
        rootPane.setDisable(false);
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
    @FXML
    private void handleTreatment() {
        showTreatmentDialog()
            .ifPresent(request -> {
                try {
                    RobotController.initializeTreatment(request.purchaseId(), request.maturationPeriod());
                } catch (RuntimeException ex) {
                    makeAlert("Treatment Error", ex);
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