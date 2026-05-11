package flooring.service;

import flooring.dao.ExportDao;
import flooring.dao.OrderDao;
import flooring.dao.ProductDao;
import flooring.dao.TaxDao;
import flooring.dto.Order;
import flooring.dto.Product;
import flooring.dto.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FlooringServiceLayerImpl implements FlooringServiceLayer {

    OrderDao orderDao;
    ProductDao productDao;
    TaxDao taxDao;
    ExportDao exportDao;

    public FlooringServiceLayerImpl(OrderDao orderDao, ProductDao productDao, TaxDao taxDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
    }


    @Override
    public List<Order> getAllOrders(String date) throws OrderDaoPersistenceException, OrderDaoDataValidationException {
        validateDate(date);
        String stringDate = getFileName(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        return orderDao.getAllOrders(stringDate);
    }

    @Override
    public List<Product> getAllProducts()
            throws OrderDaoPersistenceException {
        return productDao.getAllProducts();
    }

    public List<Tax> getAllTaxes() throws OrderDaoPersistenceException {
        return taxDao.getAllTaxes();
    }

    @Override
    public Order getOrder(String date, int orderNo) throws OrderDaoPersistenceException {

        String stringDate = getFileName(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        if (orderDao.getOrder(stringDate, orderNo) == null) {
            throw new OrderDaoPersistenceException("Order not found");
        }
        return orderDao.getOrder(stringDate, orderNo);
    }

    public void addOrder(String date, Order order)
            throws OrderDaoDuplicateIdException, OrderDaoDataValidationException, OrderDaoPersistenceException {


        String stringDate = getFileName(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        validateFutureDate(date);
        orderDao.addOrder(
                stringDate,
                order.getOrderNumber(),
                order);
    }

    @Override
    public Order removeOrder(String date, int orderNo) throws OrderDaoPersistenceException, OrderDaoDataValidationException {
        validateDate(date);
        String stringDate = getFileName(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        return orderDao.removeOrder(stringDate, orderNo);
    }


    @Override
    public Order editOrder(String date, int orderNo) throws OrderDaoPersistenceException, OrderDaoDataValidationException {
        validateDate(date);
        String stringDate = getFileName(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        Order prevOrder = orderDao.getOrder(stringDate, orderNo);
        return orderDao.editOrder(stringDate, orderNo,  prevOrder);
    }



    private void validateCustomerName(String name)
            throws OrderDaoDataValidationException {

        if (name == null || name.trim().isEmpty()) {
            throw new OrderDaoDataValidationException(
                    "ERROR: Customer name is required.");
        }

        if (!name.matches("[a-zA-Z0-9., ]+")) {

            throw new OrderDaoDataValidationException(
                    "ERROR: Customer name contains invalid characters.");
        }
    }

    private Tax validateState(String state)
            throws OrderDaoDataValidationException,
            OrderDaoPersistenceException {

        if (state == null || state.trim().isEmpty()) {

            throw new OrderDaoDataValidationException(
                    "ERROR: State is required.");
        }

        Tax tax = taxDao.getTax(state);

        if (tax == null) {

            throw new OrderDaoDataValidationException(
                    "ERROR: We do not sell in " + state + ".");
        }

        return tax;
    }

    private Product validateProductType(String productType)
            throws OrderDaoDataValidationException,
            OrderDaoPersistenceException {

        if (productType == null || productType.trim().isEmpty()) {

            throw new OrderDaoDataValidationException(
                    "ERROR: Product Type is required.");
        }

        Product product = productDao.getProduct(productType);

        if (product == null) {

            throw new OrderDaoDataValidationException(
                    "ERROR: We do not sell  " + productType + ".");
        }

        return product;
    }




    private void validateArea(BigDecimal area)
            throws OrderDaoDataValidationException {

        if (area == null) {
            throw new OrderDaoDataValidationException(
                    "ERROR: Area is required.");
        }

        if (area.compareTo(new BigDecimal("100")) < 0) {
            throw new OrderDaoDataValidationException(
                    "ERROR: Minimum order size is 100 sq ft.");
        }
    }

    private void validateDate(String date) throws OrderDaoDataValidationException{
        try {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        } catch (DateTimeParseException e) {
            throw new OrderDaoDataValidationException(
                    "ERROR: Date format is invalid.");
        }
        if (date.trim().isEmpty()){
            throw new OrderDaoDataValidationException("ERROR: Date format is invalid.");
        }
    }

    private void validateFutureDate(String date) throws OrderDaoDataValidationException{
        validateDate(date);
        LocalDate orderDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        if (orderDate.isBefore(LocalDate.now())) {
            throw new OrderDaoDataValidationException("ERROR: Order date must be in the future.");
        }
    }

    public void orderNoCheck(String date, Order order)
            throws OrderDaoPersistenceException, OrderDaoDuplicateIdException {

        String stringDate = getFileName(
                LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        if (orderDao.getOrder(stringDate, order.getOrderNumber()) != null) {
            throw new OrderDaoDuplicateIdException(
                    "Order number " + order.getOrderNumber() + " already exists.");
        }
    }

    @Override
    public Order prepareOrder(String date, Order order) throws OrderDaoDuplicateIdException, OrderDaoDataValidationException, OrderDaoPersistenceException {

        String stringDate = getFileName(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        validateCustomerName(order.getCustomerName());

        validateArea(order.getArea());

        Tax tax = validateState(order.getState());

        Product product =
                validateProductType(order.getProductType());

        order.setTaxRate(tax.getTaxRate());
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());


        BigDecimal materialCost =
                order.getArea().multiply(product.getCostPerSquareFoot());

        BigDecimal laborCost =
                order.getArea().multiply(product.getLaborCostPerSquareFoot());

        BigDecimal taxAmount =
                materialCost.add(laborCost)
                        .multiply(tax.getTaxRate()
                                .divide(new BigDecimal("100")));

        BigDecimal total =
                materialCost.add(laborCost).add(taxAmount);

        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(taxAmount);
        order.setTotal(total);
        return order;
    }

    private String getFileName(LocalDate date){
        return "Orders/Orders_" + date.format(DateTimeFormatter.ofPattern("MMddyyyy")) +".txt";
    }



    public Order applyEdits(Order editOrder, String newName, String newState, String newProductType, BigDecimal newArea) throws OrderDaoDataValidationException, OrderDaoPersistenceException {
        if (!newName.isBlank()) {
            editOrder.setCustomerName(newName);
        }
        if (!newState.isBlank()) {
            editOrder.setState(newState);
        }
        if (!newProductType.isBlank()) {
            editOrder.setProductType(newProductType);
        }
        if (newArea != null) {
            editOrder.setArea(newArea);
        }
        return editOrder;
    }

    public int getNextOrderNumber(String date)
            throws OrderDaoPersistenceException, OrderDaoDataValidationException {

        validateDate(date);

        String stringDate = getFileName(
                LocalDate.parse(date,
                        DateTimeFormatter.ofPattern("MM/dd/yyyy")));


        try {

            List<Order> orders = orderDao.getAllOrders(stringDate);

            return orders.stream()
                    .mapToInt(Order::getOrderNumber)
                    .max()
                    .orElse(0) + 1;

        } catch (OrderDaoPersistenceException e) {
            return 1;
        }
    }


    public void exportAllOrders() throws OrderDaoPersistenceException {
        List<Order> allOrders = orderDao.getAllOrdersAllDates();
        exportDao.exportAllOrders(allOrders, "Backup/DataExport.txt");
    }






}
