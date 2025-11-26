package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ViewWholesaleCompanyController implements ToastProvider {

    @FXML private CompanyCardController companyCard;
    @FXML private TableView<?> ordersTable;
    @FXML private StackPane dialogContainer;
    @FXML private HBox toastContainer;
    @FXML private Label toastLabel;

    private String companyName;
    private Runnable onBackCallback;

    @FXML
    public void initialize() {
        // Set up the card to use Edit instead of View
        companyCard.setOnEdit(this::handleEdit);
        companyCard.setOnDelete(this::handleDelete);
    }

    /**
     * Set the company to display
     */
    public void setCompany(String companyName) {
        this.companyName = companyName;

        // Fetch company details from backend
        TOWholesaleCompany company =
            CheECSEManagerFeatureSet6Controller.getWholesaleCompany(companyName);

        if (company != null) {
          companyCard.setCompany(company);
        // TODO: Populate orders table when order data is available
        }
    }
    
    /**
     * Set callback for back button navigation
     */
    public void setOnBack(Runnable callback) {
        this.onBackCallback = callback;
    }

    @FXML
    private void handleBack() {
      if (onBackCallback != null) {
        onBackCallback.run();
      }
    }

    @FXML
    private void handleEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(
                CheECSEManagerApplication.class.getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/page/companies/UpdateWholesaleCompany.fxml"));
            Parent dialog = loader.load();

            UpdateWholesaleCompanyController controller = loader.getController();
            controller.setMainController(this);
            controller.setCompany(companyName);
            controller.setOnClose((updatedName) -> {
              closeDialog();
              // Update stored name and refresh view
              if (updatedName != null && !updatedName.isEmpty()) {
                this.companyName = updatedName;
              }
              setCompany(this.companyName);
            });

            showDialog(dialog);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading UpdateWholesaleCompany dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        try {
            FXMLLoader loader = new FXMLLoader(
                CheECSEManagerApplication.class.getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/page/companies/DeleteWholesaleCompany.fxml"));
            Parent dialog = loader.load();

            DeleteWholesaleCompanyController controller = loader.getController();
            controller.setMainController(this);
            controller.setCompany(companyName);
            controller.setOnClose(() -> {
                // Go back to companies list after successful delete
                if (onBackCallback != null) {
                  onBackCallback.run();
                }
            });

            showDialog(dialog);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading DeleteWholesaleCompany dialog: " + e.getMessage());
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
        pause.setOnFinished(e -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(0.5), toastContainer);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(evt -> {
                toastContainer.setVisible(false);
                toastContainer.setManaged(false);
            });
            fade.play();
        });
        pause.play();
    }

}
