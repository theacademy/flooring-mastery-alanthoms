package flooring.service;

import flooring.dto.Order;

import java.util.List;

public interface FlooringServiceLayer {

    void addOrder(Order order) throws OrderDaoDuplicateIdException,
            OrderDaoDataValidationException,
            OrderDaoPersistenceException;

    List<Order> getAllOrders() throws
            OrderDaoPersistenceException;

    Order getOrder(int orderNo) throws
            OrderDaoPersistenceException;

    Order removeOrder(int orderNo) throws
            OrderDaoPersistenceException;
}
