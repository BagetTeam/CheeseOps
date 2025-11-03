package ca.mcgill.ecse.cheecsemanager.fxml.controller.facilitymanager;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
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
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public class FacilityManagerDisplayOneController implements Initializable {

    @FXML
    private GridPane gridPane;

    String email;
    String password;

    private Map<String, AttributeInfo> scope = Map.ofEntries(
    			Map.entry("email", new AttributeInfo("BASIC", 0))
    ,			Map.entry("password", new AttributeInfo("BASIC", 1))
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
    }

    
    private void refresh() {
        // TODO Fetch data from nackend to display it on screen
        var manager = CheECSEManagerFeatureSet1Controller.getFacilityManager();
        email = manager.getEmail();
        password = manager.getPassword();
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

        // Field - email
        if (scope.containsKey("email") && scope.get("email").getScope().equals("BASIC")) {
            Label emailLabel = new Label(FormHelper.convertCamelCaseToWords("email"));
            emailLabel.setId("email, 0");
            gridPane.add(emailLabel, 0, 0);
            StackPane pane = new StackPane();
            Text emailText = new Text(email);
            pane.setId("email, 1");
            pane.getChildren().add(emailText);
            gridPane.add(pane, 1, 0);
            gridPane.getRowConstraints().add(row);
        }

        // Field - password
        if (scope.containsKey("password") && scope.get("password").getScope().equals("BASIC")) {
            Label passwordLabel = new Label(FormHelper.convertCamelCaseToWords("password"));
            passwordLabel.setId("password, 0");
            gridPane.add(passwordLabel, 0, 1);
            StackPane pane = new StackPane();
            Text passwordText = new Text(password);
            pane.setId("password, 1");
            pane.getChildren().add(passwordText);
            gridPane.add(pane, 1, 1);
            gridPane.getRowConstraints().add(row);
        }
        // Initializes Content depending on Layout scope of Active Page (Provided by previous page)
        
        initAllScope();
        // Sort Grid
        int newRow = LayoutHelper.sortGrid(gridPane, scope);
        // Add Update Button
        Button updateButton = LayoutHelper.createGridEditButton(getClass());
        updateButton.setOnAction(this::updateFacilityManager);
        gridPane.add(updateButton, 1, newRow);
        gridPane.getRowConstraints().add(row);
    }


    // Handle ALL Scope
    private void initAllScope() {
        RowConstraints row = new RowConstraints();
        row.setMinHeight(30);
        row.setVgrow(Priority.SOMETIMES);
    }
	
    @FXML
    public void updateFacilityManager(ActionEvent event) {
        NavigationState<TOForm<?, ?>> state = new NavigationState<>("Update FacilityManager", PageType.UPDATE, "view/page/facilitymanager/FacilityManagerForm.fxml");
        // TODO send the information of FacilityManager being displayed to the form
        state.setData(new TOForm<>(null, PageType.UPDATE));
        gridPane.fireEvent(new PageSwitchEvent(state));
    }

	
}
