package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class RobotHandlingController {

    @FXML
    private StackPane activateTile;
    @FXML
    private StackPane deactivateTile;
    @FXML
    private StackPane initializeTile;
    @FXML
    private StackPane startTile;

    private final BooleanProperty robotActive = new SimpleBooleanProperty(false);

    @FXML
    private void initialize() {
        bindPowerTiles();
        wireTileInteractions();
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
        activateTile.setOnMouseClicked(event -> robotActive.set(true));
        deactivateTile.setOnMouseClicked(event -> robotActive.set(false));
    }
}