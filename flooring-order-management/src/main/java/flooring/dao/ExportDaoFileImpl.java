package flooring.dao;

import flooring.dto.Order;
import flooring.service.OrderDaoPersistenceException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.io.PrintWriter;

public class ExportDaoFileImpl implements ExportDao {

    //comma delineation
    public static final String DELIMITER = ",";

    @Override
    public void exportAllOrders(List<Order> orders, String exportFileName)
            throws OrderDaoPersistenceException {

        //get the directory from filename in case it doesn't exist
        new File(exportFileName).getParentFile().mkdirs();

        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(exportFileName));
        } catch (IOException e) {
            throw new OrderDaoPersistenceException("Could not export data.", e);
        }

        //loop through marshall data and then write
        for (Order order : orders) {
            out.println(marshallOrder(order));
            out.flush();
        }
        out.close();
    }

    private String marshallOrder(Order order) {

        //added orderDate to export to differentiate between same orderNo for different files
        String orderAsText = order.getOrderDate().toString() + DELIMITER;

        orderAsText += order.getOrderNumber() + DELIMITER;

        orderAsText += order.getCustomerName() + DELIMITER;

        orderAsText += order.getState() + DELIMITER;

        orderAsText += order.getTaxRate() + DELIMITER;

        orderAsText += order.getProductType() + DELIMITER;

        orderAsText += order.getArea() + DELIMITER;

        orderAsText += order.getCostPerSquareFoot() + DELIMITER;

        orderAsText += order.getLaborCostPerSquareFoot() + DELIMITER;

        orderAsText += order.getMaterialCost() + DELIMITER;

        orderAsText += order.getLaborCost() + DELIMITER;

        orderAsText += order.getTax() + DELIMITER;

        orderAsText += order.getTotal();

        return orderAsText;
    }
}
