package flooring.dao;

import flooring.dto.Order;

import java.util.List;

public interface OrderDao {

    Order addOrder(int orderNo, Order order);
    List<Order> getAllOrders(String date);
    Order removeOrder(int orderNo);

}
