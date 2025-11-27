package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Controller for the wholesale companies list page.
 * Displays all companies in a grid layout and provides navigation to
 * individual company views and the add company dialog.
 */
public class WholesaleCompanyPageController implements ToastProvider {

    @FXML private AnchorPane companiesPageRoot;

    @FXML private FlowPane cardsContainer;

    @FXML private StackPane dialogContainer;

    @FXML private TextField searchField;

    @FXML private HBox toastContainer;

    @FXML private Label toastLabel;

    @FXML
    public void initialize() {
        loadCompanies();
    }

    /**
     * Loads all wholesale companies from the backend and renders them as cards.
     * Shows a placeholder message if no companies exist.
     */
    private void loadCompanies() {
        cardsContainer.getChildren().clear();

        List<TOWholesaleCompany> companies =
            CheECSEManagerFeatureSet6Controller.getWholesaleCompanies();

        for (TOWholesaleCompany company : companies) {
            CompanyCardController card = new CompanyCardController();
            card.setCompany(company);

            card.setOnView(() -> handleViewCompany(company));
            card.setOnDelete(() -> handleDeleteCompanyCard(company));

            cardsContainer.getChildren().add(card);
        }
    }

    /**
     * Navigates to the detailed view of a specific wholesale company.
     *
     * @param companyName the name of the company to view
     */
    private void handleViewCompany(TOWholesaleCompany company) {
        try {
            FXMLLoader loader =
                new FXMLLoader(
                    CheECSEManagerApplication.class.getResource(
                        "/ca/mcgill/ecse/cheecsemanager/view/page/companies/ViewWholesaleCompany.fxml"));
            Parent viewPage = loader.load();

            ViewWholesaleCompanyController controller = loader.getController();
            controller.setCompany(company.getName());
            controller.setOnBack(() -> navigateBackToCompaniesPage());

            // Replace this entire page with the view page
            companiesPageRoot.getChildren().clear();
            companiesPageRoot.getChildren().add(viewPage);
            AnchorPane.setTopAnchor(viewPage, 0.0);
            AnchorPane.setBottomAnchor(viewPage, 0.0);
            AnchorPane.setLeftAnchor(viewPage, 0.0);
            AnchorPane.setRightAnchor(viewPage, 0.0);

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
                new FXMLLoader(
                    CheECSEManagerApplication.class.getResource(
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
        try {
            FXMLLoader loader =
                new FXMLLoader(
                    CheECSEManagerApplication.class.getResource(
                        "/ca/mcgill/ecse/cheecsemanager/view/page/companies/AddWholesaleCompany.fxml"));
            Parent dialog = loader.load();

            AddWholesaleCompanyController controller = loader.getController();
            controller.setMainController(this);
            controller.setOnClose(
                () -> {
                    closeDialog();
                    loadCompanies();
                });

            showDialog(dialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the delete company confirmation dialog for a specific company card.
     * Refreshes the companies list after successful deletion.
     *
     * @param company the wholesale company to delete
     */
    @FXML
    private void handleDeleteCompanyCard(TOWholesaleCompany company) {
        try {
            FXMLLoader loader =
                new FXMLLoader(
                    CheECSEManagerApplication.class.getResource(
                        "/ca/mcgill/ecse/cheecsemanager/view/page/companies/DeleteWholesaleCompany.fxml"));
            Parent dialog = loader.load();

            DeleteWholesaleCompanyController controller = loader.getController();
            controller.setMainController(this);
            controller.setCompany(company.getName());
            controller.setOnClose(
                () -> {
                    closeDialog();
                    loadCompanies();
                });

            showDialog(dialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showDialog(Parent dialog) {
        dialogContainer.getChildren().clear();
        dialogContainer.getChildren().add(dialog);
        dialogContainer.setMouseTransparent(false);
    }

    @Override
    public void closeDialog() {
        dialogContainer.getChildren().clear();
        dialogContainer.setMouseTransparent(true);
    }

    @Override
    public void showSuccessToast(String message) {
        showToast(message, 3.0);
    }

    public void showToast(String message, double durationSeconds) {
        toastLabel.setText(message);
        toastContainer.setVisible(true);
        toastContainer.setManaged(true);
        toastContainer.setOpacity(1.0);

        PauseTransition pause = new PauseTransition(Duration.seconds(durationSeconds));
        pause.setOnFinished(
            e -> {
                FadeTransition fade = new FadeTransition(Duration.seconds(0.5), toastContainer);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.setOnFinished(
                    evt -> {
                        toastContainer.setVisible(false);
                        toastContainer.setManaged(false);
                    });
                fade.play();
            });
        pause.play();
    }
}