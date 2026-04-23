package ca.mcgill.ecse.cheecsemanager.fxml.controllers.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Icon;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Legacy popup controller for viewing a shelf using a grid layout.
 * @author Ayush Patel
 * */
public class ViewShelfPopUpController implements Initializable {

  @FXML private Label shelfHeader;
  @FXML private GridPane shelfGrid;
  @FXML private Button closeBtn;

  private ShelfController mainController;
  private AnchorPane popupOverlay;
  private String shelfID;

  /**
   * Registers the parent controller so this popup can call back into it.
   * @param controller owning shelf controller
   */
  public void setMainController(ShelfController controller) {
    this.mainController = controller;
  }

  /** @return reference to the parent controller */
  public ShelfController getMainController() { return mainController; }

  /** Saves the overlay that contains this popup. */
  public void setPopupOverlay(AnchorPane overlay) {
    this.popupOverlay = overlay;
  }

  /**
   * Sets which shelf to render and triggers a refresh.
   * @param shelfID identifier of the shelf being displayed
   */
  public void setShelfToView(String shelfID) {
    this.shelfID = shelfID;
    shelfHeader.setText("Shelf " + shelfID);
    populateShelfGrid();
  }

  /** Rebuilds the grid to reflect the latest cheese placements. */
  public void refreshShelfGrid() { populateShelfGrid(); }

  /** Hooks up the close button to dismiss the popup. */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    closeBtn.setOnAction(e -> closePopup());
  }

  /** Dismisses the popup overlay (implementation pending). */
  private void closePopup() {
    // mainController.removePopup(popupOverlay);
  }

  /** Opens a nested popup showing information about a specific cheese wheel. */
  private void showCheeseWheelPopup(int cheeseID) {
    // try {
    //   // mainController.applyBlur();
    //   FXMLLoader loader = new FXMLLoader(
    //       getClass().getResource("/ca/mcgill/ecse/cheecsemanager/view/"
    //                              + "components/Shelf/ViewCheeseWheel.fxml"));
    //   Node popup = loader.load();
    //
    //   AnchorPane overlay = mainController.buildOverlay(popup);
    //
    //   ViewCheeseWheelController controller = loader.getController();
    //   controller.setMainController(mainController);
    //   controller.setPopupOverlay(overlay);
    //   controller.setCheeseToView(cheeseID);
    //   controller.setParentPopupController(this);
    //
    // } catch (Exception e) {
    //   e.printStackTrace();
    //   // mainController.removeBlur();
    // }
  }

  /** Builds the visual grid that represents the selected shelf. */
  private void populateShelfGrid() {
    shelfGrid.getChildren().clear();

    List<TOCheeseWheel> cheeses =
        CheECSEManagerFeatureSet3Controller.getCheeseWheels();

    int maxRow = 0, maxCol = 0;
    for (TOCheeseWheel ch : cheeses) {
      if (shelfID.equals(ch.getShelfID())) {
        maxRow = Math.max(maxRow, ch.getRow());
        maxCol = Math.max(maxCol, ch.getColumn());
      }
    }

    if (maxRow == 0 || maxCol == 0)
      return;

    double gridWidth = shelfGrid.getWidth() > 0 ? shelfGrid.getWidth() : 500;
    double gridHeight = shelfGrid.getHeight() > 0 ? shelfGrid.getHeight() : 300;
    double hGap = shelfGrid.getHgap();
    double vGap = shelfGrid.getVgap();

    double cellWidth = (gridWidth - hGap * (maxCol - 1)) / maxCol;
    double cellHeight = (gridHeight - vGap * (maxRow - 1)) / maxRow;

    double iconScale = Math.min(cellWidth, cellHeight) / 220.0;

    for (int c = 1; c <= maxCol; c++) {
      Label colLabel = new Label(String.valueOf(c));
      colLabel.setStyle(
          "-fx-font-size: 12px; -fx-padding: 4; -fx-text-fill: #777;");
      shelfGrid.add(colLabel, c, 0);
      GridPane.setHalignment(colLabel, HPos.CENTER);
    }

    for (int r = 1; r <= maxRow; r++) {
      Label rowLabel = new Label(String.valueOf(r));
      rowLabel.setStyle(
          "-fx-font-size: 12px; -fx-padding: 4; -fx-text-fill: #777;");
      shelfGrid.add(rowLabel, 0, r);
      GridPane.setValignment(rowLabel, VPos.CENTER);
    }

    // Cells
    for (int r = 1; r <= maxRow; r++) {
      for (int c = 1; c <= maxCol; c++) {
        VBox cell = new VBox(5);
        cell.setAlignment(Pos.CENTER);
        cell.setPadding(new Insets(5));
        cell.setStyle("-fx-border-color: #ccc; -fx-border-width: 0.5;");

        final int cellR = r, cellC = c;
        TOCheeseWheel cheese =
            cheeses.stream()
                .filter(ch
                        -> shelfID.equals(ch.getShelfID()) &&
                               ch.getRow() == cellR && ch.getColumn() == cellC)
                .findFirst()
                .orElse(null);

        if (cheese != null) {
          Icon img = new Icon("CheeseWheel");
          img.setScaleX(iconScale);
          img.setScaleY(iconScale);

          Label info = new Label("ID: " + cheese.getId());
          info.setStyle("-fx-font-size: 10px; -fx-text-fill: #777;");

          cell.getChildren().addAll(img, info);
          cell.setOnMouseClicked(e -> showCheeseWheelPopup(cheese.getId()));
        }

        shelfGrid.add(cell, c, r);
        GridPane.setHalignment(cell, HPos.CENTER);
        GridPane.setValignment(cell, VPos.CENTER);
      }
    }
  }
}
