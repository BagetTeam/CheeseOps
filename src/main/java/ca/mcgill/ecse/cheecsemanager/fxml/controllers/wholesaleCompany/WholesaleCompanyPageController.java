package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class WholesaleCompanyPageController {
    
    @FXML
    private StackPane dialogContainer;
    
    @FXML
    private StackPane contentContainer;
    
    @FXML
    private HBox toastContainer;

    @FXML
    private Label toastLabel;
    
    @FXML
    private void handleViewCompanies() {
        try {
            FXMLLoader loader = new FXMLLoader(
                CheECSEManagerApplication.class.getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/page/wholesaleCompany/page.fxml"
                )
            );
            Parent page = loader.load();
            
            // Replace current content with the companies page
            // Assuming you have a content container in Main.fxml
            contentContainer.getChildren().clear();
            contentContainer.getChildren().add(page);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading WholesaleCompanies page: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddCompany() {
        try {
            // Load the AddWholesaleCompany.fxml dialog
            FXMLLoader loader = new FXMLLoader(
                CheECSEManagerApplication.class.getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/page/wholesaleCompany/AddWholesaleCompany.fxml"
                )
            );
            Parent dialog = loader.load();
            
            // Get controller of AddWholesaleCompanyController
            AddWholesaleCompanyController controller = loader.getController();
            controller.setMainController(this);
            controller.setOnClose(() -> closeDialog());

            showDialog(dialog);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading AddWholesaleCompany dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateCompany() {
        try {
            FXMLLoader loader = new FXMLLoader(
                CheECSEManagerApplication.class.getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/page/wholesaleCompany/UpdateWholesaleCompany.fxml"
                )
            );
            Parent dialog = loader.load();
            
            UpdateWholesaleCompanyController controller = loader.getController();
            controller.setMainController(this);
            controller.setCompany("Ewen's Company"); // TO CHANGE: pass selected company's name
            controller.setOnClose(() -> closeDialog());
            
            showDialog(dialog);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading UpdateWholesaleCompany dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteCompany() {
        try {
            FXMLLoader loader = new FXMLLoader(
                CheECSEManagerApplication.class.getResource(
                    "/ca/mcgill/ecse/cheecsemanager/view/page/wholesaleCompany/DeleteWholesaleCompany.fxml"
                )
            );
            Parent dialog = loader.load();
            
            DeleteWholesaleCompanyController controller = loader.getController();
            controller.setMainController(this); // Pass reference so it can show error dialog
            controller.setCompany("Ewen's Company");
            controller.setOnClose(() -> {
                closeDialog();
            });
            
            showDialog(dialog);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading DeleteWholesaleCompany dialog: " + e.getMessage());
        }
    }

    /**
     * Show a dialog in the dialogContainer
     */
    public void showDialog(Parent dialog) {
        dialogContainer.getChildren().clear();
        dialogContainer.getChildren().add(dialog);
        dialogContainer.setMouseTransparent(false);
    }

    /**
     * Close the current dialog
     */
    public void closeDialog() {
        dialogContainer.getChildren().clear();
        dialogContainer.setMouseTransparent(true);
    }

    /**
     * Show a success toast notification
     * @param message The success message to display
     */
    public void showSuccessToast(String message) {
      showToast(message, 3.0);
    }
        /**
     * Show a toast notification that fades away
     * @param message The message to display
     * @param durationSeconds How long to show before fading (in seconds)
     */
    
    public void showToast(String message, double durationSeconds) {
        toastLabel.setText(message);
        toastContainer.setVisible(true);
        toastContainer.setManaged(true);
        toastContainer.setOpacity(1.0);
        
        // Wait for specified duration, then fade out
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
