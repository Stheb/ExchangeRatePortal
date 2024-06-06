package org.example.exchangerateportal.repository;

import org.example.exchangerateportal.model.entity.ExchangeRateHistory;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface ExchangeRateHistoryRepository extends R2dbcRepository<ExchangeRateHistory, Long> {

    @Query("SELECT erh.* FROM EXCHANGE_RATE_HISTORY erh WHERE erh.date >= :date AND erh.currency_code = :code")
    Flux<ExchangeRateHistory> findAllByCodeAndDateAfter(String code, LocalDate date);

    Mono<ExchangeRateHistory> findByDateAndCode(LocalDate date, String currency);

    @Query("SELECT erh.* FROM EXCHANGE_RATE_HISTORY erh WHERE erh.date = (SELECT MAX(e2.date) FROM EXCHANGE_RATE_HISTORY e2 WHERE e2.currency_code = erh.currency_code)")
    Flux<ExchangeRateHistory> findLatestRatesForAllCodes();

    @Query("SELECT erh.* FROM Exchange_Rate_History erh WHERE erh.currency_code = :code ORDER BY erh.date DESC LIMIT 1")
    Mono<ExchangeRateHistory> findLatestByCode(String code);
}
