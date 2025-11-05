package ca.mcgill.ecse.cheecsemanager.fxml.controller.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet2Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
import ca.mcgill.ecse.cheecsemanager.fxml.basecontroller.DeleteDialogController;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ActionButtonCell;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ButtonCell;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.TableColumnFactory;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ToastFactory;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.state.TOForm;
import ca.mcgill.ecse.cheecsemanager.fxml.util.LayoutHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

public class ShelfDisplayManyController implements Initializable  {


    @FXML
    public VBox parentContainer;

	@FXML
	private TableView<TOShelf> table;

    private List<TOShelf> shelfList;
	private Supplier<List<TOShelf>> supplier;
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
        if (this.supplier != null) {
            shelfList = this.supplier.get();
        }
        else {
            shelfList = CheECSEManagerFeatureSet1Controller.getShelves();
        }
        if (shelfList == null || shelfList.isEmpty()) {
            shelfList = Collections.emptyList();
            table.setPlaceholder(new Label("Please add a new Shelf"));
        }
        populateData();
    }

    public void setData(Map<String, AttributeInfo> scope, Supplier<List<TOShelf>> supplier) {
        // Reloads data to ensure latest values are fetched each time page is opened using Back
        this.supplier = supplier;
        this.scope = scope;
        refresh();
    }

    public void populateData() {
        ObservableList<TOShelf> list = FXCollections.observableArrayList(shelfList);
        
        table.getColumns().clear();
        // Column - shelfID
        if (scope.containsKey("shelfID") && scope.get("shelfID").getScope().equals("BASIC")) {
            TableColumn<TOShelf, String> columnShelfID = TableColumnFactory.createTableColumn("shelfID");
            columnShelfID.setCellValueFactory(new PropertyValueFactory<>("shelfID"));
		    table.getColumns().add(columnShelfID);
        }
        // Column - maxColumns
        if (scope.containsKey("maxColumns") && scope.get("maxColumns").getScope().equals("BASIC")) {
            TableColumn<TOShelf, String> columnMaxColumns = TableColumnFactory.createTableColumn("maxColumns");
            columnMaxColumns.setCellValueFactory(new PropertyValueFactory<>("maxColumns"));
            table.getColumns().add(columnMaxColumns);
        }
        // Column - maxRows
        if (scope.containsKey("maxRows") && scope.get("maxRows").getScope().equals("BASIC")) {
            TableColumn<TOShelf, String> columnMaxRows = TableColumnFactory.createTableColumn("maxRows");
            columnMaxRows.setCellValueFactory(new PropertyValueFactory<>("maxRows"));
            table.getColumns().add(columnMaxRows);
        }
        // Initialize Col - Locations
        // TODO Redirect to ShelfLocation (if needed)
        if (scope.containsKey("idForLocations") && scope.get("idForLocations").getScope().equals("ALL")) {
            TableColumn<TOShelf, Button> columnLocations = TableColumnFactory.createTableColumn("idForLocations");
            columnLocations.setCellFactory(column -> new ButtonCell<>(this::redirectToShelfLocations, null));
            table.getColumns().add(columnLocations);
        }

		// Sort Columns
        LayoutHelper.sortTableColumns(table, scope);
        TableColumn<TOShelf, HBox> columnAction = TableColumnFactory.createTableColumn("");
        columnAction.setCellFactory(column -> new ActionButtonCell<>(
                null,
                this::showDialogDeleteShelf
        ));
        table.getColumns().add(columnAction);
        table.setItems(list);
		// Asynchronously refresh width after table is loaded
        Platform.runLater(() -> LayoutHelper.refreshColumnWidths(table));
    }

    private void showDialogDeleteShelf(TOShelf shelf) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PACKAGE_ID + "view/base/DeleteDialog.fxml"));
            Parent tempContainer = fxmlLoader.load();
            // Get controller
            DeleteDialogController<TOShelf> controller = fxmlLoader.getController();
            controller.setAction(a -> deleteShelf(shelf));
            Stage stage = new Stage();
            stage.setTitle("Delete");
			stage.initModality(Modality.APPLICATION_MODAL);
			Scene scene = new Scene(tempContainer);
			scene.getStylesheets().add(getClass().getResource(PACKAGE_ID.concat("style/main.css")).toExternalForm());
			stage.setScene(scene);
			stage.showAndWait();

			// Reinitialize data
        	refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteShelf(TOShelf shelf) {
        CheECSEManagerFeatureSet2Controller.deleteShelf(shelf.getShelfID());
        ToastFactory.createError(parentContainer, "Deleted Shelf successfully");
        System.out.println("Deleted Shelf Successfully");
    }

}
