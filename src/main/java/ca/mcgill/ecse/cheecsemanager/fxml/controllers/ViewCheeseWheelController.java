package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet4Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Icon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;

import java.io.IOException;

public class ViewCheeseWheelController {

    @FXML private AnchorPane rootPane;
    @FXML private Icon cheeseIcon;

    @FXML private Label idLabel;
    @FXML private Label monthsAgedLabel;
    @FXML private Label spoiledLabel;
    @FXML private Label purchaseDateLabel;
    @FXML private Label locationLabel;
    @FXML private Label orderedLabel;

    @FXML private HBox monthsAgedBox;
    @FXML private HBox columnBox;
    @FXML private HBox rowBox;

    @FXML private Label columnLabel;
    @FXML private Label rowLabel;

    @FXML private StyledButton removeButton;
    @FXML private StyledButton cancelBtn;
    @FXML private StyledButton editButton;

    private int cheeseWheelID;
    private ShelfController mainController;
    private AnchorPane popupOverlay;

    private boolean editing = false;
    private ComboBox<String> monthsAgedCombo;
    private TextField columnField;
    private TextField rowField;

    private ViewShelfPopUpController parentPopupController;

    public void setParentPopupController(ViewShelfPopUpController parent) {
        this.parentPopupController = parent;
    }

    public void setMainController(ShelfController controller) {
        this.mainController = controller;
    }

    public void setPopupOverlay(AnchorPane overlay) {
        this.popupOverlay = overlay;

        cancelBtn.setOnAction(e -> {
            if (mainController != null && popupOverlay != null) {
                mainController.removePopup(popupOverlay);
            }
        });
    }

    public void setCheeseToView(int id) {
        this.cheeseWheelID = id;

        loadCheeseWheel();
        initializeEditButton();
    }

    private void loadCheeseWheel() {
        TOCheeseWheel wheel = CheECSEManagerFeatureSet3Controller.getCheeseWheel(cheeseWheelID);
        if (wheel == null) return;

        idLabel.setText("ID: " + wheel.getId());
        monthsAgedLabel.setText("Months Aged: " + wheel.getMonthsAged());
        spoiledLabel.setText("Spoiled: " + (wheel.isIsSpoiled() ? "Yes" : "No"));
        purchaseDateLabel.setText("Purchase Date: " + wheel.getPurchaseDate());
        locationLabel.setText("Shelf ID: " + wheel.getShelfID());
        columnLabel.setText(String.valueOf(wheel.getColumn()));
        rowLabel.setText(String.valueOf(wheel.getRow()));
        orderedLabel.setText("Ordered: " + (wheel.isIsOrdered() ? "Yes" : "No"));

        // Load Icon FXML manually (because setIcon() doesn't load it)
        loadIconFXML("CheeseWheel");

        removeButton.setOnAction(e -> removeFromShelf());
    }

    private void loadIconFXML(String iconName) {
        if (cheeseIcon == null) return;

        var resource = CheECSEManagerApplication.getResource("view/icons/" + iconName + ".fxml");
        if (resource == null) {
            System.out.println("Icon FXML not found: " + iconName);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(resource);
            Node node = loader.load();
            cheeseIcon.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeEditButton() {
        monthsAgedCombo = new ComboBox<>();
        monthsAgedCombo.getItems().addAll("Six", "Twelve", "TwentyFour", "ThirtySix");
        editButton.setOnAction(e -> toggleEditMode());
    }

    private void toggleEditMode() {
        if (!editing) {
            editing = true;
            editButton.setText("Save");

            monthsAgedCombo.setValue(monthsAgedLabel.getText().replace("Months Aged: ", ""));
            monthsAgedBox.getChildren().remove(monthsAgedLabel);
            monthsAgedBox.getChildren().add(monthsAgedCombo);

            columnField = new TextField(columnLabel.getText());
            columnBox.getChildren().remove(columnLabel);
            columnBox.getChildren().add(columnField);

            rowField = new TextField(rowLabel.getText());
            rowBox.getChildren().remove(rowLabel);
            rowBox.getChildren().add(rowField);

        } else {
            String newMonths = monthsAgedCombo.getValue();
            int newColumn;
            int newRow;
            try {
                newColumn = Integer.parseInt(columnField.getText());
                newRow = Integer.parseInt(rowField.getText());
            } catch (NumberFormatException ex) {
                System.out.println("Column and Row must be numbers.");
                return;
            }

            String error = CheECSEManagerFeatureSet3Controller.updateCheeseWheel(
                    cheeseWheelID, newMonths, false
            );
            if (!error.isEmpty()) {
                System.out.println("Update error: " + error);
                return;
            }

            TOCheeseWheel wheel = CheECSEManagerFeatureSet3Controller.getCheeseWheel(cheeseWheelID);
            error = CheECSEManagerFeatureSet4Controller.assignCheeseWheelToShelf(
                    cheeseWheelID, wheel.getShelfID(), newColumn, newRow
            );
            if (!error.isEmpty()) {
                System.out.println("Shelf update error: " + error);
                return;
            }

            editing = false;
            editButton.setText("Edit");

            monthsAgedBox.getChildren().remove(monthsAgedCombo);
            monthsAgedLabel.setText("Months Aged: " + newMonths);
            monthsAgedBox.getChildren().add(monthsAgedLabel);

            columnBox.getChildren().remove(columnField);
            columnLabel.setText(String.valueOf(newColumn));
            columnBox.getChildren().add(columnLabel);

            rowBox.getChildren().remove(rowField);
            rowLabel.setText(String.valueOf(newRow));
            rowBox.getChildren().add(rowLabel);

            if (parentPopupController != null) {
                parentPopupController.refreshShelfGrid();
            }
        }
    }

    private void removeFromShelf() {
        String error = CheECSEManagerFeatureSet4Controller.removeCheeseWheelFromShelf(cheeseWheelID);
        if (!error.isBlank()) {
            System.out.println("REMOVE ERROR: " + error);
            return;
        }

        if (mainController != null && popupOverlay != null) {
            mainController.removePopup(popupOverlay);
        } else {
            Stage stage = (Stage) removeButton.getScene().getWindow();
            stage.close();
        }

        if (parentPopupController != null) {
            parentPopupController.refreshShelfGrid();
        }
    }
}