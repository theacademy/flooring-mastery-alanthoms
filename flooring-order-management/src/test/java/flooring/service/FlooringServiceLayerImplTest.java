package flooring.service;


import flooring.dto.Order;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class FlooringServiceLayerImplTest {

    private FlooringServiceLayer service;

    @BeforeEach
    public void setUp() {
        service = new FlooringServiceLayerImpl(
                new OrderDaoStubImpl(),
                new ProductDaoStubImpl(),
                new TaxDaoStubImpl(),
                new ExportDaoStubImpl()
        );
    }

    private Order createTestOrder(int orderNo, String name) {
        Order order = new Order(orderNo);
        order.setCustomerName(name);
        order.setState("WA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("150"));
        return order;
    }

    @Test
    public void testAddAndGetOrder() throws Exception {
        Order order = createTestOrder(1, "Bob");
        Order prepped = service.prepareOrder("12/12/2030", order);
        service.addOrder("12/12/2030", prepped);

        Order retrieved = service.getOrder("12/12/2030", 1);
        assertNotNull(retrieved);
        assertEquals("Bob", retrieved.getCustomerName());
    }

    @Test
    public void testGetOrderNotFound() {
        assertThrows(OrderDaoPersistenceException.class, () ->
                service.getOrder("12/12/2030", 999)
        );
    }

    @Test
    public void testAddOrderPastDateFails() {
        Order order = createTestOrder(1, "Bob");
        assertThrows(OrderDaoDataValidationException.class, () ->
                service.addOrder("01/01/2000", order)
        );
    }

    @Test
    public void testPrepareOrderInvalidName() {
        Order order = createTestOrder(1, "");
        assertThrows(OrderDaoDataValidationException.class, () ->
                service.prepareOrder("12/12/2030", order)
        );
    }

    @Test
    public void testPrepareOrderInvalidState() {
        Order order = createTestOrder(1, "Bob");
        order.setState("XX"); // not in stub
        assertThrows(OrderDaoDataValidationException.class, () ->
                service.prepareOrder("12/12/2030", order)
        );
    }

    @Test
    public void testPrepareOrderAreaTooSmall() {
        Order order = createTestOrder(1, "Bob");
        order.setArea(new BigDecimal("50"));
        assertThrows(OrderDaoDataValidationException.class, () ->
                service.prepareOrder("12/12/2030", order)
        );
    }

    @Test
    public void testPrepareOrderCalculatesCorrectly() throws Exception {
        Order order = createTestOrder(1, "Bob");
        Order prepped = service.prepareOrder("12/12/2030", order);

        assertNotNull(prepped.getMaterialCost());
        assertNotNull(prepped.getLaborCost());
        assertNotNull(prepped.getTax());
        assertNotNull(prepped.getTotal());
    }

    @Test
    public void testRemoveOrder() throws Exception {
        Order order = createTestOrder(1, "Bob");
        Order prepped = service.prepareOrder("12/12/2030", order);
        service.addOrder("12/12/2030", prepped);

        service.removeOrder("12/12/2030", 1);
        assertThrows(OrderDaoPersistenceException.class, () ->
                service.getOrder("12/12/2030", 1)
        );
    }

    @Test
    public void testGetNextOrderNumber() throws Exception {
        Order order = createTestOrder(1, "Bob");
        Order prepped = service.prepareOrder("12/12/2030", order);
        service.addOrder("12/12/2030", prepped);

        int next = service.getNextOrderNumber("12/12/2030");
        assertEquals(2, next);
    }

    @Test
    public void testInvalidDateFormat() {
        assertThrows(Exception.class, () ->
                service.getAllOrders("notadate")
        );
    }
}