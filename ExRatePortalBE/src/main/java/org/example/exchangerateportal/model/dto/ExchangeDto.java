package org.example.exchangerateportal.model.dto;

public class ExchangeDto {

    private final String code;

    private final Double rate;

    private final String date;

    public ExchangeDto(String code, Double rate, String date) {
        this.code = code;
        this.rate = rate;
        this.date = date;
    }

    public static ExchangeDto of(String code, Double rate, String date) {
        return new ExchangeDto(code, rate, date);
    }

    public String getCode() {
        return code;
    }

    public Double getRate() {
        return rate;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "ExchangeDto{"
                + "code='" + code + '\''
                + ", rate=" + rate
                + ", date='" + date + '\''
                + '}';
    }
}
