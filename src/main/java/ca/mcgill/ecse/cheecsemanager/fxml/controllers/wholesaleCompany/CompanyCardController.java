package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.WholesaleCompanyDataProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Reusable component displaying wholesale company details with action buttons.
 * Renders company name, address, and provides edit/delete callbacks.
 */
public class CompanyCardController {
  private final WholesaleCompanyDataProvider provider =
      WholesaleCompanyDataProvider.getInstance();

  @FXML private HBox companyCardRoot;
  @FXML private Label nameLabel;
  @FXML private Label soldCheeseLabel;
  @FXML private Label addressLabel;
  @FXML private Label ordersLabel;

  @FXML private StyledButton updateCompanyBtn;
  @FXML private StyledButton deleteCompanyBtn;

  private Integer companyIndex;
  private Runnable onDeleteCallback;

  public void init(Integer companyIndex, Runnable onView, Runnable onDelete,
                   boolean isInCompanyDetails) {
    setCompany(companyIndex);
    setOnDelete(onDelete);

    companyCardRoot.setOnMouseClicked(e -> onView.run());

    if (isInCompanyDetails) {
      provider.getCompanies().addListener(this::updateOrdersLabel);
    }

    companyCardRoot.sceneProperty().addListener((obs, oldScene, newScene) -> {
      if (newScene == null) {
        provider.getCompanies().removeListener(this::updateOrdersLabel);
      }
    });
  }

  private void
  updateOrdersLabel(javafx.collections.ListChangeListener
                        .Change<? extends TOWholesaleCompany> change) {
    if (provider.getCompanies().size() > companyIndex) {
      setCompany(companyIndex);
    }
  }

  /**
   * Populates the card with company information from a transfer object.
   *
   * @param company the wholesale company data to display
   */
  public void setCompany(Integer companyIndex) {
    this.companyIndex = companyIndex;
    var company = provider.getCompanies().get(companyIndex);

    nameLabel.setText(company.getName());
    addressLabel.setText(company.getAddress());
    soldCheeseLabel.setText("" + getNrCheeseWheelsOrdereds(company));

    int ordersCount = company.numberOfOrderDates();
    ordersLabel.setText("" + ordersCount);
  }

  private int getNrCheeseWheelsOrdereds(TOWholesaleCompany company) {
    Integer[] nrCheeseWheelsOrdereds = company.getNrCheeseWheelsOrdereds();
    Integer[] nrCheeseWheelsMissings = company.getNrCheeseWheelsMissings();

    int count = 0;
    for (int i = 0; i < nrCheeseWheelsOrdereds.length; i++) {
      count += nrCheeseWheelsOrdereds[i] - nrCheeseWheelsMissings[i];
    }
    return count;
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

    var company = provider.getCompanies().get(companyIndex);
    UpdateWholesaleCompanyController.currentCompanyName = company.getName();
    companyCardRoot.fireEvent(new ShowPopupEvent(
        "view/page/companies/UpdateWholesaleCompany.fxml", "Update Company"));
  }
}
