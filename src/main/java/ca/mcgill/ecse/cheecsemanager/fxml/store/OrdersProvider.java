package ca.mcgill.ecse.cheecsemanager.fxml.store;

import ca.mcgill.ecse.cheecsemanager.controller.CheECSEManagerFeatureSet6Controller;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Centralized observable source of order data so that UI controllers can react
 * to model mutations without reloading the page.
 * @author Ming Li Liu
 */
public class OrdersProvider {
  /** Lightweight projection representing a single wholesale company order. */
  public class Order {
    public Date orderDate;
    public String monthsAged;
    public Integer nrCheeseWheelsOrdered;
    public Integer nrCheeseWheelsMissing;
    public Date deliveryDate;
  }

  private String companyName;

  private final ObservableList<Order> orders =
      FXCollections.observableArrayList();

  private static OrdersProvider INSTANCE = new OrdersProvider();

  /** Builds the singleton instance and primes it with the current selection. */
  private OrdersProvider() { refresh(); }

  /** @return shared provider instance */
  public static OrdersProvider getInstance() { return INSTANCE; }

  /**
   * @return observable list of orders that the FXML tables bind to
   */
  public ObservableList<Order> getOrders() { return orders; }

  /**
   * Refreshes the observable list so the UI reflects the currently selected
   * wholesale company's orders.
   */
  public void refresh() {
    if (this.companyName == null) {
      this.orders.setAll(new ArrayList<>());
      return;
    }

    var company = CheECSEManagerFeatureSet6Controller.getWholesaleCompany(
        this.companyName);

    List<Order> latestOrders = new ArrayList<>();

    for (int i = 0; i < company.numberOfOrderDates(); i++) {
      var orderDate = company.getOrderDate(i);
      var monthsAged = company.getMonthsAged(i);
      var nrCheeseWheelsOrdered = company.getNrCheeseWheelsOrdered(i);
      var nrCheeseWheelsMissing = company.getNrCheeseWheelsMissing(i);
      var deliveryDate = company.getDeliveryDate(i);

      var order = new Order();
      order.orderDate = orderDate;
      order.monthsAged = monthsAged;
      order.nrCheeseWheelsOrdered = nrCheeseWheelsOrdered;
      order.nrCheeseWheelsMissing = nrCheeseWheelsMissing;
      order.deliveryDate = deliveryDate;

      latestOrders.add(order);
    }

    this.orders.setAll(latestOrders);
  }

  /**
   * Updates the wholesale company whose orders should be shown and refreshes the
   * backing list.
   * @param companyName name of the selected wholesale company
   */
  public void setCompany(String companyName) {
    this.companyName = companyName;
    refresh();
  }
}
