package ca.mcgill.ecse.cheecsemanager.fxml.controller.farmer;

import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.IdNodeFactory;
import ca.mcgill.ecse.cheecsemanager.fxml.state.AttributeInfo;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.state.TOForm;
import ca.mcgill.ecse.cheecsemanager.fxml.util.FormHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.LayoutHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class FarmerDisplayOneController implements Initializable {
  @FXML private GridPane gridPane;

  private TOFarmer farmer;
  private Supplier<TOFarmer> supplier;
  private Map<String, AttributeInfo> scope =
      Map.ofEntries(Map.entry("name", new AttributeInfo("BASIC", 0)),
          Map.entry("email", new AttributeInfo("BASIC", 1)),
          Map.entry("address", new AttributeInfo("BASIC", 3)));

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    refresh();
  }

  private void refresh() {
    if (supplier != null) {
      // Reloads data to ensure latest values are fetched each time page is opened using Back
      this.farmer = this.supplier.get();
    }

    if (this.farmer != null)
      populateData();
  }

  public void setData(Map<String, AttributeInfo> scope, Supplier<TOFarmer> supplier) {
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
      Text nameText = new Text(String.valueOf(farmer.getName()));
      pane.setId("name, 1");
      pane.getChildren().add(nameText);
      gridPane.add(pane, 1, 0);
      gridPane.getRowConstraints().add(row);
    }

    // Field - email
    if (scope.containsKey("email") && scope.get("email").getScope().equals("BASIC")) {
      Label emailLabel = new Label(FormHelper.convertCamelCaseToWords("email"));
      emailLabel.setId("email, 0");
      gridPane.add(emailLabel, 0, 1);
      StackPane pane = new StackPane();
      Text emailText = new Text(String.valueOf(farmer.getEmail()));
      pane.setId("email, 1");
      pane.getChildren().add(emailText);
      gridPane.add(pane, 1, 1);
      gridPane.getRowConstraints().add(row);
    }

    // Field - address
    if (scope.containsKey("address") && scope.get("address").getScope().equals("BASIC")) {
      Label addressLabel = new Label(FormHelper.convertCamelCaseToWords("address"));
      addressLabel.setId("address, 0");
      gridPane.add(addressLabel, 0, 3);
      StackPane pane = new StackPane();
      Text addressText = new Text(String.valueOf(farmer.getAddress()));
      pane.setId("address, 1");
      pane.getChildren().add(addressText);
      gridPane.add(pane, 1, 3);
      gridPane.getRowConstraints().add(row);
    }
    // Sort Grid
    int newRow = LayoutHelper.sortGrid(gridPane, scope);
    // Add Update Button
    Button updateButton = LayoutHelper.createGridEditButton(getClass());
    updateButton.setOnAction(this::updateFarmer);
    gridPane.add(updateButton, 1, newRow);
    gridPane.getRowConstraints().add(row);
  }

  @FXML
  public void updateFarmer(ActionEvent event) {
    NavigationState<TOForm<?, TOFarmer>> state =
        new NavigationState<>("Update Farmer", PageType.UPDATE, "view/page/farmer/FarmerForm.fxml");
    state.setData(new TOForm<>(farmer, PageType.UPDATE));
    gridPane.fireEvent(new PageSwitchEvent(state));
  }
}
