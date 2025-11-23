package ca.mcgill.ecse.cheecsemanager.fxml.controllers;


import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class RobotHandlingController {

    public AnchorPane activateScreen;
    public AnchorPane initializeScreen;
    public AnchorPane deactivateScreen;

    public Label statusDisplay;

    public Button activateRobot;
    public Button initializeRobot;
    public Button deactivateRobot;

    public Button backToMenu;


    public void activateRobot(){
        this.transition(activateScreen);
        statusDisplay.setText("Status: Activated");

    }

    public void initializeRobot(){
        this.transition(initializeScreen);
        statusDisplay.setText("Status: Initialized");
    }

    public void deactivateRobot(){
        this.transition(deactivateScreen);
        statusDisplay.setText("Status: Deactivated");
    }



    public void transition(Pane coverPane){
        coverPane.setVisible(true);
        FadeTransition fade = new FadeTransition(new Duration(1500), coverPane);
        FadeTransition fade2 = new FadeTransition(new Duration(1500), coverPane);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();


        fade.setOnFinished(e -> {
            fade2.setFromValue(1);
            fade2.setToValue(0);
            fade2.play();
        });

        fade2.setOnFinished(e ->{
            coverPane.setVisible(false);
        });
    }



    public void returnToMenu(){}
}
