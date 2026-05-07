package flooring.dao;

import flooring.dto.Order;

import java.util.List;

public interface OrderDao {

    Order addOrder(int orderNo, Order order);
    List<Order> getAllOrders();
    Order getOrder(int orderNo);
    Order removeOrder(int orderNo);

}
