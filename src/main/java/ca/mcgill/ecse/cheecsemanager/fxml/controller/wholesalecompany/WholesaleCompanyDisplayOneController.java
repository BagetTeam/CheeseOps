package ca.mcgill.ecse.cheecsemanager.fxml.controller.wholesalecompany;

import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.IdNodeFactory;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.TableColumnFactory;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.state.TOForm;
import ca.mcgill.ecse.cheecsemanager.fxml.util.FormHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.LayoutHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class WholesaleCompanyDisplayOneController implements Initializable {
  @FXML private GridPane gridPane;

  private TOWholesaleCompany wholesaleCompany;
  private Supplier<TOWholesaleCompany> supplier;
  private Map<String, AttributeInfo> scope =
      Map.ofEntries(Map.entry("name", new AttributeInfo("BASIC", 0)),
          Map.entry("address", new AttributeInfo("BASIC", 1)),
          Map.entry("monthsAgeds", new AttributeInfo("ID", 2)));

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    refresh();
  }

  private void refresh() {
    if (supplier != null) {
      // Reloads data to ensure latest values are fetched each time page is opened using Back
      this.wholesaleCompany = this.supplier.get();
    }

    if (this.wholesaleCompany != null)
      populateData();
  }

  public void setData(Map<String, AttributeInfo> scope, Supplier<TOWholesaleCompany> supplier) {
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

    // Field - name
    if (scope.containsKey("name") && scope.get("name").getScope().equals("BASIC")) {
      Label nameLabel = new Label(FormHelper.convertCamelCaseToWords("name"));
      nameLabel.setId("name, 0");
      gridPane.add(nameLabel, 0, 0);
      StackPane pane = new StackPane();
      Text nameText = new Text(String.valueOf(wholesaleCompany.getName()));
      pane.setId("name, 1");
      pane.getChildren().add(nameText);
      gridPane.add(pane, 1, 0);
      gridPane.getRowConstraints().add(row);
    }

    // Field - address
    if (scope.containsKey("address") && scope.get("address").getScope().equals("BASIC")) {
      Label addressLabel = new Label(FormHelper.convertCamelCaseToWords("address"));
      addressLabel.setId("address, 0");
      gridPane.add(addressLabel, 0, 1);
      StackPane pane = new StackPane();
      Text addressText = new Text(String.valueOf(wholesaleCompany.getAddress()));
      pane.setId("address, 1");
      pane.getChildren().add(addressText);
      gridPane.add(pane, 1, 1);
      gridPane.getRowConstraints().add(row);
    }

    // Column - orders
    if (scope.containsKey("monthsAgeds") && scope.get("monthsAgeds").getScope().equals("ID")) {
      Label monthsAgedsLabel = new Label(FormHelper.convertCamelCaseToWords("monthsAgeds"));
      monthsAgedsLabel.setId("orderDates, 0");
      gridPane.add(monthsAgedsLabel, 0, 2);
      String monthsAgedsText = FormHelper.isEmpty(wholesaleCompany.getMonthsAgeds())
          ? "-"
          : Arrays.stream(wholesaleCompany.getMonthsAgeds())
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
      Node purchasesNode = IdNodeFactory.createIdNode(monthsAgedsText, null);
      purchasesNode.setId("orderDates, 1");
      gridPane.add(purchasesNode, 1, 2);
      gridPane.getRowConstraints().add(row);
    }
    // Sort Grid
    int newRow = LayoutHelper.sortGrid(gridPane, scope);
    // Add Update Button
    Button updateButton = LayoutHelper.createGridEditButton(getClass());
    updateButton.setOnAction(this::updateWholesaleCompany);
    gridPane.add(updateButton, 1, newRow);
    gridPane.getRowConstraints().add(row);
  }

  @FXML
  public void updateWholesaleCompany(ActionEvent event) {
    NavigationState<TOForm<?, TOWholesaleCompany>> state =
        new NavigationState<>("Update WholesaleCompany", PageType.UPDATE,
            "view/page/wholesalecompany/WholesaleCompanyForm.fxml");
    state.setData(new TOForm<>(wholesaleCompany, PageType.UPDATE));
    gridPane.fireEvent(new PageSwitchEvent(state));
  }
}
