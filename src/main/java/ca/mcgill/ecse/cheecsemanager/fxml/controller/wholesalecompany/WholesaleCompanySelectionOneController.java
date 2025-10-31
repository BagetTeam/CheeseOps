package ca.mcgill.ecse.cheecsemanager.fxml.controller.wholesalecompany;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
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

public class WholesaleCompanySelectionOneController {

    private Consumer<String> confirmAction;

    
	@FXML
	private TableColumn<TOWholesaleCompany, String> columnName;
	@FXML
	private TableColumn<TOWholesaleCompany, String> columnAddress;
    @FXML
    private TableColumn<TOWholesaleCompany, RadioButton> columnSelect;

    @FXML
    private TableView<TOWholesaleCompany> table;

    @FXML
    private Button buttonCancel;

    private List<TOWholesaleCompany> list = new ArrayList<>();
    private TOWholesaleCompany selectedWholesaleCompany;

    public void setAction(Consumer<String> confirmAction, String selectionKey, Function<TOWholesaleCompany, Boolean> isSelectable) {
        this.confirmAction = confirmAction;
        list = CheECSEManagerFeatureSet6Controller.getWholesaleCompanies();
        if (list == null || list.isEmpty()) {
            list = Collections.emptyList();
            table.setPlaceholder(new Label("Please add a new WholesaleCompany"));
        }
		// Filter out items which are already mapped (In case of X..1 mappings on both side)
        if (isSelectable != null) list = list.stream().filter(isSelectable::apply).toList();
        ObservableList<TOWholesaleCompany> obList = FXCollections.observableArrayList(list);

        // Column - name
        columnName.setText(FormHelper.convertCamelCaseToWords("name"));
		columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        // Column - address
        columnAddress.setText(FormHelper.convertCamelCaseToWords("address"));
		columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        ToggleGroup toggleGroup = new ToggleGroup();
        if (!FormHelper.isEmpty(selectionKey)) {
            TOWholesaleCompany preSelection = list.stream().filter(it -> selectionKey.equals(it.getName())).findFirst().orElse(null);
            BiPredicate<TOWholesaleCompany, TOWholesaleCompany> selectionCondition = (a, b) -> a.getName() == b.getName();
            columnSelect.setCellFactory(columnSelect -> new RadioButtonCell<>(toggleGroup, this::setSelectedValue, selectionCondition, preSelection));
        } else {
            columnSelect.setCellFactory(columnSelect -> new RadioButtonCell<>(toggleGroup, this::setSelectedValue));
        }
        table.setItems(obList);
		// Resize columns
        LayoutHelper.refreshColumnWidths(table);
    }

    private void setSelectedValue(TOWholesaleCompany toWholesaleCompany) {
        selectedWholesaleCompany = toWholesaleCompany;
    }

    @FXML
    private void onCancel(ActionEvent event) {
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onDone(ActionEvent event) {
        confirmAction.accept(selectedWholesaleCompany.getName());
        onCancel(event);
    }
}
