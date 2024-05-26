package org.example.exchangerateportal.model.entity;

import jakarta.persistence.Entity;
import org.springframework.data.relational.core.mapping.Table;

@Entity
@Table(name = "EXCHANGERATE")
public class ExchangeEntity extends BaseEntity {

    private String code;

    private Double rate;

    private String date;

    public ExchangeEntity() {
    }

    public ExchangeEntity(String code, Double rate, String date) {
        this.code = code;
        this.rate = rate;
        this.date = date;
    }

    public ExchangeEntity(String code, Double rate, String date, Long id) {
        super(id);
        this.code = code;
        this.rate = rate;
        this.date = date;
    }

    public static ExchangeEntity of(String code, Double rate, String date) {
        return new ExchangeEntity(code, rate, date);
    }

    public static ExchangeEntity of(String code, Double rate, String date, Long id) {
        return new ExchangeEntity(code, rate, date, id);
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
        return "ExchangeEntity{"
                + "code='" + code + '\''
                + ", rate=" + rate
                + ", date='" + date + '\''
                + '}';
    }
}
