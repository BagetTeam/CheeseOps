package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewShelfPopUpController implements Initializable {

    @FXML private Label shelfHeader;
    @FXML private GridPane shelfGrid;
    @FXML private Button closeBtn;

    private AnchorPane popupOverlay; // overlay added by main controller
    private ShelfController mainController;
    private String shelfID;

    public void setPopupOverlay(AnchorPane overlay) {
        this.popupOverlay = overlay;
    }

    public void setMainController(ShelfController controller) {
        this.mainController = controller;
    }

    public void setShelfToView(String shelfID) {
        this.shelfID = shelfID;
        shelfHeader.setText("Shelf " + shelfID);
        populateShelfGrid();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        closeBtn.setOnAction(e -> closePopup());
    }

    private void closePopup() {
        if (mainController != null && popupOverlay != null) {
            mainController.removePopup(popupOverlay);
        }
    }

    private void populateShelfGrid() {
        shelfGrid.getChildren().clear();

        List<TOCheeseWheel> allCheeses = CheECSEManagerFeatureSet3Controller.getCheeseWheels();

        int maxRow = 0;
        int maxCol = 0;
        for (TOCheeseWheel cheese : allCheeses) {
            if (shelfID.equals(cheese.getShelfID())) {
                maxRow = Math.max(maxRow, cheese.getRow());
                maxCol = Math.max(maxCol, cheese.getColumn());
            }
        }

        for (int r = 1; r <= maxRow; r++) {
            for (int c = 1; c <= maxCol; c++) {
                final int row = r;
                final int col = c;
                VBox cell = new VBox(5);
                cell.getStyleClass().add("grid-cell");

                TOCheeseWheel cheeseAtPos = allCheeses.stream()
                        .filter(ch -> shelfID.equals(ch.getShelfID()) && ch.getRow() == row && ch.getColumn() == col)
                        .findFirst()
                        .orElse(null);

                if (cheeseAtPos != null) {
                    ImageView cheeseImage = new ImageView(new Image(getClass().getResourceAsStream(
                            "/ca/mcgill/ecse/cheecsemanager/image/cheeseWheel.png")));
                    cheeseImage.setFitWidth(50);
                    cheeseImage.setFitHeight(50);
                    cell.getChildren().add(cheeseImage);

                    Label info = new Label(
                            "ID: " + cheeseAtPos.getId() +
                                    "\nAged: " + cheeseAtPos.getMonthsAged() +
                                    "\nSpoiled: " + cheeseAtPos.isIsSpoiled()
                    );
                    info.setStyle("-fx-font-size: 10px; -fx-text-alignment: center;");
                    cell.getChildren().add(info);
                } else {
                    Label placeholder = new Label("");
                    placeholder.setPrefSize(80, 80);
                    cell.getChildren().add(placeholder);
                }

                shelfGrid.add(cell, col - 1, row - 1);
            }
        }
    }
}