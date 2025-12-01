package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Reusable component displaying wholesale company details with action buttons.
 * Renders company name, address, and provides edit/delete callbacks.
 */
public class CompanyCardController {

  @FXML private HBox companyCardRoot;
  @FXML private Label nameLabel;
  @FXML private Label soldCheeseLabel;
  @FXML private Label addressLabel;
  @FXML private Label ordersLabel;

  @FXML private StyledButton updateCompanyBtn;
  @FXML private StyledButton deleteCompanyBtn;

  private TOWholesaleCompany company;
  private Runnable onDeleteCallback;

  public void init(TOWholesaleCompany company, Runnable onView,
                   Runnable onDelete) {
    setCompany(company);
    setOnDelete(onDelete);

    companyCardRoot.setOnMouseClicked(e -> onView.run());
  }

  /**
   * Populates the card with company information from a transfer object.
   *
   * @param company the wholesale company data to display
   */
  private void setCompany(TOWholesaleCompany company) {
    this.company = company;
    nameLabel.setText(company.getName());
    addressLabel.setText(company.getAddress());
    soldCheeseLabel.setText("" + (company.getNrCheeseWheelsOrdereds().length -
                                  company.getNrCheeseWheelsMissings().length));

    int ordersCount = company.numberOfOrderDates();
    ordersLabel.setText("" + ordersCount);
  }

  /**
   * Registers a callback for the delete button action.
   *
   * @param callback the action to execute when delete is triggered
   */
  public void setOnDelete(Runnable callback) {
    this.onDeleteCallback = callback;
  }

  @FXML
  private void handleDelete(ActionEvent e) {
    e.consume();
    if (onDeleteCallback != null) {
      onDeleteCallback.run();
    }
  }

  @FXML
  void handleEdit(ActionEvent e) {
    e.consume();
    UpdateWholesaleCompanyController.currentCompanyName =
        this.company.getName();
    companyCardRoot.fireEvent(new ShowPopupEvent(
        "view/page/companies/UpdateWholesaleCompany.fxml", "Update Company"));
  }
}
