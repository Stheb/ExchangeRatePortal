package org.example.exchangerateportal.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;

@Entity
@Table(name = "EXCHANGE_RATE_HISTORY")
public class ExchangeRateHistory extends BaseEntity {

    @Column(value = "CURRENCY_CODE")
    private String code;

    private Double rate;

    private LocalDate date;

    public ExchangeRateHistory() {
    }

    public ExchangeRateHistory(String code, Double rate, LocalDate date) {
        this.code = code;
        this.rate = rate;
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public Double getRate() {
        return rate;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "ExchangeRateHistory{"
                + "code='" + code + '\''
                + ", rate=" + rate
                + ", date='" + date + '\''
                + '}';
    }
}
