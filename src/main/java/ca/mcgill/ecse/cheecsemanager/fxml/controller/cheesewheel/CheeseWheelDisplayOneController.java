package ca.mcgill.ecse.cheecsemanager.fxml.controller.cheesewheel;

import ca.mcgill.ecse.cheecsemanager.controller.TOCheeseWheel;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.state.TOForm;
import ca.mcgill.ecse.cheecsemanager.fxml.util.FormHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.LayoutHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public class CheeseWheelDisplayOneController implements Initializable {

    @FXML
    private GridPane gridPane;

    private TOCheeseWheel cheeseWheel;
	private Supplier<TOCheeseWheel> supplier;
    private Map<String, AttributeInfo> scope = Map.ofEntries(
            Map.entry("id", new AttributeInfo("BASIC", 0))
            ,Map.entry("monthsAged", new AttributeInfo("BASIC", 1))
            ,Map.entry("isSpoiled", new AttributeInfo("BASIC", 2))
            ,Map.entry("purchaseDate", new AttributeInfo("BASIC", 3))
            ,Map.entry("shelfID", new AttributeInfo("BASIC", 4))
            ,Map.entry("column", new AttributeInfo("BASIC", 5))
            ,Map.entry("row", new AttributeInfo("BASIC", 6))
            ,Map.entry("isOrdered", new AttributeInfo("BASIC", 7))
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
    }

    
    private void refresh() {
        if (supplier != null) {
            // Reloads data to ensure latest values are fetched each time page is opened using Back
            this.cheeseWheel = this.supplier.get();
        } 

        if (this.cheeseWheel != null) populateData();
    }

    public void setData(Map<String, AttributeInfo> scope, Supplier<TOCheeseWheel> supplier) {
        this.supplier = supplier;
        this.scope = scope;
        refresh();
    }

    public void populateData() {
		gridPane.getChildren().clear();
        RowConstraints row = new RowConstraints();
        row.setMinHeight(10);
        row.setPrefHeight(30);
        row.setVgrow(Priority.SOMETIMES);
        gridPane.getRowConstraints().add(row);


        // Field - id
        if (scope.containsKey("id") && scope.get("id").getScope().equals("BASIC")) {
            Label idLabel = new Label(FormHelper.convertCamelCaseToWords("id"));
            idLabel.setId("name, 0");
            gridPane.add(idLabel, 0, 0);
            StackPane pane = new StackPane();
            Text idText = new Text(String.valueOf(cheeseWheel.getId()));
            pane.setId("id, 1");
            pane.getChildren().add(idText);
            gridPane.add(pane, 1, 0);
            gridPane.getRowConstraints().add(row);
        }
        // Field - monthsAged
        if (scope.containsKey("monthsAged") && scope.get("monthsAged").getScope().equals("BASIC")) {
            Label monthsAgedLabel = new Label(FormHelper.convertCamelCaseToWords("monthsAged"));
            monthsAgedLabel.setId("monthsAged, 0");
            gridPane.add(monthsAgedLabel, 0, 1);
            StackPane pane = new StackPane();
            Text monthsAgedText = new Text(String.valueOf(cheeseWheel.getMonthsAged()));
            pane.setId("monthsAged, 1");
            pane.getChildren().add(monthsAgedText);
            gridPane.add(pane, 1, 1);
            gridPane.getRowConstraints().add(row);
        }
        // Field - monthsAged
        if (scope.containsKey("isSpoiled") && scope.get("isSpoiled").getScope().equals("BASIC")) {
            Label isSpoiledLabel = new Label(FormHelper.convertCamelCaseToWords("isSpoiled"));
            isSpoiledLabel.setId("isSpoiled, 0");
            gridPane.add(isSpoiledLabel, 0, 2);
            CheckBox box = new CheckBox();
            box.setSelected(cheeseWheel.getIsSpoiled());
            box.setMouseTransparent(true);
            box.setId("monthsAged, 1");
            gridPane.add(box, 1, 2);
            gridPane.getRowConstraints().add(row);
        }
        // Field - purchaseDate
        if (scope.containsKey("purchaseDate") && scope.get("purchaseDate").getScope().equals("BASIC")) {
            Label purchaseDateLabel = new Label(FormHelper.convertCamelCaseToWords("purchaseDate"));
            purchaseDateLabel.setId("purchaseDate, 0");
            gridPane.add(purchaseDateLabel, 0, 3);
            StackPane pane = new StackPane();
            Text purchaseDateText = new Text(String.valueOf(cheeseWheel.getPurchaseDate()));
            pane.setId("purchaseDate, 1");
            pane.getChildren().add(purchaseDateText);
            gridPane.add(pane, 1, 3);
            gridPane.getRowConstraints().add(row);
        }
        // TODO Remaining Columns

        // Populate Basic Items
        // Sort Grid
        int newRow = LayoutHelper.sortGrid(gridPane, scope);
        // Add Update Button
        Button updateButton = LayoutHelper.createGridEditButton(getClass());
        updateButton.setOnAction(this::updateCheeseWheel);
        gridPane.add(updateButton, 1, newRow);
        gridPane.getRowConstraints().add(row);
    }

    // Handle ID Scope
    private void initIdScope() {
        RowConstraints row = new RowConstraints();
        row.setMinHeight(10);
        row.setPrefHeight(30);
        row.setVgrow(Priority.SOMETIMES);
    }

    // Handle ALL Scope
    private void initAllScope() {
        RowConstraints row = new RowConstraints();
        row.setMinHeight(30);
        row.setVgrow(Priority.SOMETIMES);
    }
	
    @FXML
    public void updateCheeseWheel(ActionEvent event) {
        NavigationState<TOForm<?, TOCheeseWheel>> state = new NavigationState<>("Update CheeseWheel", PageType.UPDATE, "view/page/cheesewheel/CheeseWheelForm.fxml");
        state.setData(new TOForm<>(cheeseWheel, PageType.UPDATE));
        gridPane.fireEvent(new PageSwitchEvent(state));
    }

	
}
