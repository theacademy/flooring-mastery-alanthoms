package flooring.dao;

import flooring.dto.Order;
import flooring.dto.Product;

import java.util.List;

public class ProductDaoFileImpl implements ProductDao {
    @Override
    public Product addProduct(String productType, Order order) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public Product getProduct(String productType) {
        return null;
    }
}
