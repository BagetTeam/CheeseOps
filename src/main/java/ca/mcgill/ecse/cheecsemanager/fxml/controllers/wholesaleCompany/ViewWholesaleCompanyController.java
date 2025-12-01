package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import java.sql.Date;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * Controller for viewing and managing a wholesale company's details and orders.
 * Displays company information card, orders table, and provides actions for
 * editing, deleting, and placing new orders.
 */
public class ViewWholesaleCompanyController {
  @FXML private StackPane companyCardContainer;

  @FXML private TableView<Integer> ordersTable;
  @FXML private TableColumn<Integer, Date> transactionDateColumn;
  @FXML private TableColumn<Integer, String> monthsAgedColumn;
  @FXML private TableColumn<Integer, Integer> cheeseWheelsColumn;
  @FXML private TableColumn<Integer, Integer> missingWheelsColumn;
  @FXML private TableColumn<Integer, Date> deliveryDateColumn;

  private TOWholesaleCompany company;
  private Runnable onBackCallback;

  @FXML
  public void initialize() {
    Label placeholder = new Label("No orders yet");
    placeholder.setStyle("-fx-text-fill: -color-muted; -fx-font-size: 14px;");
    ordersTable.setPlaceholder(placeholder);
  }

  private void setupTableColumns() {
    transactionDateColumn.setCellValueFactory(
        cellData
        -> new SimpleObjectProperty<>(
            company.getOrderDate(cellData.getValue())));

    monthsAgedColumn.setCellValueFactory(
        cellData
        -> new SimpleStringProperty(
            company.getMonthsAged(cellData.getValue())));

    cheeseWheelsColumn.setCellValueFactory(
        cellData
        -> new SimpleIntegerProperty(
               company.getNrCheeseWheelsOrdered(cellData.getValue()))
               .asObject());

    missingWheelsColumn.setCellValueFactory(
        cellData
        -> new SimpleIntegerProperty(
               company.getNrCheeseWheelsMissing(cellData.getValue()))
               .asObject());

    deliveryDateColumn.setCellValueFactory(
        cellData
        -> new SimpleObjectProperty<>(
            company.getDeliveryDate(cellData.getValue())));
  }
  /**
   * Sets the company to be displayed and loads its order data.
   * Populates the company card and orders table with current information.
   *
   * @param companyName the name of the wholesale company to display
   */
  public void setCompany(String companyName, HBox companyCard) {
    this.companyCardContainer.getChildren().clear();
    this.companyCardContainer.getChildren().add(companyCard);

    // Fetch company details from backend
    this.company =
        CheECSEManagerFeatureSet6Controller.getWholesaleCompany(companyName);

    if (company != null) {
      setupTableColumns();
      loadOrders();
    }
  }

  private void loadOrders() {
    javafx.collections.ObservableList<Integer> indices =
        javafx.collections.FXCollections.observableArrayList();

    for (int i = 0; i < company.numberOfOrderDates(); i++) {
      indices.add(i);
    }

    ordersTable.setItems(indices);
  }
  /**
   * Registers a callback to execute when navigating back to the companies list.
   *
   * @param callback the action to run when back button is clicked
   */
  public void setOnBack(Runnable callback) { this.onBackCallback = callback; }

  @FXML
  private void handleBack() {
    if (onBackCallback != null) {
      onBackCallback.run();
    }
  }

  /**
   * Opens the delete company confirmation dialog and navigates back on success.
   */
  @FXML
  private void handleDelete() {}

  /**
   * Opens the order placement dialog for the current company.
   * Refreshes the orders table after successful order creation.
   */
  @FXML
  private void handlePlaceOrder() {
    try {
      FXMLLoader loader =
          new FXMLLoader(CheECSEManagerApplication.class.getResource(
              "/ca/mcgill/ecse/cheecsemanager/view/components/Company/"
              + "CompanyOrderPlacement.fxml"));
      Parent dialog = loader.load();

      // CompanyOrderPlacementController controller = loader.getController();
      // controller.setMainController(this);
      // controller.setCompany(companyName);
      // controller.setOnClose(() -> {
      //   closeDialog();
      //   setCompany(companyName);
      // });

      // showDialog(dialog);

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Error loading CompanyOrderPlacement dialog: " +
                         e.getMessage());
    }
  }
}
