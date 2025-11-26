package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CompanyCardController extends VBox {

    @FXML
    private Label nameLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label ordersLabel;

    private Runnable onViewCallback;
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

    public void setCompany(TOWholesaleCompany company) {
        nameLabel.setText(company.getName());
        addressLabel.setText(company.getAddress());

        int ordersCount = company.numberOfOrderDates();
        ordersLabel.setText(ordersCount + " order" + (ordersCount != 1 ? "s" : ""));
    }

    public void setOnView(Runnable callback) {
        this.onViewCallback = callback;
    }

    public void setOnDelete(Runnable callback) {
        this.onDeleteCallback = callback;
    }

    @FXML
    private void handleView() {
        if (onViewCallback != null) {
            onViewCallback.run();
        }
    }

    @FXML
    private void handleDelete() {
        if (onDeleteCallback != null) {
            onDeleteCallback.run();
        }
    }
}