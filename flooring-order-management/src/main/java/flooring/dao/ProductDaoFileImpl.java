package flooring.dao;

import flooring.dto.Order;
import flooring.dto.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class ProductDaoFileImpl implements ProductDao {

    private Map<String, Product> products = new HashMap<>();
    private static final String PRODUCTS_FILE = "data/Products.txt";
    private static final String DELIMITER = ",";


    @Override
    public List<Product> getAllProducts() {
        loadProducts();
        return new ArrayList<>(products.values());
    }

    @Override
    public Product getProduct(String productType) {
        loadProducts();
        return products.get(productType);
    }

    private Product unmarshallProduct(String productAsText) {

        String[] tokens = productAsText.split(DELIMITER);

        String productType = tokens[0];

        Product product = new Product(productType);

        product.setCostPerSquareFoot(
                new BigDecimal(tokens[1]));

        product.setLaborCostPerSquareFoot(
                new BigDecimal(tokens[2]));

        return product;
    }


    private void loadProducts() {

        Scanner scanner;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCTS_FILE)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not load tax data.", e);
        }

        scanner.nextLine(); // skip header

        while (scanner.hasNextLine()) {

            String currentLine = scanner.nextLine();

            Product currentProduct = unmarshallProduct(currentLine);

            products.put(currentProduct.getProductType(), currentProduct);
        }

        scanner.close();
    }
}
