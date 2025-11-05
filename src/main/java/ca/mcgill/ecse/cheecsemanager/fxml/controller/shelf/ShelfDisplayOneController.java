package ca.mcgill.ecse.cheecsemanager.fxml.controller.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
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
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public class ShelfDisplayOneController implements Initializable {

    @FXML
    private GridPane gridPane;

    private TOShelf shelf;
	private Supplier<TOShelf> supplier;
    private Map<String, AttributeInfo> scope = Map.ofEntries(
            Map.entry("shelfID", new AttributeInfo("BASIC", 0))
            ,Map.entry("maxColumns", new AttributeInfo("BASIC", 1))
            ,Map.entry("maxRows", new AttributeInfo("BASIC", 2))
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
    }

    
    private void refresh() {
        if (supplier != null) {
            // Reloads data to ensure latest values are fetched each time page is opened using Back
            this.shelf = this.supplier.get();
        } 

        if (this.shelf != null) populateData();
    }

    public void setData(Map<String, AttributeInfo> scope, Supplier<TOShelf> supplier) {
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

        // Populate Basic Items
        // Col, row

        // Field - shelfID
        if (scope.containsKey("shelfID") && scope.get("shelfID").getScope().equals("BASIC")) {
            Label shelfIDLabel = new Label(FormHelper.convertCamelCaseToWords("shelfID"));
            shelfIDLabel.setId("shelfID, 0");
            gridPane.add(shelfIDLabel, 0, 0);
            StackPane pane = new StackPane();
            Text shelfIDText = new Text(String.valueOf(shelf.getShelfID()));
            pane.setId("shelfID, 1");
            pane.getChildren().add(shelfIDText);
            gridPane.add(pane, 1, 0);
            gridPane.getRowConstraints().add(row);
        }
        // Field - maxColumns
        if (scope.containsKey("maxColumns") && scope.get("maxColumns").getScope().equals("BASIC")) {
            Label maxColumnsLabel = new Label(FormHelper.convertCamelCaseToWords("maxColumns"));
            maxColumnsLabel.setId("maxColumns, 0");
            gridPane.add(maxColumnsLabel, 0, 1);
            StackPane pane = new StackPane();
            Text maxColumnsText = new Text(String.valueOf(shelf.getMaxColumns()));
            pane.setId("maxColumns, 1");
            pane.getChildren().add(maxColumnsText);
            gridPane.add(pane, 1, 1);
            gridPane.getRowConstraints().add(row);
        }
        // Field - maxRows
        if (scope.containsKey("maxRows") && scope.get("maxRows").getScope().equals("BASIC")) {
            Label maxRowsLabel = new Label(FormHelper.convertCamelCaseToWords("maxRows"));
            maxRowsLabel.setId("maxRows, 0");
            gridPane.add(maxRowsLabel, 0, 2);
            StackPane pane = new StackPane();
            Text maxRowsText = new Text(String.valueOf(shelf.getMaxRows()));
            pane.setId("maxRows, 1");
            pane.getChildren().add(maxRowsText);
            gridPane.add(pane, 1, 2);
            gridPane.getRowConstraints().add(row);
        }
        // Initializes Content depending on Layout scope of Active Page (Provided by previous page)
        // Sort Grid
        LayoutHelper.sortGrid(gridPane, scope);
    }
}
