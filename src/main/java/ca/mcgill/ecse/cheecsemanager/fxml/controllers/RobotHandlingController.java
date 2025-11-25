package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

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
        // robotActive.set(checkRobotInitialState());
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
        robotActive.set(true);
    }

    private void handleDeactivate() {
        // TODO: send “power off” command, ensure safe shutdown, log action
        robotActive.set(false);
    }

    private void handleInitialize() {
        if (!robotActive.get()) return; // guard if somehow triggered while inactive
        // TODO: run diagnostics / calibration routines
    }

    private void handleTreatment() {
        if (!robotActive.get()) return;
        // TODO: kick off treatment workflow
    }
}