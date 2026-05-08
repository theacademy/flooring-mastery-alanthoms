package flooring.service;

import flooring.dto.Order;

import java.util.List;

public interface FlooringServiceLayer {

    void addOrder(String date, Order order) throws OrderDaoDuplicateIdException,
            OrderDaoDataValidationException,
            OrderDaoPersistenceException;

    List<Order> getAllOrders(String date) throws
            OrderDaoPersistenceException, OrderDaoDataValidationException;

    Order getOrder(String date, int orderNo) throws
            OrderDaoPersistenceException;

    Order removeOrder(String date, int orderNo) throws
            OrderDaoPersistenceException, OrderDaoDataValidationException;
}
