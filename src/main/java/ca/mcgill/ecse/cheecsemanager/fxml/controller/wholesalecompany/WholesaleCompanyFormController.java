package ca.mcgill.ecse.cheecsemanager.fxml.controller.wholesalecompany;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet5Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
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

public class WholesaleCompanyFormController implements Initializable {
  @FXML private Label labelName;
  @FXML private TextField inputFieldName;
  @FXML private Label labelAddress;
  @FXML private TextField inputFieldAddress;

  @FXML private Button buttonCancel;
  @FXML private Button buttonSave;

  @FXML private Text textError;

  private TOWholesaleCompany currentWholesaleCompany;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // Initialize Label text with formatting
    labelName.setText(FormHelper.convertCamelCaseToWords("name"));
    // Initialize Label text with formatting
    labelAddress.setText(FormHelper.convertCamelCaseToWords("address"));
  }

  public void setData(TOForm<?, TOWholesaleCompany> toForm) {
    if (toForm.getPageType().equals(PageType.UPDATE)) {
      handleUpdateInit(toForm.getData());
    }
  }

  public void handleUpdateInit(TOWholesaleCompany wholesaleCompany) {
    this.currentWholesaleCompany = wholesaleCompany;

    inputFieldName.setText(wholesaleCompany.getName());
    inputFieldAddress.setText(wholesaleCompany.getAddress());

    // Mark Key field as Disabled
    inputFieldName.setDisable(true);

    System.out.println("Preloaded WholesaleCompany data");
  }

  @FXML
  private void onSave(ActionEvent event) {
    try {
      String name = inputFieldName.getText();
      String address = inputFieldAddress.getText();
      String savedStatus = "";
      // Perform Update Operation
      if (currentWholesaleCompany != null) {
        savedStatus =
            CheECSEManagerFeatureSet5Controller.updateWholesaleCompany(name, name, address);
      }
      // Perform Add Operation
      if (currentWholesaleCompany == null) {
        savedStatus = CheECSEManagerFeatureSet5Controller.addWholesaleCompany(name, address);
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
