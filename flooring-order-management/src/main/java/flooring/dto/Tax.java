package flooring.dto;

import java.math.BigDecimal;

public class Tax {

    private String stateAbbreviation;
    private String stateName;
    private BigDecimal taxRate;

    public Tax(String stateName) {
        this.stateAbbreviation = stateName;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
}