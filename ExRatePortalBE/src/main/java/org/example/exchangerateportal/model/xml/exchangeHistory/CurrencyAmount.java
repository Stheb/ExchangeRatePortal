package org.example.exchangerateportal.model.xml.exchangeHistory;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class CurrencyAmount {

    @JacksonXmlProperty(localName = "Ccy")
    private String currency;

    @JacksonXmlProperty(localName = "Amt")
    private Double amount;

    public String getCurrency() {
        return currency;
    }

    public Double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "CurrencyAmount{"
                + "currency='" + currency + '\''
                + ", amount=" + amount
                + '}';
    }
}
