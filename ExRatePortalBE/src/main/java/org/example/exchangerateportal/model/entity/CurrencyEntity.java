package org.example.exchangerateportal.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.springframework.data.relational.core.mapping.Table;

@Entity
@Table(name = "CURRENCY")
public class CurrencyEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String code;

    private String name;

    public CurrencyEntity() {
    }

    public CurrencyEntity(String name, String code) {
        this.code = code;
        this.name = name;
    }

    public static CurrencyEntity of(String name, String code) {
        return new CurrencyEntity(name, code);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CurrencyEntity{"
                + "code='" + code + '\''
                + ", name='" + name + '\''
                + '}';
    }
}
