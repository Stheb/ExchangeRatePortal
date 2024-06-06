package org.example.exchangerateportal.controller;

import org.example.exchangerateportal.model.dto.CurrencyDto;
import org.example.exchangerateportal.model.dto.ExchangeDto;
import org.example.exchangerateportal.model.response.CurrencyRecord;
import org.example.exchangerateportal.model.response.ExchangeRecord;
import org.example.exchangerateportal.service.CurrencyService;
import org.example.exchangerateportal.service.ExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ExchangeRateController {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateController.class);

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/currencies")
    public Flux<CurrencyRecord> getAllCurrencies() {
        return currencyService.getAllCurrencies()
                .map(this::convertToRecord)
                .doOnNext(currencyDto -> logger.info("Received currency: {}", currencyDto));
    }

    @GetMapping("/exchangeRateToday")
    public Flux<ExchangeRecord> getExchangeRatesForToday() {
        return exchangeService.getExchangeRateToday()
                .map(this::convertToRecord)
                .doOnNext(exchangeRate -> logger.info("Retrieved today's exchange rate: {}", exchangeRate));
    }

    @GetMapping("/exchangeRateHistory/{currency}/{startDate}/{forceFromLbLt}")
    public Flux<ExchangeRecord> getExchangeRateHistory(@PathVariable String currency, @PathVariable String startDate, @PathVariable Boolean forceFromLbLt) {
        return exchangeService.getExchangeRateHistoryByCurrencyAndStartDate(currency, startDate, forceFromLbLt)
                .map(this::convertToRecord)
                .doOnNext(history -> logger.info("Retrieved exchange rate history item for {}: {}", currency, history));
    }

    @GetMapping(value = "/exchangeRateFor/{currency}")
    public Mono<ExchangeRecord> getExchangeRateForCurrency(@PathVariable(name = "currency") String currency) {
        return exchangeService.getExchangeRateForCurrency(currency)
                .map(this::convertToRecord)
                .doOnNext(rates -> logger.info("Retrieved exchange rate for {}: with rates {}", currency, rates));
    }

    private CurrencyRecord convertToRecord(CurrencyDto currencyDto) {
        return new CurrencyRecord(currencyDto.getName(), currencyDto.getCode());
    }

    private ExchangeRecord convertToRecord(ExchangeDto exchangeDto) {
        return new ExchangeRecord(exchangeDto.getCode(), exchangeDto.getRate(), exchangeDto.getDate());
    }

}
