package flooring.service;

import flooring.dao.OrderDao;
import flooring.dto.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDaoStubImpl implements OrderDao {
    private Map<Integer, Order> orders = new HashMap<>();

    @Override
    public Order addOrder(String date, int orderNo, Order order) {
        return orders.put(orderNo, order);
    }

    @Override
    public Order getOrder(String date, int orderNo) {
        return orders.get(orderNo);
    }

    @Override
    public List<Order> getAllOrders(String date) {
        return new ArrayList<>(orders.values());
    }

    @Override
    public Order removeOrder(String date, int orderNo) {
        return orders.remove(orderNo);
    }

    @Override
    public Order editOrder(String date, int orderNo, Order order) {
        return orders.put(orderNo, order);
    }

    @Override
    public List<Order> getAllOrdersAllDates() {
        return new ArrayList<>(orders.values());
    }
}