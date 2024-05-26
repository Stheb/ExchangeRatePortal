package org.example.exchangerateportal.repository;

import org.example.exchangerateportal.model.entity.CurrencyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CurrencyRepository extends R2dbcRepository<CurrencyEntity, Integer> {

    @Query("SELECT ce.code FROM CURRENCY ce")
    Flux<String> findAllCurrencyCodes();

}
