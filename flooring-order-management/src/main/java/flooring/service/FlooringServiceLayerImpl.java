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
    public Order getOrder(int orderNo) throws OrderDaoPersistenceException {
        return orderDao.getOrder(orderNo);
    }

    @Override
    public Order removeOrder(int orderNo) throws OrderDaoPersistenceException {
        return orderDao.removeOrder(orderNo);
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


    @Override
    public void addOrder(Order order) throws OrderDaoDuplicateIdException, OrderDaoDataValidationException, OrderDaoPersistenceException {
        if(orderDao.getOrder(order.getOrderNumber()) != null){
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


        orderDao.addOrder(order.getOrderNumber(), order);
    }

    private String getFileName(LocalDate date){
        return "Orders/Orders_" + date.format(DateTimeFormatter.ofPattern("MMddyyyy")) +".txt";
    }


}
