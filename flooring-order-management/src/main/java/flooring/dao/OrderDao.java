package flooring.dao;

import flooring.dto.Order;
import flooring.service.OrderDaoPersistenceException;

import java.util.List;

public interface OrderDao {

    Order addOrder(String date, int orderNo, Order order) throws OrderDaoPersistenceException;
    List<Order> getAllOrders(String date) throws OrderDaoPersistenceException;
    Order getOrder(String date, int orderNo) throws OrderDaoPersistenceException;
    Order removeOrder(String date, int orderNo) throws OrderDaoPersistenceException;

}
