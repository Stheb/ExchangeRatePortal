package org.example.exchangerateportal.controller;

import org.example.exchangerateportal.model.dto.CurrencyDto;
import org.example.exchangerateportal.model.dto.ExchangeDto;
import org.example.exchangerateportal.service.CurrencyService;
import org.example.exchangerateportal.service.ExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ExchangeRateController {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateController.class); ;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/currencies")
    public Flux<CurrencyDto> getAllCurrencies() {
        return currencyService.getAllCurrencies()
                .doOnNext(currencyDto -> logger.info("Received currency: {}", currencyDto));
    }

    // TODO dont think we'll need this API.
    @GetMapping("/exchangeRateToday")
    public Flux<ExchangeDto> getExchangeRatesForToday() {
        return exchangeService.getExchangeRateToday()
                .doOnNext(exchangeRate -> logger.info("Retrieved today's exchange rate: {}", exchangeRate));
    }

    @GetMapping("/exchangeRateHistory/{currency}/{startDate}")
    public Flux<ExchangeDto> getExchangeRateHistory(@PathVariable String currency, @PathVariable String startDate) {
        return exchangeService.getExchangeRateHistoryByCurrencyAndStartDate(currency, startDate)
                .doOnNext(history -> logger.info("Retrieved exchange rate history item for {}: {}", currency, history));
    }

    @GetMapping("/exchangeRateFor/{currency}")
    public Mono<ExchangeDto> getExchangeRateForCurrency(@PathVariable(name = "currency") String currency) {
        return exchangeService.getExchangeRateForCurrency(currency)
                .doOnNext(rates -> logger.info("Retrieved exchange rate for {}: with rates {}", currency, rates));
    }



}
