package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import ca.mcgill.ecse.cheecsemanager.controller.RobotController;

public class RobotHandlingController {

    @FXML private StackPane activateTile;
    @FXML private StackPane deactivateTile;
    @FXML private StackPane initializeTile;
    @FXML private StackPane startTile;        // if you need it later

    private final BooleanProperty robotActive = new SimpleBooleanProperty(false);

    @FXML
    private void initialize() {
        bindPowerTiles();
        wireTileInteractions();
        
        // TODO: check if robot is already active on load (e.g., from saved state)
        robotActive.set(RobotController.isRobotActivated());
    }

    private void bindPowerTiles() {
        activateTile.visibleProperty().bind(robotActive.not());
        activateTile.managedProperty().bind(robotActive.not());

        deactivateTile.visibleProperty().bind(robotActive);
        deactivateTile.managedProperty().bind(robotActive);

        initializeTile.disableProperty().bind(robotActive.not());
        startTile.disableProperty().bind(robotActive.not());
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

    private void handleTreatment() {
        // TODO: Create a modal to allow user to enter purchaseID (int) and choose a aging time (Six, Twelve, TwentyFour, or ThirtySix) and get user input
        // TODO: RobotController.initializeTreatment(pass something);
        System.out.println("Starting treatment...");
    }
}