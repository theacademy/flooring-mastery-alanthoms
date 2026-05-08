package flooring.dao;

import flooring.dto.Order;
import flooring.service.OrderDaoPersistenceException;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {





    public static final String DELIMITER = ",";



    private Map<Integer, Order> orders = new HashMap<>();



    @Override
    public Order addOrder(String stringDateFileName, int orderNo, Order order) throws OrderDaoPersistenceException {
        loadOrders(stringDateFileName);
        Order prevOrder = orders.put(orderNo, order);
        writeOrders(stringDateFileName);
        return prevOrder;
    }

    @Override
    public List<Order> getAllOrders(String stringDateFileName) throws OrderDaoPersistenceException  {
        loadOrders(stringDateFileName);
        return  new ArrayList<>(orders.values());
    }

    public Order getOrder(String stringDateFileName, int orderNo) throws OrderDaoPersistenceException {
        loadOrders(stringDateFileName);
        return orders.get(orderNo);
    }

    @Override
    public Order removeOrder(String stringDateFileName, int orderNo) throws OrderDaoPersistenceException{
        loadOrders(stringDateFileName);
        Order removedOrder = orders.remove(orderNo);
        writeOrders(stringDateFileName);
        return removedOrder;
    }


    private Order unmarshallOrder(String orderAsText) {

        String[] orderTokens = orderAsText.split(DELIMITER);

        int orderNo = Integer.parseInt(orderTokens[0]);

        Order orderFromFile = new Order(orderNo);

        orderFromFile.setCustomerName(orderTokens[1]);
        orderFromFile.setState(orderTokens[2]);

        orderFromFile.setTaxRate(new BigDecimal(orderTokens[3]));

        orderFromFile.setProductType(orderTokens[4]);

        orderFromFile.setArea(new BigDecimal(orderTokens[5]));

        orderFromFile.setCostPerSquareFoot(
                new BigDecimal(orderTokens[6]));

        orderFromFile.setLaborCostPerSquareFoot(
                new BigDecimal(orderTokens[7]));

        orderFromFile.setMaterialCost(
                new BigDecimal(orderTokens[8]));

        orderFromFile.setLaborCost(
                new BigDecimal(orderTokens[9]));

        orderFromFile.setTax(
                new BigDecimal(orderTokens[10]));

        orderFromFile.setTotal(
                new BigDecimal(orderTokens[11]));

        return orderFromFile;
    }

    private void loadOrders(String stringDateFileName) throws OrderDaoPersistenceException {

        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(stringDateFileName)));
        } catch (FileNotFoundException e) {
            return;
        }
        // currentLine holds the most recent line read from the file
        String currentLine;
        // currentStudent holds the most recent student unmarshalled
        Order currentOrder;
        // Go through ROSTER_FILE line by line, decoding each line into a
        // Student object by calling the unmarshallStudent method.
        // Process while we have more lines in the file
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a Student
            currentOrder = unmarshallOrder(currentLine);

            // We are going to use the student id as the map key for our student object.
            // Put currentStudent into the map using student id as the key
            orders.put(currentOrder.getOrderNumber(), currentOrder);
        }
        // close scanner
        scanner.close();
    }

    private String marshallOrder(Order order) {

        String orderAsText = order.getOrderNumber() + DELIMITER;

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

    private void writeOrders(String stringDateFileName) throws OrderDaoPersistenceException {

        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(stringDateFileName));
        } catch (IOException e) {
            throw new OrderDaoPersistenceException(
                    "Could not save order data.", e);
        }

        // write header row first

        String orderAsText;

        List<Order> orderList = this.getAllOrders(stringDateFileName);

        for (Order currentOrder : orderList) {

            orderAsText = marshallOrder(currentOrder);

            out.println(orderAsText);

            out.flush();
        }

        out.close();
    }

}
