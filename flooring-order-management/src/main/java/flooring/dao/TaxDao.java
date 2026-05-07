package flooring.dao;

import flooring.dto.Order;
import flooring.dto.Product;
import flooring.dto.Tax;

import java.util.List;

public interface TaxDao {

    Tax addTax(String stateAbbreviation, Tax tax);
    List<Tax> getAllTaxes();
    Tax getTax(String stateAbbreviation);
}