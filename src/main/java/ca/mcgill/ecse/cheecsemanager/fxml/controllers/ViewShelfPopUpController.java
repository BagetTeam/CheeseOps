package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    private ShelfController mainController;
    private AnchorPane popupOverlay;

    private String shelfID;

    // ---------------------------------------------------------
    //              SETTERS USED BY ShelfController
    // ---------------------------------------------------------
    public void setMainController(ShelfController controller) {
        this.mainController = controller;
    }

    public void setPopupOverlay(AnchorPane overlay) {
        this.popupOverlay = overlay;
    }

    public void setShelfToView(String shelfID) {
        this.shelfID = shelfID;
        shelfHeader.setText("Shelf " + shelfID);
        populateShelfGrid();
    }

    // ---------------------------------------------------------
    // Public method to refresh the grid after edits
    // ---------------------------------------------------------
    public void refreshShelfGrid() {
        populateShelfGrid();
    }

    // ---------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        closeBtn.setOnAction(e -> closePopup());
    }

    private void closePopup() {
        mainController.removePopup(popupOverlay);
    }

    // ---------------------------------------------------------
    //              OPEN THE CHEESE-WHEEL POPUP
    // ---------------------------------------------------------
    private void showCheeseWheelPopup(int cheeseID) {
        try {
            mainController.applyBlur();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/components/Shelf/ViewCheeseWheel.fxml"
            ));
            AnchorPane popup = loader.load();
            AnchorPane overlay = mainController.buildOverlay(popup);

            ViewCheeseWheelController controller = loader.getController();
            controller.setMainController(mainController);
            controller.setPopupOverlay(overlay);
            controller.setCheeseToView(cheeseID);

            // Pass this popup controller to refresh grid after edits
            controller.setParentPopupController(this);

        } catch (Exception e) {
            e.printStackTrace();
            mainController.applyBlur(); // safe fallback
        }
    }

    // ---------------------------------------------------------
    //              POPULATE GRID
    // ---------------------------------------------------------
    private void populateShelfGrid() {

        shelfGrid.getChildren().clear();

        List<TOCheeseWheel> cheeses = CheECSEManagerFeatureSet3Controller.getCheeseWheels();

        int maxRow = 0;
        int maxCol = 0;

        for (TOCheeseWheel ch : cheeses) {
            if (shelfID.equals(ch.getShelfID())) {
                maxRow = Math.max(maxRow, ch.getRow());
                maxCol = Math.max(maxCol, ch.getColumn());
            }
        }

        // headers
        for (int c = 1; c <= maxCol; c++) {
            Label colLabel = new Label(String.valueOf(c));
            colLabel.setStyle("-fx-font-size: 12px; -fx-padding: 4; -fx-text-fill: #777;");
            shelfGrid.add(colLabel, c, 0);
        }

        for (int r = 1; r <= maxRow; r++) {
            Label rowLabel = new Label(String.valueOf(r));
            rowLabel.setStyle("-fx-font-size: 12px; -fx-padding: 4; -fx-text-fill: #777;");
            shelfGrid.add(rowLabel, 0, r);
        }

        // cells
        for (int r = 1; r <= maxRow; r++) {
            for (int c = 1; c <= maxCol; c++) {

                VBox cell = new VBox(5);
                cell.setStyle("-fx-border-color: #ccc; -fx-border-width: 0.5; -fx-alignment: center;");

                final int cellR = r;
                final int cellC = c;

                TOCheeseWheel cheese = cheeses.stream()
                        .filter(ch -> shelfID.equals(ch.getShelfID())
                                && ch.getRow() == cellR
                                && ch.getColumn() == cellC)
                        .findFirst()
                        .orElse(null);

                if (cheese != null) {
                    ImageView img = new ImageView(new Image(
                            getClass().getResourceAsStream("/ca/mcgill/ecse/cheecsemanager/image/cheeseWheel.png")
                    ));
                    img.setFitWidth(50);
                    img.setFitHeight(50);

                    Label info = new Label(
                            "ID: " + cheese.getId()
                    );
                    info.setStyle("-fx-font-size: 10px; -fx-text-fill: #777;");

                    cell.getChildren().addAll(img, info);

                    // CLICK opens popup
                    cell.setOnMouseClicked(e -> showCheeseWheelPopup(cheese.getId()));
                }

                shelfGrid.add(cell, c, r);
            }
        }
    }
}