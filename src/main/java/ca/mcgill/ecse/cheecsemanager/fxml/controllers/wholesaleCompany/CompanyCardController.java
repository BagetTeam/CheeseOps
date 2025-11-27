package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.fxml.components.StyledButton;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Reusable component displaying wholesale company details with action buttons.
 * Renders company name, address, and provides edit/delete callbacks.
 */
public class CompanyCardController extends VBox {

    @FXML
    private Label nameLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label ordersLabel;

    @FXML private StyledButton updateCompanyBtn;
    @FXML private StyledButton deleteCompanyBtn;

    private Runnable onViewCallback;
    private Runnable onEditCallback;
    private Runnable onDeleteCallback;

    public CompanyCardController() {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
                "/ca/mcgill/ecse/cheecsemanager/view/components/Company/CompanyCard.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Populates the card with company information from a transfer object.
     *
     * @param company the wholesale company data to display
     */
    public void setCompany(TOWholesaleCompany company) {
        nameLabel.setText(company.getName());
        addressLabel.setText(company.getAddress());

        int ordersCount = company.numberOfOrderDates();
        ordersLabel.setText(ordersCount + " order" + (ordersCount != 1 ? "s" : ""));
    }

    public void setOnView(Runnable callback) {
        this.onViewCallback = callback;
    }

    /**
     * Registers a callback for the edit button action.
     *
     * @param callback the action to execute when edit is triggered
     */
    public void setOnEdit(Runnable callback) {
        this.onEditCallback = callback;
        updateCompanyBtn.setText("Edit");
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
    private void handleView() {
        if (onViewCallback != null) {
            onViewCallback.run();
        } else if (onEditCallback != null) {
            onEditCallback.run();
        }
    }

    @FXML
    private void handleDelete() {
        if (onDeleteCallback != null) {
            onDeleteCallback.run();
        }
    }
}