package flooring.dao;

import flooring.dto.Order;
import flooring.dto.Product;
import flooring.dto.Tax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaxDao {

    List<Tax> getAllTaxes();
    Tax getTax(String stateAbbreviation);
}