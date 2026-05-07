package flooring.dao;

import flooring.dto.Order;
import flooring.service.OrderDaoPersistenceException;

import java.util.List;

public interface OrderDao {

    Order addOrder(int orderNo, Order order) throws OrderDaoPersistenceException;
    List<Order> getAllOrders() throws OrderDaoPersistenceException;
    Order getOrder(int orderNo) throws OrderDaoPersistenceException;
    Order removeOrder(int orderNo) throws OrderDaoPersistenceException;

}
