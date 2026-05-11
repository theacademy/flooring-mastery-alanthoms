package flooring.dao;

import flooring.dto.Order;
import flooring.service.OrderDaoPersistenceException;

import java.util.List;

public interface ExportDao {
    public void exportAllOrders(List<Order> orders, String exportFileName) throws OrderDaoPersistenceException;
}
