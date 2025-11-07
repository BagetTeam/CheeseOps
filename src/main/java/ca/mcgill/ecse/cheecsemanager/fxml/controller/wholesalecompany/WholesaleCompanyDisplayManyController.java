package ca.mcgill.ecse.cheecsemanager.fxml.controller.wholesalecompany;

import static ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication.PACKAGE_ID;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
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
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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

public class WholesaleCompanyDisplayManyController implements Initializable {
  @FXML public VBox parentContainer;

  @FXML private TableView<TOWholesaleCompany> table;

  private List<TOWholesaleCompany> wholesaleCompanyList;
  private Supplier<List<TOWholesaleCompany>> supplier;
  private Map<String, AttributeInfo> scope =
      Map.ofEntries(Map.entry("name", new AttributeInfo("BASIC", 0)),
          Map.entry("address", new AttributeInfo("BASIC", 1)),
          Map.entry("orderDates", new AttributeInfo("ID", 2)),
          Map.entry("monthsAgeds", new AttributeInfo("ID", 3)));

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    refresh();
  }

  private void refresh() {
    if (this.supplier != null) {
      wholesaleCompanyList = this.supplier.get();
    } else {
      wholesaleCompanyList = CheECSEManagerFeatureSet6Controller.getWholesaleCompanies();
    }
    if (wholesaleCompanyList == null || wholesaleCompanyList.isEmpty()) {
      wholesaleCompanyList = Collections.emptyList();
      table.setPlaceholder(new Label("Please add a new WholesaleCompany"));
    }
    populateData();
  }

  public void setData(
      Map<String, AttributeInfo> scope, Supplier<List<TOWholesaleCompany>> supplier) {
    // Reloads data to ensure latest values are fetched each time page is opened using Back
    this.supplier = supplier;
    this.scope = scope;
    refresh();
  }

  public void populateData() {
    ObservableList<TOWholesaleCompany> list =
        FXCollections.observableArrayList(wholesaleCompanyList);

    table.getColumns().clear();
    // Column - name
    if (scope.containsKey("name") && scope.get("name").getScope().equals("BASIC")) {
      TableColumn<TOWholesaleCompany, String> columnName =
          TableColumnFactory.createTableColumn("name");
      columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
      table.getColumns().add(columnName);
    }
    // Column - address
    if (scope.containsKey("address") && scope.get("address").getScope().equals("BASIC")) {
      TableColumn<TOWholesaleCompany, String> columnAddress =
          TableColumnFactory.createTableColumn("address");
      columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
      table.getColumns().add(columnAddress);
    }
    // Column - address
    if (scope.containsKey("monthsAgeds") && scope.get("monthsAgeds").getScope().equals("ID")) {
      Function<TOWholesaleCompany, String> monthsAgedsIdExtractor = e
          -> FormHelper.isEmpty(e.getMonthsAgeds()) ? "-"
                                                    : Arrays.stream(e.getMonthsAgeds())
                                                          .map(String::valueOf)
                                                          .collect(Collectors.joining(", "));
      TableColumn<TOWholesaleCompany, TOWholesaleCompany> columnMonthsAged =
          TableColumnFactory.createIdColumn("monthsAgeds", monthsAgedsIdExtractor, null);
      table.getColumns().add(columnMonthsAged);
    }

    // Sort Columns
    LayoutHelper.sortTableColumns(table, scope);
    TableColumn<TOWholesaleCompany, HBox> columnAction = TableColumnFactory.createTableColumn("");
    columnAction.setCellFactory(column
        -> new ActionButtonCell<>(
            this::updateWholesaleCompany, this::showDialogDeleteWholesaleCompany));
    table.getColumns().add(columnAction);
    table.setItems(list);
    // Asynchronously refresh width after table is loaded
    Platform.runLater(() -> LayoutHelper.refreshColumnWidths(table));
  }

  protected void updateWholesaleCompany(TOWholesaleCompany wholesaleCompany) {
    NavigationState<TOForm<?, TOWholesaleCompany>> state =
        new NavigationState<>("Update WholesaleCompany", PageType.UPDATE,
            "view/page/wholesalecompany/WholesaleCompanyForm.fxml");
    state.setData(new TOForm<>(wholesaleCompany, PageType.UPDATE));
    parentContainer.fireEvent(new PageSwitchEvent(state));
  }

  private void showDialogDeleteWholesaleCompany(TOWholesaleCompany wholesaleCompany) {
    try {
      FXMLLoader fxmlLoader =
          new FXMLLoader(getClass().getResource(PACKAGE_ID + "view/base/DeleteDialog.fxml"));
      Parent tempContainer = fxmlLoader.load();
      // Get controller
      DeleteDialogController<TOWholesaleCompany> controller = fxmlLoader.getController();
      controller.setAction(a -> deleteWholesaleCompany(wholesaleCompany));
      Stage stage = new Stage();
      stage.setTitle("Delete");
      stage.initModality(Modality.APPLICATION_MODAL);
      Scene scene = new Scene(tempContainer);
      scene.getStylesheets().add(
          getClass().getResource(PACKAGE_ID.concat("style/main.css")).toExternalForm());
      stage.setScene(scene);
      stage.showAndWait();

      // Reinitialize data
      refresh();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void deleteWholesaleCompany(TOWholesaleCompany wholesaleCompany) {
    CheECSEManagerFeatureSet6Controller.deleteWholesaleCompany(wholesaleCompany.getName());
    ToastFactory.createError(parentContainer, "Deleted WholesaleCompany successfully");
    System.out.println("Deleted WholesaleCompany Successfully");
  }
}
