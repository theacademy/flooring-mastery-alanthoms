package flooring.dao;

import flooring.dto.Order;
import flooring.service.OrderDaoPersistenceException;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {


    private final String ORDERS_FILE;
    public static final String DELIMITER = ",";

    public OrderDaoFileImpl(){
        ORDERS_FILE = "orders.txt";
    }
    private Map<Integer, Order> orders = new HashMap<>();

    @Override
    public Order addOrder(int orderNo, Order order) throws OrderDaoPersistenceException {
        loadOrders();
        Order prevOrder = orders.put(orderNo, order);
        writeOrders();
        return prevOrder;
    }

    @Override
    public List<Order> getAllOrders() throws OrderDaoPersistenceException  {
        loadOrders();
        return  new ArrayList<>(orders.values());
    }

    public Order getOrder(int orderNo) throws OrderDaoPersistenceException {
        loadOrders();
        return orders.get(orderNo);
    }

    @Override
    public Order removeOrder(int orderNo) throws OrderDaoPersistenceException{
        loadOrders();
        Order removedOrder = orders.remove(orderNo);
        writeOrders();
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

    private void loadOrders() throws OrderDaoPersistenceException {
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(ORDERS_FILE)));
        } catch (FileNotFoundException e) {
            throw new OrderDaoPersistenceException(
                    "-_- Could not load roster data into memory.", e);
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

    private void writeOrders() throws OrderDaoPersistenceException {

        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(ORDERS_FILE));
        } catch (IOException e) {
            throw new OrderDaoPersistenceException(
                    "Could not save order data.", e);
        }

        // write header row first
        out.println(
                "OrderNumber,CustomerName,State,TaxRate," +
                        "ProductType,Area,CostPerSquareFoot," +
                        "LaborCostPerSquareFoot,MaterialCost," +
                        "LaborCost,Tax,Total"
        );

        String orderAsText;

        List<Order> orderList = this.getAllOrders();

        for (Order currentOrder : orderList) {

            orderAsText = marshallOrder(currentOrder);

            out.println(orderAsText);

            out.flush();
        }

        out.close();
    }

}
