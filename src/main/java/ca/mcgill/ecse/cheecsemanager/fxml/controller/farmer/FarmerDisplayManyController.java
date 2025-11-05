package ca.mcgill.ecse.cheecsemanager.fxml.controller.farmer;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import ca.mcgill.ecse.cheecsemanager.fxml.basecontroller.DeleteDialogController;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ActionButtonCell;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ButtonCell;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.TableColumnFactory;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ToastFactory;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.state.TOForm;
import ca.mcgill.ecse.cheecsemanager.fxml.util.FormHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.LayoutHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.util.TabSwitchEvent;
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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

public class FarmerDisplayManyController implements Initializable  {


    @FXML
    public VBox parentContainer;

	@FXML
	private TableView<TOFarmer> table;

    private List<TOFarmer> farmerList;
	private Supplier<List<TOFarmer>> supplier;
    private Map<String, AttributeInfo> scope = Map.ofEntries(
    			Map.entry("name", new AttributeInfo("BASIC", 0))
    ,			Map.entry("email", new AttributeInfo("BASIC", 1))
    ,			Map.entry("address", new AttributeInfo("BASIC", 3))
    );

	@Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
    }

    private void refresh() {
        if (this.supplier != null) {
            farmerList = this.supplier.get();
        }
        else {
            farmerList = CheECSEManagerFeatureSet7Controller.getFarmers();
        }
        if (farmerList == null || farmerList.isEmpty()) {
            farmerList = Collections.emptyList();
            table.setPlaceholder(new Label("Please add a new Farmer"));
        }
        populateData();
    }

    public void setData(Map<String, AttributeInfo> scope, Supplier<List<TOFarmer>> supplier) {
        // Reloads data to ensure latest values are fetched each time page is opened using Back
        this.supplier = supplier;
        this.scope = scope;
        refresh();
    }

    public void populateData() {
        ObservableList<TOFarmer> list = FXCollections.observableArrayList(farmerList);
        
        table.getColumns().clear();
        // Column - name
        if (scope.containsKey("name") && scope.get("name").getScope().equals("BASIC")) {
            TableColumn<TOFarmer, String> columnName = TableColumnFactory.createTableColumn("name");
            columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		    table.getColumns().add(columnName);
        }
        // Column - email
        if (scope.containsKey("email") && scope.get("email").getScope().equals("BASIC")) {
            TableColumn<TOFarmer, String> columnEmail = TableColumnFactory.createTableColumn("email");
            columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		    table.getColumns().add(columnEmail);
        }
        // Column - address
        if (scope.containsKey("address") && scope.get("address").getScope().equals("BASIC")) {
            TableColumn<TOFarmer, String> columnAddress = TableColumnFactory.createTableColumn("address");
            columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		    table.getColumns().add(columnAddress);
        }

		// Sort Columns
        LayoutHelper.sortTableColumns(table, scope);
        TableColumn<TOFarmer, HBox> columnAction = TableColumnFactory.createTableColumn("");
        columnAction.setCellFactory(column -> new ActionButtonCell<>(
                this::updateFarmer,
                this::showDialogDeleteFarmer
        ));
        table.getColumns().add(columnAction);
        table.setItems(list);
		// Asynchronously refresh width after table is loaded
        Platform.runLater(() -> LayoutHelper.refreshColumnWidths(table));
    }

	protected void updateFarmer(TOFarmer farmer) {
        NavigationState<TOForm<?, TOFarmer>> state = new NavigationState<>("Update Farmer", PageType.UPDATE, "view/page/farmer/FarmerForm.fxml");
        state.setData(new TOForm<>(farmer, PageType.UPDATE));
        parentContainer.fireEvent(new PageSwitchEvent(state));
    }

    private void showDialogDeleteFarmer(TOFarmer farmer) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PACKAGE_ID + "view/base/DeleteDialog.fxml"));
            Parent tempContainer = fxmlLoader.load();
            // Get controller
            DeleteDialogController<TOFarmer> controller = fxmlLoader.getController();
            controller.setAction(a -> deleteFarmer(farmer));
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

    private void deleteFarmer(TOFarmer farmer) {
        CheECSEManagerFeatureSet7Controller.deleteFarmer(farmer.getEmail());
        ToastFactory.createError(parentContainer, "Deleted Farmer successfully");
        System.out.println("Deleted Farmer Successfully");
    }

}
