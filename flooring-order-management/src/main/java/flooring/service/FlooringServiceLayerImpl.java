package flooring.service;

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
    public Order getOrder(String date, int orderNo) throws OrderDaoPersistenceException {

        String stringDate = getFileName(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        if (orderDao.getOrder(stringDate, orderNo) == null) {
            throw new OrderDaoPersistenceException("Order not found");
        }
        return orderDao.getOrder(stringDate, orderNo);
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

    private void validateOrderData(Order order) throws OrderDaoDataValidationException{

        if (order.getCustomerName() == null
                || order.getCustomerName().trim().isEmpty()
                || order.getState() == null
                || order.getState().trim().isEmpty()
                || order.getProductType() == null
                || order.getProductType().trim().isEmpty()
                || order.getArea() == null
                || order.getArea().compareTo(BigDecimal.ZERO) <= 0) {

            throw new OrderDaoDataValidationException(
                    "ERROR: Customer Name, State, Product Type, and Area are required and must be valid.");
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


    @Override
    public Order prepareOrder(String date, Order order) throws OrderDaoDuplicateIdException, OrderDaoDataValidationException, OrderDaoPersistenceException {
        validateDate(date);

        String stringDate = getFileName(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        if(orderDao.getOrder(stringDate, order.getOrderNumber()) != null){
            throw new OrderDaoDuplicateIdException("Order number" + order.getOrderNumber() + " already exists.");
        }
        validateOrderData(order);

        Tax tax = taxDao.getTax(order.getState());

        if (tax == null) {
            throw new UnsupportedOperationException(
                    "ERROR: State " + order.getState() + " not found.");
        }


        Product product = productDao.getProduct(order.getProductType());
        if (product == null) {
            throw new UnsupportedOperationException(
                    "ERROR: Product type " + order.getProductType() + " not found.");
        }


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

    public void addOrder(String date, Order order)
            throws OrderDaoDuplicateIdException, OrderDaoDataValidationException, OrderDaoPersistenceException {

        validateFutureDate(date);

        String stringDate = getFileName(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        orderDao.addOrder(
                stringDate,
                order.getOrderNumber(),
                order);
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






}
