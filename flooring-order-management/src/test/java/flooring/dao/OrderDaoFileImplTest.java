package flooring.dao;

import flooring.dto.Order;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDaoFileImplTest {

    private OrderDaoFileImpl dao;
    private static final String TEST_DIR = "TestOrders";
    private static final String TEST_FILE_1 = "TestOrders/Orders_01012030.txt";
    private static final String TEST_FILE_2 = "TestOrders/Orders_02012030.txt";



    @BeforeEach
    public void setUp() {
        //start with clean folder and file
        dao = new OrderDaoFileImpl();
        new File(TEST_DIR).mkdirs();
    }

    @AfterEach
    public void tearDown() {
        //remove everything after
        new File(TEST_FILE_1).delete();
        new File(TEST_FILE_2).delete();
        new File(TEST_DIR).delete();
    }


    private Order createTestOrder(int orderNo, String name) {
        Order order = new Order(orderNo);
        order.setCustomerName(name);
        order.setState("WA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("150"));
        order.setTaxRate(new BigDecimal("9.25"));
        order.setCostPerSquareFoot(new BigDecimal("5.15"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        order.setMaterialCost(new BigDecimal("772.50"));
        order.setLaborCost(new BigDecimal("712.50"));
        order.setTax(new BigDecimal("136.99"));
        order.setTotal(new BigDecimal("1621.99"));
        return order;
    }

    @Test
    public void testAddAndGetOrder() throws Exception {
        //set values for order

        Order order1 = createTestOrder(1, "Bob");

        dao.addOrder(TEST_FILE_1, 1, order1);
        Order retrieved = dao.getOrder(TEST_FILE_1, 1);

        assertNotNull(retrieved);
        assertEquals(order1, retrieved);
    }

    @Test
    public void testGetOrderNotFound() throws Exception {
        Order retrieved = dao.getOrder(TEST_FILE_1, 999);
        assertNull(retrieved);
    }

    @Test
    public void testRemoveOrder() throws Exception {
        Order order1 = createTestOrder(1, "Bob");


        dao.removeOrder(TEST_FILE_1, 1);
        Order retrieved = dao.getOrder(TEST_FILE_1, 1);
        assertNull(retrieved);
    }

    @Test
    public void testGetAllOrders() throws Exception {

        Order order1 = createTestOrder(1, "Bob");
        Order order2 = createTestOrder(2, "Alice");


        dao.addOrder(TEST_FILE_1, 1, order1);
        dao.addOrder(TEST_FILE_1, 2, order2);

        assertEquals(2, dao.getAllOrders(TEST_FILE_1).size());
    }


    @Test
    public void testGetAllOrdersAllDates() throws Exception {
        dao.addOrder(TEST_FILE_1, 1, createTestOrder(1, "Bob"));
        dao.addOrder(TEST_FILE_2, 2, createTestOrder(2, "Alice"));

        List<Order> allOrders = dao.getAllOrdersAllDates(TEST_DIR);
        assertEquals(2, allOrders.size());
    }


}