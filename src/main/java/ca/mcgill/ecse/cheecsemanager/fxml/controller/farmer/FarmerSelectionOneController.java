package ca.mcgill.ecse.cheecsemanager.fxml.controller.farmer;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.RadioButtonCell;
import ca.mcgill.ecse.cheecsemanager.fxml.util.FormHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.LayoutHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

public class FarmerSelectionOneController {

    private Consumer<String> confirmAction;

    
	@FXML
	private TableColumn<TOFarmer, String> columnName;
	@FXML
	private TableColumn<TOFarmer, String> columnEmail;
	@FXML
	private TableColumn<TOFarmer, String> columnAddress;
    @FXML
    private TableColumn<TOFarmer, RadioButton> columnSelect;

    @FXML
    private TableView<TOFarmer> table;

    @FXML
    private Button buttonCancel;

    private List<TOFarmer> list = new ArrayList<>();
    private TOFarmer selectedFarmer;

    public void setAction(Consumer<String> confirmAction, String selectionKey, Function<TOFarmer, Boolean> isSelectable) {
        this.confirmAction = confirmAction;
        list = CheECSEManagerFeatureSet7Controller.getFarmers();
        if (list == null || list.isEmpty()) {
            list = Collections.emptyList();
            table.setPlaceholder(new Label("Please add a new Farmer"));
        }
		// Filter out items which are already mapped (In case of X..1 mappings on both side)
        if (isSelectable != null) list = list.stream().filter(isSelectable::apply).toList();
        ObservableList<TOFarmer> obList = FXCollections.observableArrayList(list);

        // Column - name
        columnName.setText(FormHelper.convertCamelCaseToWords("name"));
		columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        // Column - email
        columnEmail.setText(FormHelper.convertCamelCaseToWords("email"));
		columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        // Column - address
        columnAddress.setText(FormHelper.convertCamelCaseToWords("address"));
		columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        ToggleGroup toggleGroup = new ToggleGroup();
        if (!FormHelper.isEmpty(selectionKey)) {
            TOFarmer preSelection = list.stream().filter(it -> selectionKey.equals(it.getEmail())).findFirst().orElse(null);
            BiPredicate<TOFarmer, TOFarmer> selectionCondition = (a, b) -> a.getEmail() == b.getEmail();
            columnSelect.setCellFactory(columnSelect -> new RadioButtonCell<>(toggleGroup, this::setSelectedValue, selectionCondition, preSelection));
        } else {
            columnSelect.setCellFactory(columnSelect -> new RadioButtonCell<>(toggleGroup, this::setSelectedValue));
        }
        table.setItems(obList);
		// Resize columns
        LayoutHelper.refreshColumnWidths(table);
    }

    private void setSelectedValue(TOFarmer toFarmer) {
        selectedFarmer = toFarmer;
    }

    @FXML
    private void onCancel(ActionEvent event) {
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onDone(ActionEvent event) {
        confirmAction.accept(selectedFarmer.getEmail());
        onCancel(event);
    }
}
