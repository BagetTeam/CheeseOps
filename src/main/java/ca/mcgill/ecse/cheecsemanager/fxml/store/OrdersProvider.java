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

  private OrdersProvider() { refresh(); }

  public static OrdersProvider getInstance() { return INSTANCE; }

  public ObservableList<Order> getOrders() { return orders; }

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

  public void setCompany(String companyName) {
    this.companyName = companyName;
    refresh();
  }
}
