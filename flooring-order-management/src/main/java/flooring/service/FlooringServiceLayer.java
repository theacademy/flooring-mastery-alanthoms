package flooring.service;

import flooring.dto.Order;
import flooring.dto.Product;
import flooring.dto.Tax;

import java.math.BigDecimal;
import java.util.List;

public interface FlooringServiceLayer {

    Order prepareOrder(String date, Order order) throws OrderDaoDuplicateIdException,
            OrderDaoDataValidationException,
            OrderDaoPersistenceException;

    List<Order> getAllOrders(String date) throws
            OrderDaoPersistenceException, OrderDaoDataValidationException;

    Order getOrder(String date, int orderNo) throws
            OrderDaoPersistenceException;

    Order removeOrder(String date, int orderNo) throws
            OrderDaoPersistenceException, OrderDaoDataValidationException;


    Order editOrder(String date, int orderNo) throws
            OrderDaoPersistenceException, OrderDaoDataValidationException;

    void addOrder(String date, Order order) throws OrderDaoDuplicateIdException,
            OrderDaoDataValidationException,
            OrderDaoPersistenceException;

    Order applyEdits(Order order, String name, String state, String product, BigDecimal area) throws OrderDaoDataValidationException, OrderDaoPersistenceException;


    List<Product> getAllProducts() throws
            OrderDaoPersistenceException, OrderDaoDataValidationException;


    List<Tax> getAllTaxes() throws OrderDaoDataValidationException, OrderDaoPersistenceException;


    int getNextOrderNumber(String date)
            throws OrderDaoPersistenceException, OrderDaoDataValidationException;
}

