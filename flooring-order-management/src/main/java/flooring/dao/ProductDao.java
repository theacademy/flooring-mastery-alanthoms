package flooring.dao;

import flooring.dto.Order;
import flooring.dto.Product;

import java.util.List;

public interface ProductDao {

    Product addProduct(String productType, Order order);
    List<Product> getAllProducts();
    Product getProduct(String productType);

}
