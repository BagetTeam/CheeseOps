package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import ca.mcgill.ecse.cheecsemanager.fxml.events.ShowPopupEvent;
import ca.mcgill.ecse.cheecsemanager.fxml.store.OrdersProvider;
import ca.mcgill.ecse.cheecsemanager.fxml.store.OrdersProvider.Order;
import java.sql.Date;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
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
  private final OrdersProvider ordersProvider = OrdersProvider.getInstance();

  @FXML private StackPane root;
  @FXML private StackPane companyCardContainer;

  @FXML private TableView<Order> ordersTable;
  @FXML private TableColumn<Order, Date> transactionDateColumn;
  @FXML private TableColumn<Order, String> monthsAgedColumn;
  @FXML private TableColumn<Order, Integer> cheeseWheelsColumn;
  @FXML private TableColumn<Order, Integer> missingWheelsColumn;
  @FXML private TableColumn<Order, Date> deliveryDateColumn;

  private TOWholesaleCompany company;
  private Runnable onBackCallback;

  /** Configures table placeholders and basic UI state. */
  @FXML
  public void initialize() {
    Label placeholder = new Label("No orders yet");
    placeholder.setStyle("-fx-text-fill: -color-muted; -fx-font-size: 14px;");
    ordersTable.setPlaceholder(placeholder);
  }

  /** Initializes table column bindings and connects the shared data provider. */
  private void setupTableColumns() {
    transactionDateColumn.setCellValueFactory(
        cellData -> new SimpleObjectProperty<>(cellData.getValue().orderDate));

    monthsAgedColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().monthsAged));

    cheeseWheelsColumn.setCellValueFactory(
        cellData
        -> new SimpleIntegerProperty(cellData.getValue().nrCheeseWheelsOrdered)
               .asObject());

    missingWheelsColumn.setCellValueFactory(
        cellData
        -> new SimpleIntegerProperty(cellData.getValue().nrCheeseWheelsMissing)
               .asObject());

    deliveryDateColumn.setCellValueFactory(
        cellData
        -> new SimpleObjectProperty<>(cellData.getValue().deliveryDate));

    ordersTable.setItems(ordersProvider.getOrders());
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

    ordersProvider.setCompany(this.company.getName());

    if (this.company != null) {
      setupTableColumns();
    }
  }

  /**
   * Registers a callback to execute when navigating back to the companies list.
   *
   * @param callback the action to run when back button is clicked
   */
  public void setOnBack(Runnable callback) { this.onBackCallback = callback; }

  /** Invokes the optional callback to return to the list view. */
  @FXML
  private void handleBack() {
    if (onBackCallback != null) {
      onBackCallback.run();
    }
  }

  /**
   * Opens the order placement dialog for the current company.
   * Refreshes the orders table after successful order creation.
   */
  @FXML
  private void handlePlaceOrder() {
    try {
      CompanyOrderPlacementController.companyName = company.getName();
      root.fireEvent(new ShowPopupEvent(
          "view/components/Company/CompanyOrderPlacement.fxml", "New Order"));

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Error loading CompanyOrderPlacement dialog: " +
                         e.getMessage());
    }
  }
}
