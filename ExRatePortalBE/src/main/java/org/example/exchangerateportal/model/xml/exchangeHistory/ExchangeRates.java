package org.example.exchangerateportal.model.xml.exchangeHistory;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "FxRates")
public class ExchangeRates {

    @JacksonXmlProperty(localName = "FxRate")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FxRate> rates;

    public List<FxRate> getRates() {
        return rates;
    }

    @Override
    public String toString() {
        return "ExchangeRates{"
                + "rates=" + rates
                + '}';
    }
}
