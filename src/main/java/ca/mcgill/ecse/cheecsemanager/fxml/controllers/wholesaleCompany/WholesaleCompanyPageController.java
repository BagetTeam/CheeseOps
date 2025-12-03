package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Animation.AnimationManager;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Animation.EasingInterpolators;
import ca.mcgill.ecse.cheecsemanager.fxml.components.Input;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ToastEvent.ToastType;
import ca.mcgill.ecse.cheecsemanager.fxml.store.WholesaleCompanyDataProvider;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the wholesale companies list page.
 * Displays all companies in a grid layout and provides navigation to
 * individual company views and the add company dialog.
 * @author Oliver Mao
 */
public class WholesaleCompanyPageController {
  private final WholesaleCompanyDataProvider dataProvider =
      WholesaleCompanyDataProvider.getInstance();

  @FXML private StackPane companiesPageRoot;
  @FXML private VBox cardsContainer;
  @FXML private Input searchInput;

  /** Wires up listeners and renders the initial set of company cards. */
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

    for (int i = 0; i < companies.size(); i++) {
      TOWholesaleCompany company = companies.get(i);
      if (filter.isEmpty() ||
          company.getName().toLowerCase().contains(filter)) {
        addCompanyCard(company, i);
      }
    }
  }

  /**
   * Rebuilds the cards whenever the underlying wholesale company list changes.
   */
  private void dataListenerChanged(
      ListChangeListener.Change<? extends TOWholesaleCompany> change) {
    Platform.runLater(() -> { loadCompanies(); });
  }

  /** Adds a single company card to the container at the given index. */
  private void addCompanyCard(TOWholesaleCompany company, int i) {
    HBox card = newCompanyCard(company, null, i);
    cardsContainer.getChildren().add(card);
  }

  /** Filters the visible cards as the search query changes. */
  private void inputListener(ObservableValue<? extends String> observable,
                             String oldValue, String newValue) {
    loadCompanies();
  }

  /**
   * Navigates to the detailed view of a specific wholesale company.
   *
   * @param companyName the name of the company to view
   */
  /**
   * Performs animated navigation into the detail view for a company.
   */
  private void handleViewCompany(TOWholesaleCompany company, int i) {
    try {
      var width = companiesPageRoot.getWidth();

      FXMLLoader loader = new FXMLLoader(CheECSEManagerApplication.getResource(
          "view/page/companies/ViewWholesaleCompany.fxml"));
      StackPane viewPage = loader.load();

      ViewWholesaleCompanyController controller = loader.getController();

      HBox card = newCompanyCard(company, viewPage, i);
      controller.setCompany(company.getName(), card);
      controller.setOnBack(() -> {
        AnimationManager.numericBuilder()
            .target(viewPage.translateXProperty())
            .from(0)
            .to(width)
            .durationMillis(500)
            .easing(EasingInterpolators.CUBIC_OUT)
            .onFinished(
                () -> { companiesPageRoot.getChildren().remove(viewPage); })
            .play();
      });

      // Replace this entire page with the view page
      companiesPageRoot.getChildren().add(viewPage);

      AnimationManager.numericBuilder()
          .target(viewPage.translateXProperty())
          .from(width)
          .to(0)
          .durationMillis(500)
          .easing(EasingInterpolators.CUBIC_OUT)
          .play();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Builds a company card configured with the correct callbacks.
   */
  private HBox newCompanyCard(TOWholesaleCompany company, StackPane viewPage,
                              int i) {
    FXMLLoader loader = new FXMLLoader(CheECSEManagerApplication.getResource(
        "view/components/Company/CompanyCard.fxml"));

    try {
      HBox card = loader.load();
      CompanyCardController controller = loader.getController();
      controller.init(i,
                      ()
                          -> {
                        if (viewPage == null)
                          handleViewCompany(company, i);
                      },
                      ()
                          -> { handleDeleteCompanyCard(company, viewPage); },
                      viewPage != null);

      return card;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Opens the add company dialog and refreshes the list on successful creation.
   */
  @FXML
  private void handleAddCompany() {
    this.companiesPageRoot.fireEvent(new ShowPopupEvent(
        "view/page/companies/AddWholesaleCompany.fxml", "Add Company"));
  }

  /**
   * Opens the delete company confirmation dialog for a specific company card.
   * Refreshes the companies list after successful deletion.
   *
   * @param company the wholesale company to delete
   */
  @FXML
  private void handleDeleteCompanyCard(TOWholesaleCompany company,
                                       StackPane viewPage) {
    DeleteWholesaleCompanyController.companyName = company.getName();

    if (viewPage != null) {
      DeleteWholesaleCompanyController.onDeleteCallback =
          () -> companiesPageRoot.getChildren().remove(viewPage);
    }

    this.companiesPageRoot.fireEvent(new ShowPopupEvent(
        "view/page/companies/DeleteWholesaleCompany.fxml", "Delete Company"));
  }

  /** Emits a success toast from this page. */
  public void showToast(String message) {
    companiesPageRoot.fireEvent(new ToastEvent(message, ToastType.SUCCESS));
  }
}
