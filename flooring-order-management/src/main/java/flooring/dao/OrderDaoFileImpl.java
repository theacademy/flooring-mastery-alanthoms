package flooring.dao;

import flooring.dto.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDaoFileImpl implements OrderDao {


    private Map<Integer, Order> orders = new HashMap<>();

    @Override
    public Order addOrder(int orderNo, Order order) {
        Order prevOrder = orders.put(orderNo, order);
        return prevOrder;
    }

    @Override
    public List<Order> getAllOrders() {
        return null;
    }

    public Order getOrder(int orderNo) {
        return orders.get(orderNo);
    }

    @Override
    public Order removeOrder(int orderNo) {
        return null;
    }
}
