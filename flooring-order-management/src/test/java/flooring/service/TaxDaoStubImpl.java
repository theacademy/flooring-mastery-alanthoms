package flooring.service;

import flooring.dao.TaxDao;
import flooring.dto.Tax;

import java.math.BigDecimal;
import java.util.List;

public class TaxDaoStubImpl implements TaxDao {
    @Override
    public List<Tax> getAllTaxes() {
        Tax tax = new Tax("WA");
        tax.setStateName("Washington");
        tax.setTaxRate(new BigDecimal("9.25"));
        return List.of(tax);
    }

    @Override
    public Tax getTax(String state) {
        if (state.equals("WA")) {
            Tax tax = new Tax("WA");
            tax.setStateName("Washington");
            tax.setTaxRate(new BigDecimal("9.25"));
            return tax;
        }
        return null;
    }
}