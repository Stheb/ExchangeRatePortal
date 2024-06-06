package org.example.exchangerateportal.service;

import org.example.exchangerateportal.client.LbLtClient;
import org.example.exchangerateportal.model.dto.ExchangeDto;
import org.example.exchangerateportal.model.entity.ExchangeRateHistory;
import org.example.exchangerateportal.model.xml.exchangeHistory.CurrencyAmount;
import org.example.exchangerateportal.model.xml.exchangeHistory.ExchangeRates;
import org.example.exchangerateportal.model.xml.exchangeHistory.FxRate;
import org.example.exchangerateportal.repository.ExchangeRateHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExchangeService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeService.class);

    @Autowired
    private ExchangeRateHistoryRepository exchangeRateHistoryRepository;

    @Autowired
    private LbLtClient lbLtClient;

    public Flux<ExchangeDto> getExchangeRateToday() {
        return exchangeRateHistoryRepository.findLatestRatesForAllCodes()
                .map(this::convertToDto);
    }

    @Transactional
    public Flux<ExchangeDto> getExchangeRateHistoryByCurrencyAndStartDate(String currency, String startDate, Boolean forceHistoryFromLbLt) {

        if (forceHistoryFromLbLt) {
            return lbLtClient.getExchangeRateHistoryForCurrency(currency, "2020-01-01")
                    .flatMapMany(rates -> Flux.fromIterable(rates.getRates())
                            .map(this::convertToHistoryItem)
                            .flatMap(newRate -> exchangeRateHistoryRepository.findByDateAndCode(newRate.getDate(), newRate.getCode())
                                    .switchIfEmpty(exchangeRateHistoryRepository.save(newRate))
                                    .then(Mono.empty())
                            )
                    )
                    .thenMany(exchangeRateHistoryRepository.findAllByCodeAndDateAfter(currency, LocalDate.parse(startDate)))
                    .map(this::convertToDto);
        } else {
            return this.exchangeRateHistoryRepository.findAllByCodeAndDateAfter(currency, LocalDate.parse(startDate))
                    .map(this::convertToDto);
        }
    }

    private ExchangeDto convertToDto(ExchangeRateHistory exchangeRateHistory) {
        return new ExchangeDto(exchangeRateHistory.getCode(), exchangeRateHistory.getRate(), exchangeRateHistory.getDate().toString());
    }

    @Transactional
    public Mono<Void> updateExchangeRateToday(ExchangeRates exchangeRates) {

        List<ExchangeRateHistory> convertedEntity = exchangeRates.getRates().stream()
                .map(this::convertToHistoryItem).toList();

        return Flux.fromIterable(convertedEntity)
                .flatMap(historyItem ->
                        exchangeRateHistoryRepository.findByDateAndCode(historyItem.getDate(), historyItem.getCode())
                                .switchIfEmpty(Mono.just(historyItem))
                )
                .filter(historyItem -> historyItem.getId() == null)
                .collectList()
                .flatMapMany(exchangeRateHistoryRepository::saveAll)
                .doOnNext(savedEntities -> logger.info("Updated today's exchange rate {} to the database", savedEntities))
                .then();
    }

    private ExchangeRateHistory convertToHistoryItem(FxRate fxRate) {

        List<CurrencyAmount> currencyAmountList = fxRate.getCurrencyAmountList();
        return new ExchangeRateHistory(
                currencyAmountList.get(1).getCurrency(),
                currencyAmountList.get(1).getAmount(),
                LocalDate.parse(fxRate.getDateTime())
        );
    }

    public Mono<ExchangeDto> getExchangeRateForCurrency(String currency) {
        return exchangeRateHistoryRepository.findLatestByCode(currency)
                .map(this::convertToDto);
    }

}
