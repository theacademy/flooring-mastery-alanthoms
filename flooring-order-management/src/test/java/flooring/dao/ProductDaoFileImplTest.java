package flooring.dao;

import flooring.dto.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoFileImplTest {

    private ProductDaoFileImpl dao;
    private static final String TEST_FILE = "src/test/resources/testdata/TestProducts.txt";

    @BeforeEach
    public void setUp() {
        dao = new ProductDaoFileImpl(TEST_FILE);
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = dao.getAllProducts();
        assertEquals(2, products.size());
    }

    @Test
    public void testGetProductFound() {
        Product product = dao.getProduct("Wood");
        assertNotNull(product);
        assertEquals("Wood", product.getProductType());
        assertEquals(new BigDecimal("5.15"), product.getCostPerSquareFoot());
    }

    @Test
    public void testGetProductNotFound() {
        Product product = dao.getProduct("Marble");
        assertNull(product);
    }
}