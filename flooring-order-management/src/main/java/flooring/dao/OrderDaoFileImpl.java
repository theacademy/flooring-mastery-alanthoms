package flooring.dao;

import flooring.dto.Order;
import flooring.service.OrderDaoPersistenceException;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {




    //comma delineation
    public static final String DELIMITER = ",";

    //hashmap with orderno as key to store order objects
    private Map<Integer, Order> orders = new HashMap<>();



    @Override
    public Order addOrder(String stringDateFileName, int orderNo, Order order) throws OrderDaoPersistenceException {
        //call load orders
        loadOrders(stringDateFileName);
        //put in map
        Order prevOrder = orders.put(orderNo, order);
        //write new order
        writeOrders(stringDateFileName);
        return prevOrder;
    }

    @Override
    public List<Order> getAllOrders(String stringDateFileName) throws OrderDaoPersistenceException  {
        //simply load and turn values
        loadOrders(stringDateFileName);
        return  new ArrayList<>(orders.values());
    }

    //helper method to getOrder such as for delete and edit
    public Order getOrder(String stringDateFileName, int orderNo) throws OrderDaoPersistenceException {

        loadOrders(stringDateFileName);
        return orders.get(orderNo);
    }

    @Override
    public Order removeOrder(String stringDateFileName, int orderNo) throws OrderDaoPersistenceException{
        loadOrders(stringDateFileName);
        //simple remove
        Order removedOrder = orders.remove(orderNo);
        writeOrders(stringDateFileName);
        return removedOrder;
    }

    @Override
    public Order editOrder(String stringDateFileName, int orderNo, Order editedOrder) throws OrderDaoPersistenceException {
        loadOrders(stringDateFileName);
        //use put to replace/ edit old order
        Order removedOrder = orders.put(orderNo, editedOrder);
        writeOrders(stringDateFileName);
        return removedOrder;
    }


    private Order unmarshallOrder(String orderAsText) {

        //delimiter is grabbed to split, being comma
        String[] orderTokens = orderAsText.split(DELIMITER);

        //create order object with orderNo
        int orderNo = Integer.parseInt(orderTokens[0]);
        //
        Order orderFromFile = new Order(orderNo);

        //values are set according to order in order object with customer name first
        //setter methods have validation
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
        //clear previous order as changes are written to file
        orders.clear();
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
        // Go through file line by line, decoding each line into a
        // Student object by calling the unmarshallmethod.
        // Process while we have more lines in the file
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into an Order
            currentOrder = unmarshallOrder(currentLine);

            // We are going to use the orderno as the map key for our order object.
            // Put current Order into the map using orderNo as the key
            orders.put(currentOrder.getOrderNumber(), currentOrder);
        }
        // close scanner
        scanner.close();
    }

    private String marshallOrder(Order order) {

        // get values and add to string separated by comma in exact order
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


        String orderAsText;
        //orders list is gathered using values not getAll as it would overwrite
        //classRoster read from one single file and would not change
        List<Order> orderList = new ArrayList<>(orders.values());

        for (Order currentOrder : orderList) {

            orderAsText = marshallOrder(currentOrder);

            out.println(orderAsText);

            out.flush();
        }

        out.close();
    }

    public List<Order> getAllOrdersAllDates() throws OrderDaoPersistenceException {
        //get orders folder and files inside ending in txt
        File ordersDir = new File("Orders");
        File[] orderFiles = ordersDir.listFiles((dir, name) -> name.endsWith(".txt"));

        List<Order> allOrders = new ArrayList<>();
        //return empty arraylist if null
        if (orderFiles == null) return allOrders;
        //loop through add all values to list
        for (File file : orderFiles) {
            loadOrders(file.getPath());
            String formattedDate = getDateFromFilename(file.getName());
            LocalDate date = LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            for (Order order : orders.values()) {
                order.setOrderDate(date);
            }
            allOrders.addAll(orders.values());
        }
        return allOrders;
    }

    //helper method to get date from a filename
    public String getDateFromFilename(String fileName) throws OrderDaoPersistenceException {
        String rawDate = fileName.replace("Orders_", "").replace(".txt", ""); // "06012026"
        return rawDate.substring(0, 2) + "/" + rawDate.substring(2, 4) + "/" + rawDate.substring(4); // "06/01/2026"
    }

    //helper method for export all method
    public List<Order> getAllOrdersAllDates(String directory) throws OrderDaoPersistenceException {
        File ordersDir = new File(directory);
        File[] orderFiles = ordersDir.listFiles((dir, name) -> name.endsWith(".txt"));

        List<Order> allOrders = new ArrayList<>();
        if (orderFiles == null) return allOrders;

        for (File file : orderFiles) {
            loadOrders(file.getPath());
            String formattedDate = getDateFromFilename(file.getName());
            LocalDate date = LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            for (Order order : orders.values()) {
                order.setOrderDate(date);
            }
            allOrders.addAll(orders.values());
        }
        return allOrders;
    }


}
