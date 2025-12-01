package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Input;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent.ToastType;
import ca.mcgill.ecse.cheecsemanager.fxml.store.WholesaleCompanyDataProvider;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the wholesale companies list page.
 * Displays all companies in a grid layout and provides navigation to
 * individual company views and the add company dialog.
 */
public class WholesaleCompanyPageController {
  private final WholesaleCompanyDataProvider dataProvider =
      WholesaleCompanyDataProvider.getInstance();

  @FXML private StackPane companiesPageRoot;
  @FXML private VBox cardsContainer;
  @FXML private Input searchInput;

  @FXML
  public void initialize() {
    loadCompanies();

    dataProvider.getCompanies().addListener(this::dataListenerChanged);
    searchInput.textProperty().addListener(this::inputListener);

    companiesPageRoot.sceneProperty().addListener((obs, oldScene, newScene) -> {
      if (newScene == null) {
        dataProvider.getCompanies().removeListener(this::dataListenerChanged);
        searchInput.textProperty().removeListener(this::inputListener);
      }
    });
  }

  /**
   * Loads all wholesale companies from the backend and renders them as cards.
   * Shows a placeholder message if no companies exist.
   */
  private void loadCompanies() {
    cardsContainer.getChildren().clear();

    List<TOWholesaleCompany> companies = dataProvider.getCompanies();
    String filter = searchInput.getText();

    for (TOWholesaleCompany company : companies) {
      if (filter.isEmpty() ||
          company.getName().toLowerCase().contains(filter)) {
        addCompanyCard(company);
      }
    }
  }

  private void dataListenerChanged(
      ListChangeListener.Change<? extends TOWholesaleCompany> change) {
    Platform.runLater(() -> { loadCompanies(); });
  }

  private void addCompanyCard(TOWholesaleCompany company) {
    FXMLLoader loader = new FXMLLoader(CheECSEManagerApplication.getResource(
        "view/components/Company/CompanyCard.fxml"));

    try {
      HBox card = loader.load();
      CompanyCardController controller = loader.getController();
      controller.init(company,
                      ()
                          -> handleViewCompany(company),
                      () -> handleDeleteCompanyCard(company));

      cardsContainer.getChildren().add(card);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void inputListener(ObservableValue<? extends String> observable,
                             String oldValue, String newValue) {
    loadCompanies();
  }

  /**
   * Navigates to the detailed view of a specific wholesale company.
   *
   * @param companyName the name of the company to view
   */
  private void handleViewCompany(TOWholesaleCompany company) {
    try {
      FXMLLoader loader = new FXMLLoader(CheECSEManagerApplication.getResource(
          "view/page/companies/ViewWholesaleCompany.fxml"));
      StackPane viewPage = loader.load();

      ViewWholesaleCompanyController controller = loader.getController();
      controller.setCompany(company.getName());
      controller.setOnBack(() -> navigateBackToCompaniesPage());

      // Replace this entire page with the view page
      companiesPageRoot.getChildren().add(viewPage);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * Navigates back to the companies page.
   */
  private void navigateBackToCompaniesPage() {
    try {
      FXMLLoader loader =
          new FXMLLoader(CheECSEManagerApplication.class.getResource(
              "/ca/mcgill/ecse/cheecsemanager/view/page/companies/page.fxml"));
      Parent companiesPage = loader.load();

      // Replace current content with companies page
      companiesPageRoot.getChildren().clear();
      companiesPageRoot.getChildren().add(companiesPage);
      AnchorPane.setTopAnchor(companiesPage, 0.0);
      AnchorPane.setBottomAnchor(companiesPage, 0.0);
      AnchorPane.setLeftAnchor(companiesPage, 0.0);
      AnchorPane.setRightAnchor(companiesPage, 0.0);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Opens the add company dialog and refreshes the list on successful creation.
   */
  @FXML
  private void handleAddCompany() {
    this.companiesPageRoot.fireEvent(
        new ShowPopupEvent("view/page/companies/AddWholesaleCompany.fxml"));
  }

  /**
   * Opens the delete company confirmation dialog for a specific company card.
   * Refreshes the companies list after successful deletion.
   *
   * @param company the wholesale company to delete
   */
  @FXML
  private void handleDeleteCompanyCard(TOWholesaleCompany company) {
    // try {
    //   FXMLLoader loader =
    //       new FXMLLoader(CheECSEManagerApplication.class.getResource(
    //           "/ca/mcgill/ecse/cheecsemanager/view/page/companies/"
    //           + "DeleteWholesaleCompany.fxml"));
    //   Parent dialog = loader.load();
    //
    //   DeleteWholesaleCompanyController controller = loader.getController();
    //   controller.setMainController(this);
    //   controller.setCompany(company.getName());
    //   controller.setOnClose(() -> {
    //     closeDialog();
    //     loadCompanies();
    //   });
    //
    //   showDialog(dialog);
    //
    // } catch (Exception e) {
    //   e.printStackTrace();
    // }
  }

  public void showToast(String message) {
    companiesPageRoot.fireEvent(new ToastEvent(message, ToastType.SUCCESS));
  }
}
