package ca.mcgill.ecse.cheecsemanager.fxml.controllers;


import ca.mcgill.ecse.cheecsemanager.controller.RobotController;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

/** Legacy controller handling simple robot action buttons and animations. */
public class RobotHandlingController {

    public AnchorPane activateScreen;
    public AnchorPane initializeScreen;
    public AnchorPane deactivateScreen;

    public Label statusDisplay;

    public Button activateRobotButton;
    public Button initializeRobotButton;
    public Button deactivateRobotButton;
    public Button backToMenu;
    public TextField shelfIDInput;


    /** Activates the robot and plays the activation animation strip. */
    public void onActivateRobot(){
        try {
            RobotController.activateRobot();
            this.transition(activateScreen, "Status: Activated", "#92e090");
        }catch (Exception e){}
    }

    /** Initializes the robot using the provided shelf id text box. */
    public void onInitializeRobot(){
        try {
            //don't know what arg to take in initialize robot
            if (!shelfIDInput.getText().isEmpty()){
                RobotController.initializeRobot(shelfIDInput.getText());
                this.transition(initializeScreen, "Status: Initialized", "#ffda6d");
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Initialization Error");
                alert.setContentText("Error: no shelf ID provided for initialization.");
                alert.showAndWait();
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Initialization Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /** Deactivates the robot and plays the corresponding animation. */
    public void onDeactivateRobot(){
        try {
            RobotController.deactivateRobot();
            this.transition(deactivateScreen, "Status: Deactivated","#f07e68");
        }catch (Exception e){}
    }



    /**
     * Plays a fade in/out overlay while updating the displayed robot status.
     * @param coverPane pane to animate over the button grid
     * @param newStatus status string to show after the animation
     * @param paint color applied to the status label
     */
    public void transition(Pane coverPane, String newStatus, String paint){
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
            statusDisplay.setText(newStatus);
            statusDisplay.setTextFill(Paint.valueOf(paint));
        });

        fade2.setOnFinished(e ->{
            coverPane.setVisible(false);
        });
    }



    /** Navigates back to the main menu (implementation pending). */
    public void returnToMenu(){}
}
