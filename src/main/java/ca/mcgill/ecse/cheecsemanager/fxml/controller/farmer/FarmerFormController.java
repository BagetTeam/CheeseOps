package ca.mcgill.ecse.cheecsemanager.fxml.controller.farmer;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet3Controller;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet7Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOFarmer;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ToastFactory;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.state.TOForm;
import ca.mcgill.ecse.cheecsemanager.fxml.util.FormHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.InvalidInputException;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class FarmerFormController implements Initializable {

    @FXML
    private Label labelEmail;
    @FXML
    private TextField inputFieldEmail;
    @FXML
    private Label labelPassword;
    @FXML
    private TextField inputFieldPassword;
    @FXML
    private Label labelName;
    @FXML
    private TextField inputFieldName;
    @FXML
    private Label labelAddress;
    @FXML
    private TextField inputFieldAddress;

    @FXML
    private Button buttonCancel;
	@FXML
	private Button buttonSave;

    @FXML
    private Text textError;

    private TOFarmer currentFarmer;

	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

        // Initialize Label text with formatting
        labelName.setText(FormHelper.convertCamelCaseToWords("name"));
        // Initialize Label text with formatting
        labelEmail.setText(FormHelper.convertCamelCaseToWords("email"));
        // Initialize Label text with formatting
        labelPassword.setText(FormHelper.convertCamelCaseToWords("password"));
        // Initialize Label text with formatting
        labelAddress.setText(FormHelper.convertCamelCaseToWords("address"));
	}

	public void setData(TOForm<?, TOFarmer> toForm) {
		if (toForm.getPageType().equals(PageType.UPDATE)) {
			handleUpdateInit(toForm.getData());
		}
	}


    public void handleUpdateInit(TOFarmer farmer) {
        this.currentFarmer = farmer;

    	inputFieldEmail.setText(farmer.getEmail());
    	inputFieldPassword.setText(farmer.getPassword());
    	inputFieldAddress.setText(farmer.getAddress());

		// Mark Key field as Disabled
		inputFieldEmail.setDisable(true);

        System.out.println("Preloaded Farmer data");
    }

    @FXML
    private void onSave(ActionEvent event) {
		try {
			String email = inputFieldEmail.getText();
			String password = inputFieldPassword.getText();
			String name = inputFieldName.getText();
			String address = inputFieldAddress.getText();
			String savedStatus = "";
            // Perform Update Operation
            if (currentFarmer != null) {
              	savedStatus = CheECSEManagerFeatureSet7Controller.updateFarmer(
            		email, password, name, address
            	);
                
            } 
            // Perform Add Operation
            if (currentFarmer == null) {
                savedStatus = CheECSEManagerFeatureSet3Controller.registerFarmer(
                    email, password, name, address
                );
            }

			// Validate Controller response
			if (!savedStatus.isEmpty()) throw new InvalidInputException(savedStatus);
			else {
                textError.setText("");
				buttonSave.setDisable(true);
				ToastFactory.createSuccess(buttonSave, "Successfully saved item. Redirecting back to table...");
                FormHelper.triggerAfterDelay(() -> buttonCancel.fireEvent(new PageSwitchEvent(
						new NavigationState<>(null, PageType.BACK, null)
				)), 2);
			}

		} catch (RuntimeException e) {
            textError.setText(FormHelper.formatTextMessage(e));
		}
    }

    public void onCancel(ActionEvent actionEvent) {
        buttonCancel.fireEvent(new PageSwitchEvent(
				new NavigationState<>(null, PageType.BACK, null)
		));
    }

}
