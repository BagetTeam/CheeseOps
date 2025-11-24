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
import javafx.scene.Node;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewShelfPopUpController implements Initializable {

    @FXML private Label shelfHeader;
    @FXML private GridPane shelfGrid;
    @FXML private Button closeBtn;
    @FXML private Button assignCheeseButton;

    private ShelfController mainController;
    private AnchorPane popupOverlay;
    private String shelfID;

    public void setMainController(ShelfController controller) {
        this.mainController = controller;
    }

    public ShelfController getMainController() {
        return mainController;
    }

    public void setPopupOverlay(AnchorPane overlay) {
        this.popupOverlay = overlay;
    }

    public void setShelfToView(String shelfID) {
        this.shelfID = shelfID;
        shelfHeader.setText("Shelf " + shelfID);
        populateShelfGrid();
    }

    public void refreshShelfGrid() {
        populateShelfGrid();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        closeBtn.setOnAction(e -> closePopup());
        assignCheeseButton.setOnAction(e -> showAssignCheeseWheelPopup());
    }

    private void closePopup() {
        mainController.removePopup(popupOverlay);
    }

    private void showCheeseWheelPopup(int cheeseID) {
        try {
            mainController.applyBlur();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/components/Shelf/ViewCheeseWheel.fxml"
            ));
            Node popup = loader.load();

            AnchorPane overlay = mainController.buildOverlay(popup);

            ViewCheeseWheelController controller = loader.getController();
            controller.setMainController(mainController);
            controller.setPopupOverlay(overlay);
            controller.setCheeseToView(cheeseID);
            controller.setParentPopupController(this);

        } catch (Exception e) {
            e.printStackTrace();
            mainController.removeBlur();
        }
    }

    private void showAssignCheeseWheelPopup() {
        mainController.applyBlur();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/components/Shelf/AssignCheeseWheelPopUp.fxml"
            ));

            Node popup = loader.load();
            AnchorPane overlay = mainController.buildOverlay(popup);

            AssignCheeseWheelController controller = loader.getController();
            controller.setParentPopup(this);
            controller.setOverlay(overlay);

        } catch (Exception ex) {
            ex.printStackTrace();
            mainController.removeBlur();
        }
    }

    private void populateShelfGrid() {
        shelfGrid.getChildren().clear();
        List<TOCheeseWheel> cheeses = CheECSEManagerFeatureSet3Controller.getCheeseWheels();

        int maxRow = 0, maxCol = 0;
        for (TOCheeseWheel ch : cheeses) {
            if (shelfID.equals(ch.getShelfID())) {
                maxRow = Math.max(maxRow, ch.getRow());
                maxCol = Math.max(maxCol, ch.getColumn());
            }
        }

        // Column headers
        for (int c = 1; c <= maxCol; c++) {
            Label colLabel = new Label(String.valueOf(c));
            colLabel.setStyle("-fx-font-size: 12px; -fx-padding: 4; -fx-text-fill: #777;");
            shelfGrid.add(colLabel, c, 0);
        }

        // Row headers
        for (int r = 1; r <= maxRow; r++) {
            Label rowLabel = new Label(String.valueOf(r));
            rowLabel.setStyle("-fx-font-size: 12px; -fx-padding: 4; -fx-text-fill: #777;");
            shelfGrid.add(rowLabel, 0, r);
        }

        // Cells
        for (int r = 1; r <= maxRow; r++) {
            for (int c = 1; c <= maxCol; c++) {
                VBox cell = new VBox(5);
                cell.setStyle("-fx-border-color: #ccc; -fx-border-width: 0.5; -fx-alignment: center;");

                final int cellR = r, cellC = c;
                TOCheeseWheel cheese = cheeses.stream()
                        .filter(ch -> shelfID.equals(ch.getShelfID()) && ch.getRow() == cellR && ch.getColumn() == cellC)
                        .findFirst()
                        .orElse(null);

                if (cheese != null) {
                    ImageView img = new ImageView(new Image(
                            getClass().getResourceAsStream("/ca/mcgill/ecse/cheecsemanager/image/cheeseWheel.png")
                    ));
                    img.setFitWidth(50);
                    img.setFitHeight(50);
                    Label info = new Label("ID: " + cheese.getId());
                    info.setStyle("-fx-font-size: 10px; -fx-text-fill: #777;");
                    cell.getChildren().addAll(img, info);

                    cell.setOnMouseClicked(e -> showCheeseWheelPopup(cheese.getId()));
                }

                shelfGrid.add(cell, c, r);
            }
        }
    }
}