package flooring.dao;

import flooring.dto.Product;
import flooring.dto.Tax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class TaxDaoFileImpl implements TaxDao {


    private Map<String, Tax> taxes = new HashMap<>();
    private static final String TAXES_FILE = "data/Taxes.txt";
    private static final String DELIMITER = ",";

    @Override
    public List<Tax> getAllTaxes() {
        loadTaxes();
        return new ArrayList<>(taxes.values());
    }

    @Override
    public Tax getTax(String stateAbbreviation) {
        loadTaxes();
        return taxes.get(stateAbbreviation);
    }

    private Tax unmarshallTax(String taxAsText) {

        String[] tokens = taxAsText.split(DELIMITER);

        String stateAbbreviation = tokens[0];

        Tax tax = new Tax(stateAbbreviation);

        tax.setStateName(new String(tokens[1]));
        tax.setTaxRate(new BigDecimal(tokens[2]));

        return tax;
    }

    private void loadTaxes() {

        Scanner scanner;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(TAXES_FILE)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not load tax data.", e);
        }

        scanner.nextLine(); // skip header

        while (scanner.hasNextLine()) {

            String currentLine = scanner.nextLine();

            Tax currentTax = unmarshallTax(currentLine);

            taxes.put(currentTax.getStateAbbreviation(), currentTax);
        }
        scanner.close();
    }
}
