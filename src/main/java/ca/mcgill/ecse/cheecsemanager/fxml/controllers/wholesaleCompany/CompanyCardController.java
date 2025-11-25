package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import java.io.IOException;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CompanyCardController extends VBox {
    @FXML
    private Label nameLabel;
    
    @FXML
    private Label addressLabel;
    
    @FXML
    private Label ordersLabel;
    
    @FXML
    private Button updateCompanyBtn;
    
    @FXML
    private Button deleteCompanyBtn;
    
    private TOWholesaleCompany company;
    
    public CompanyCardController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/ca/mcgill/ecse/cheecsemanager/view/components/wholesaleCompany/WholesaleCompanyCard.fxml"
        ));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void setCompany(TOWholesaleCompany company) {
        this.company = company;
        updateUI();
    }
    
    public TOWholesaleCompany getCompany() {
        return company;
    }
    
    private void updateUI() {
        if (company != null) {
            nameLabel.setText(company.getName());
            addressLabel.setText(company.getAddress());
            
            int orderCount = company.numberOfOrderDates();
            ordersLabel.setText(orderCount + (orderCount == 1 ? " order" : " orders"));
        }
    }
    
    public void setOnUpdate(Runnable callback) {
        updateCompanyBtn.setOnAction(e -> callback.run());
    }
    
    public void setOnDelete(Runnable callback) {
        deleteCompanyBtn.setOnAction(e -> callback.run());
    }
} 
