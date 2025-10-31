package ca.mcgill.ecse.cheecsemanager.fxml.controller.shelf;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet2Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOShelf;
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

public class ShelfFormController implements Initializable {

    @FXML
    private Label labelId;
    @FXML
    private TextField inputFieldId;
    @FXML
    private Label labelNrColumns;
    @FXML
    private TextField inputFieldNrColumns;
    @FXML
    private Label labelNrRows;
    @FXML
    private TextField inputFieldNrRows;

    @FXML
    private Button buttonCancel;
	@FXML
	private Button buttonSave;

    @FXML
    private Text textError;

    private TOShelf currentShelf;

	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		
        // Initialize Label text with formatting
        labelId.setText(FormHelper.convertCamelCaseToWords("id"));
        // Initialize Label text with formatting
        labelNrColumns.setText(FormHelper.convertCamelCaseToWords("nrColumns"));
        // Initialize Label text with formatting
        labelNrRows.setText(FormHelper.convertCamelCaseToWords("nrRows"));
	}

	public void setData(TOForm<?, TOShelf> toForm) {
		if (toForm.getPageType().equals(PageType.UPDATE)) {
			handleUpdateInit(toForm.getData());
		}
	}


    public void handleUpdateInit(TOShelf shelf) {
        this.currentShelf = shelf;

    	// TODO Preload data into form fields

		// Mark Key field as Disabled
		inputFieldId.setDisable(true);

        System.out.println("Preloaded Shelf data");
    }

    @FXML
    private void onSave(ActionEvent event) {
		try {
			String id = inputFieldId.getText();
			int nrColumns = Integer.parseInt(inputFieldNrColumns.getText());
			int nrRows = Integer.parseInt(inputFieldNrRows.getText());
			String savedStatus = "";
            // Perform Add Operation
            if (currentShelf == null) {
                savedStatus = CheECSEManagerFeatureSet2Controller.addShelf(
                    id, nrColumns, nrRows
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
