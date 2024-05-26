package org.example.exchangerateportal.model.xml.currency;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyEntry {

    @JacksonXmlProperty(localName = "Ccy")
    private String currency;

    @JacksonXmlProperty(localName = "CcyNm")
    private String currencyNameEN;

    public String getCurrency() {
        return currency;
    }

    public String getCurrencyNameEN() {
        return currencyNameEN;
    }

    @Override
    public String toString() {
        return "CurrencyEntry{"
                + "currency='" + currency + '\''
                + ", currencyNameEN='" + currencyNameEN + '\''
                + '}';
    }
}
