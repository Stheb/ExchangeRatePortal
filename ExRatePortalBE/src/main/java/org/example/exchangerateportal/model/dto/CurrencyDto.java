package org.example.exchangerateportal.model.dto;

public class CurrencyDto {

    private final String name;

    private final String code;

    public CurrencyDto(String currency, String code) {
        this.name = currency;
        this.code = code;
    }

    public static CurrencyDto of(String currency, String code) {
        return new CurrencyDto(currency, code);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "CurrencyDto{"
                + "currency='" + name + '\''
                + ", code='" + code + '\''
                + '}';
    }
}
