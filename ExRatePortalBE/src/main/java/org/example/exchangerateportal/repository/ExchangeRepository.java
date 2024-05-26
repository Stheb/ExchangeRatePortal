package org.example.exchangerateportal.repository;

import org.example.exchangerateportal.model.entity.ExchangeEntity;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface ExchangeRepository extends R2dbcRepository<ExchangeEntity, Integer> {

    public Flux<ExchangeEntity> findAllByCodeIn(List<String> exchangeCodes);

    public Mono<ExchangeEntity> findByCode(String exchangeCode);
}
