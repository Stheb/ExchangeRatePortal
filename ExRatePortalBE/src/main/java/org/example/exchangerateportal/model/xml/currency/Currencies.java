package org.example.exchangerateportal.model.xml.currency;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;


@JacksonXmlRootElement(localName = "CcyTbl")
public class Currencies {

    @JacksonXmlProperty(localName = "CcyNtry")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<CurrencyEntry> currencyEntries;

    public List<CurrencyEntry> getCurrencyEntries() {
        return currencyEntries;
    }

    @Override
    public String toString() {
        return "Currencies{"
                + "currencyEntries=" + currencyEntries
                + '}';
    }
}
