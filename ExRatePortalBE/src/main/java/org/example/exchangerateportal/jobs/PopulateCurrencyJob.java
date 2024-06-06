package org.example.exchangerateportal.jobs;

import org.example.exchangerateportal.service.CurrencyService;
import org.example.exchangerateportal.client.LbLtClient;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PopulateCurrencyJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(PopulateCurrencyJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        LbLtClient lbLtClient = (LbLtClient) jobExecutionContext.getJobDetail().getJobDataMap().get("lbLtClient");
        CurrencyService currencyService = (CurrencyService) jobExecutionContext.getJobDetail().getJobDataMap().get("currencyService");

        lbLtClient.getAllCurrencies()
                .flatMap(currencyService::addAllCurrencies)
                .doOnSuccess(onSuccess -> {
                    LocalDateTime localDateTime = LocalDateTime.now();
                    logger.info("Currencies successfully added at {}", localDateTime);
                    triggerExchangeRateJob(jobExecutionContext);
                })
                .doOnError(error -> logger.error("Couldn't add currencies", error))
                .subscribe();
    }

    private void triggerExchangeRateJob(JobExecutionContext jobExecutionContext) {
        try {

            PopulateExchangeRatesJob populateExchangeRatesJob = new PopulateExchangeRatesJob();
            populateExchangeRatesJob.execute(jobExecutionContext);

        } catch (SchedulerException e) {
            logger.error("Error while scheduling PopulateExchangeRatesJob", e);
        }
    }

}
