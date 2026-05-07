package flooring.service;

import flooring.dao.OrderDao;
import flooring.dao.ProductDao;
import flooring.dao.TaxDao;
import flooring.dto.Order;
import flooring.dto.Product;
import flooring.dto.Tax;

import java.math.BigDecimal;
import java.util.List;

public class FlooringServiceLayerImpl implements FlooringServiceLayer {

    OrderDao orderDao;
    ProductDao productDao;
    TaxDao taxDao;

    public FlooringServiceLayerImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }


    @Override
    public List<Order> getAllOrders() throws OrderDaoPersistenceException {
        return orderDao.getAllOrders();
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


    @Override
    public void addOrder(Order order) throws OrderDaoDuplicateIdException, OrderDaoDataValidationException, OrderDaoDataValidationException{
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


}
