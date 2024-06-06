package org.example.exchangerateportal.client;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.exchangerateportal.model.xml.currency.Currencies;
import org.example.exchangerateportal.model.xml.exchangeHistory.ExchangeRates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;


@Service
public class LbLtClient {

    private static final Logger serviceLogger = LoggerFactory.getLogger(LbLtClient.class);

    private final String DEFAULT_EXCHANGE_RATE_TYPE = "EU";

    private final LocalDate MINIMUM_START_DATE = LocalDate.parse("2020-01-01");

    @Value("${exchange.rate.uri:/getFxRatesForCurrency}")
    private String exchangeRateForCurrencyUri;

    @Value("${currency.uri:/getCurrencyList}")
    private String currencyListUri;

    @Value("${current.exchange.rate.uri:/getCurrentFxRates}")
    private String currentFxRatesUri;

    @Autowired
    private WebClient lbLtWebClient;

    @Autowired
    private XmlMapper xmlMapper;

    public Mono<Currencies> getAllCurrencies() {
        return lbLtWebClient.get()
                .uri(currencyListUri)
                .accept(MediaType.TEXT_XML)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(xmlString -> deserializeXml(xmlString, Currencies.class))
                .onErrorResume(throwable -> handleError(new Currencies(), throwable));
    }

    public Mono<ExchangeRates> getExchangeRateHistoryForCurrency(String currency, String startDate) {
        LocalDate today = LocalDate.now();

        String startDateString = compareDates(startDate);

        return lbLtWebClient.get()
                .uri(uriBuilder -> uriBuilder.path(exchangeRateForCurrencyUri)
                        .queryParam("tp", DEFAULT_EXCHANGE_RATE_TYPE)
                        .queryParam("ccy", currency)
                        .queryParam("dtFrom", startDateString)
                        .queryParam("dtTo", today)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(xmlString -> deserializeXml(xmlString, ExchangeRates.class))
                .onErrorResume(throwable -> handleError(new ExchangeRates(), throwable));
    }

    private String compareDates(String starDate) {
        String startDateString;
        LocalDate inputtedDate = LocalDate.parse(starDate);
        if (inputtedDate.isBefore(MINIMUM_START_DATE)) {
            startDateString = MINIMUM_START_DATE.toString();
        } else {
            startDateString = inputtedDate.toString();
        }
        return startDateString;
    }

    public Mono<ExchangeRates> getExchangeRateToday() {
        return lbLtWebClient.get()
                .uri(uriBuilder -> uriBuilder.path(currentFxRatesUri)
                        .queryParam("tp", DEFAULT_EXCHANGE_RATE_TYPE)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(xmlString -> deserializeXml(xmlString, ExchangeRates.class))
                .onErrorResume(throwable -> handleError(new ExchangeRates(), throwable));
    }

    private <T> Mono<T> deserializeXml(String xml, Class<T> clazz) {
        try {
            return Mono.just(xmlMapper.readValue(xml, clazz));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private <T> Mono<T> handleError(T newInstance, Throwable throwable) {
        serviceLogger.error(throwable.getMessage(), throwable);
        return Mono.just(newInstance);
    }

}
