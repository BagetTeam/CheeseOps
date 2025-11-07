package ca.mcgill.ecse.cheecsemanager.fxml.controller.facilitymanager;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet1Controller;
import ca.mcgill.ecse.cheecsemanager.fxml.layout.ToastFactory;
import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.state.PageType;
import ca.mcgill.ecse.cheecsemanager.fxml.state.TOForm;
import ca.mcgill.ecse.cheecsemanager.fxml.util.FormHelper;
import ca.mcgill.ecse.cheecsemanager.fxml.util.InvalidInputException;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class FacilityManagerFormController implements Initializable {
  @FXML private Label labelEmail;
  @FXML private TextField inputFieldEmail;
  @FXML private Label labelPassword;
  @FXML private TextField inputFieldPassword;

  @FXML private Button buttonCancel;
  @FXML private Button buttonSave;

  @FXML private Text textError;

  private Object currentFacilityManager;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // Initialize Label text with formatting
    labelEmail.setText(FormHelper.convertCamelCaseToWords("email"));
    // Initialize Label text with formatting
    labelPassword.setText(FormHelper.convertCamelCaseToWords("password"));
  }

  public void setData(TOForm<?, Object> toForm) {
    if (toForm.getPageType().equals(PageType.UPDATE)) {
      // Object to store current Facility Manager
      currentFacilityManager = toForm;
      labelEmail.setVisible(false);
      labelEmail.setManaged(false);
      inputFieldEmail.setVisible(false);
      inputFieldEmail.setManaged(false);
    }
  }

  @FXML
  private void onSave(ActionEvent event) {
    try {
      String password = inputFieldPassword.getText();
      String email = inputFieldEmail.getText();
      String savedStatus = "";
      // Perform Update Operation
      if (currentFacilityManager != null) {
        savedStatus = CheECSEManagerFeatureSet1Controller.updateFacilityManager(password);

      } else {
        // TODO Perform Add Operation with both email and password
        savedStatus = CheECSEManagerFeatureSet1Controller.createFacilityManager(email, password);
      }

      // Validate Controller response
      if (!savedStatus.isEmpty())
        throw new InvalidInputException(savedStatus);
      else {
        textError.setText("");
        buttonSave.setDisable(true);
        ToastFactory.createSuccess(
            buttonSave, "Successfully saved item. Redirecting back to table...");
        FormHelper.triggerAfterDelay(()
                                         -> buttonCancel.fireEvent(new PageSwitchEvent(
                                             new NavigationState<>(null, PageType.BACK, null))),
            2);
      }

    } catch (RuntimeException e) {
      textError.setText(FormHelper.formatTextMessage(e));
    }
  }

  public void onCancel(ActionEvent actionEvent) {
    buttonCancel.fireEvent(new PageSwitchEvent(new NavigationState<>(null, PageType.BACK, null)));
  }
}
