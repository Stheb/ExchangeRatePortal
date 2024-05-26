package org.example.exchangerateportal.service;

import org.example.exchangerateportal.client.LbLtClient;
import org.example.exchangerateportal.model.dto.ExchangeDto;
import org.example.exchangerateportal.model.entity.ExchangeEntity;
import org.example.exchangerateportal.model.xml.exchangeHistory.CurrencyAmount;
import org.example.exchangerateportal.model.xml.exchangeHistory.ExchangeRates;
import org.example.exchangerateportal.model.xml.exchangeHistory.FxRate;
import org.example.exchangerateportal.repository.ExchangeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class ExchangeService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeService.class);

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private LbLtClient lbLtClient;

    // TODO remove
    public Flux<ExchangeDto> getExchangeRateToday() {
        return exchangeRepository.findAll()
                .map(this::convertToDto);
    }

    public Flux<ExchangeDto> getExchangeRateHistoryByCurrencyAndStartDate(String currency, String startDate) {
        return this.lbLtClient.getExchangeRateHistoryForCurrency(currency, startDate)
                .map(exchangeRates -> exchangeRates.getRates().stream()
                        .map(this::convertToDto).toList())
                .flatMapMany(Flux::fromIterable);
    }

    @Transactional
    public Mono<Void> updateExchangeRateToday(ExchangeRates exchangeRates) {
        List<ExchangeEntity> convertedEntity = exchangeRates.getRates().stream()
                .map(this::convertToEntity)
                .toList();

        return updateNecessaryEntities(convertedEntity)
                .flatMapMany(exchangeRepository::saveAll)
                .doOnNext(savedEntities -> {
                    logger.info("Updated today's exchange rate {} to the database", savedEntities);
                })
                .then();

    }

    private Mono<List<ExchangeEntity>> updateNecessaryEntities(List<ExchangeEntity> convertedEntity) {
        List<String> codes = convertedEntity.stream().map(ExchangeEntity::getCode).toList();

        return exchangeRepository.findAllByCodeIn(codes)
                .collectList()
                .flatMapMany(existingEntities ->
                        Flux.fromIterable(convertedEntity)
                                .map(coEntity -> {

                                    // Update rate when necessary
                                    Optional<ExchangeEntity> existing = existingEntities.stream()
                                            .filter(exEntity -> exEntity.getCode().equals(coEntity.getCode()))
                                            .findFirst();

                                    if (existing.isPresent()) {
                                        ExchangeEntity existingEntity = existing.get();
                                        return ExchangeEntity.of(existingEntity.getCode(), coEntity.getRate(), coEntity.getDate(), existingEntity.getId());
                                    } else {
                                        return coEntity;
                                    }
                                })).collectList();
    }

    public Mono<ExchangeDto> getExchangeRateForCurrency(String currency) {
        return exchangeRepository.findByCode(currency)
                .map(this::convertToDto);
    }

    private ExchangeDto convertToDto(FxRate fxRate) {
        List<CurrencyAmount> currencyAmountList = fxRate.getCurrencyAmountList();
        return ExchangeDto.of(
                currencyAmountList.get(1).getCurrency(),
                currencyAmountList.get(1).getAmount(),
                fxRate.getDateTime()
        );
    }

    private ExchangeDto convertToDto(ExchangeEntity exchange) {
        return ExchangeDto.of(exchange.getCode(), exchange.getRate(), exchange.getDate());
    }

    // XML to entity
    private ExchangeEntity convertToEntity(FxRate fxRate) {
        // TODO get(1) method is super bad here. Need to rethink this part!
        List<CurrencyAmount> currencyAmountList = fxRate.getCurrencyAmountList();
        return ExchangeEntity.of(
                currencyAmountList.get(1).getCurrency(),
                currencyAmountList.get(1).getAmount(),
                fxRate.getDateTime()
        );
    }

}
