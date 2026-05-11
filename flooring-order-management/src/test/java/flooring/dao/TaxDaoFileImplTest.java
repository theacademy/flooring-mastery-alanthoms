package flooring.dao;

import flooring.dto.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class TaxDaoFileImplTest {

    private TaxDaoFileImpl dao;
    private static final String TEST_FILE = "src/test/resources/testdata/TestTaxes.txt";

    @BeforeEach
    public void setUp() {
        dao = new TaxDaoFileImpl(TEST_FILE);
    }

    @Test
    public void testGetAllTaxes() {
        List<Tax> taxes = dao.getAllTaxes();
        assertEquals(2, taxes.size());
    }

    @Test
    public void testGetTaxFound() {
        Tax tax = dao.getTax("WA");
        assertNotNull(tax);
        assertEquals("Washington", tax.getStateName());
        assertEquals(new BigDecimal("9.25"), tax.getTaxRate());
    }

    @Test
    public void testGetTaxNotFound() {
        Tax tax = dao.getTax("CA");
        assertNull(tax);
    }
}