package ca.mcgill.ecse.cheecsemanager.fxml.controllers.wholesaleCompany;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import ca.mcgill.ecse.cheecsemanager.controller.TOWholesaleCompany;
import java.sql.Date;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;

/**
 * Controller for viewing and managing a wholesale company's details and orders.
 * Displays company information card, orders table, and provides actions for
 * editing, deleting, and placing new orders.
 */
public class ViewWholesaleCompanyController implements ToastProvider {

  @FXML private CompanyCardController companyCard;
  @FXML private TableView<Integer> ordersTable;
  @FXML private TableColumn<Integer, Date> transactionDateColumn;
  @FXML private TableColumn<Integer, String> monthsAgedColumn;
  @FXML private TableColumn<Integer, Integer> cheeseWheelsColumn;
  @FXML private TableColumn<Integer, Integer> missingWheelsColumn;
  @FXML private TableColumn<Integer, Date> deliveryDateColumn;
  @FXML private StackPane dialogContainer;
  @FXML private HBox toastContainer;
  @FXML private Label toastLabel;

  private String companyName;
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
  public void setCompany(String companyName) {
    this.companyName = companyName;

    // Fetch company details from backend
    this.company =
        CheECSEManagerFeatureSet6Controller.getWholesaleCompany(companyName);

    if (company != null) {
      // companyCard.setCompany(company);
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
   * Opens the edit company dialog and refreshes the view on successful update.
   */
  @FXML
  private void handleEdit() {
    try {
      FXMLLoader loader =
          new FXMLLoader(CheECSEManagerApplication.class.getResource(
              "/ca/mcgill/ecse/cheecsemanager/view/page/companies/"
              + "UpdateWholesaleCompany.fxml"));
      Parent dialog = loader.load();

      UpdateWholesaleCompanyController controller = loader.getController();
      // controller.setMainController(this);
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
      System.err.println("Error loading UpdateWholesaleCompany dialog: " +
                         e.getMessage());
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

      CompanyOrderPlacementController controller = loader.getController();
      controller.setMainController(this);
      controller.setCompany(companyName);
      controller.setOnClose(() -> {
        closeDialog();
        setCompany(companyName);
      });

      showDialog(dialog);

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Error loading CompanyOrderPlacement dialog: " +
                         e.getMessage());
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

    PauseTransition pause =
        new PauseTransition(Duration.seconds(durationSeconds));
    pause.setOnFinished(e -> {
      FadeTransition fade =
          new FadeTransition(Duration.seconds(0.5), toastContainer);
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
