package org.example.exchangerateportal.model.xml.exchangeHistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FxRate {

    @JacksonXmlProperty(localName = "Dt")
    private String dateTime;

    @JacksonXmlProperty(localName = "CcyAmt")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<CurrencyAmount> currencyAmountList;

    public String getDateTime() {
        return dateTime;
    }

    public List<CurrencyAmount> getCurrencyAmountList() {
        return currencyAmountList;
    }

    @Override
    public String toString() {
        return "FxRate{"
                + "dateTime='" + dateTime + '\''
                + ", currencyAmountList=" + currencyAmountList
                + '}';
    }
}
