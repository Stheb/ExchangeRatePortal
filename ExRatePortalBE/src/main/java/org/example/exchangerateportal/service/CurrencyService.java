package org.example.exchangerateportal.service;

import org.example.exchangerateportal.model.dto.CurrencyDto;
import org.example.exchangerateportal.model.entity.CurrencyEntity;
import org.example.exchangerateportal.model.xml.currency.Currencies;
import org.example.exchangerateportal.model.xml.currency.CurrencyEntry;
import org.example.exchangerateportal.repository.CurrencyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CurrencyService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    @Autowired
    private CurrencyRepository currencyRepository;

    public Flux<CurrencyDto> getAllCurrencies() {
        return currencyRepository.findAll()
                .map(this::convertToDto);
    }

    @Transactional
    public Mono<Void> addAllCurrencies(Currencies currencies) {

        return currencyRepository.findAllCurrencyCodes()
                .collectList()
                .flatMapMany(existingCurrencyCodes -> {

                    // Deal with only new currencies.
                    return Flux.fromIterable(currencies.getCurrencyEntries())
                            .filter(entry -> !existingCurrencyCodes.contains(entry.getCurrency()))
                            .map(this::convertToEntity);
                })
                .collectList()
                .flatMapMany(currencyRepository::saveAll)
                .doOnNext(savedEntities -> {
                    logger.info("Added new currency {} to the database", savedEntities);
                })
                .then();
    }

    private CurrencyDto convertToDto(CurrencyEntity currency) {
        return CurrencyDto.of(currency.getName(), currency.getCode());
    }

    // XML to Entity
    private CurrencyEntity convertToEntity(CurrencyEntry entry) {
        return CurrencyEntity.of(entry.getCurrencyNameEN(), entry.getCurrency());
    }
}
