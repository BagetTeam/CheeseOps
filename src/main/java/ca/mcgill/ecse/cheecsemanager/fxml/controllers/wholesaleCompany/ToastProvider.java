package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import javafx.scene.Parent;

/**
 * Interface for controllers that can display dialogs and toast notifications.
 * Allows dialog controllers to communicate with their parent page controllers.
 */
public interface ToastProvider {

    /**
     * Display a success toast message to the user.
     * 
     * @param message The success message to display
     */
    void showSuccessToast(String message);

    /**
     * Display a dialog overlay on the page.
     * 
     * @param dialog The dialog content to display
     */
    void showDialog(Parent dialog);

    /**
     * Close the currently displayed dialog.
     */
    void closeDialog();
}