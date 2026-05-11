package flooring.service;


import flooring.dao.ProductDao;
import flooring.dto.Product;

import java.math.BigDecimal;
import java.util.List;

public class ProductDaoStubImpl implements ProductDao {
    @Override
    public List<Product> getAllProducts() {
        Product wood = new Product("Wood");
        wood.setCostPerSquareFoot(new BigDecimal("5.15"));
        wood.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        return List.of(wood);
    }

    @Override
    public Product getProduct(String productType) {
        if (productType.equals("Wood")) {
            Product wood = new Product("Wood");
            wood.setCostPerSquareFoot(new BigDecimal("5.15"));
            wood.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
            return wood;
        }
        return null;
    }
}