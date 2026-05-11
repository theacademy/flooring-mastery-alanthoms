package flooring.service;

import flooring.dao.ExportDao;
import flooring.dto.Order;

import java.util.List;

public class ExportDaoStubImpl implements ExportDao {
    @Override
    public void exportAllOrders(List<Order> orders, String filename) {
        // do nothing
    }
}