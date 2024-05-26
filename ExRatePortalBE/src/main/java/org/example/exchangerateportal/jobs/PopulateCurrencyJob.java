package org.example.exchangerateportal.jobs;

import org.example.exchangerateportal.service.CurrencyService;
import org.example.exchangerateportal.client.LbLtClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PopulateCurrencyJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(PopulateCurrencyJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LbLtClient lbLtClient = (LbLtClient) jobExecutionContext.getJobDetail().getJobDataMap().get("lbLtClient");
        CurrencyService currencyService = (CurrencyService) jobExecutionContext.getJobDetail().getJobDataMap().get("currencyService");

        lbLtClient.getAllCurrencies()
                .flatMap(currencyService::addAllCurrencies)
                .doOnSuccess(onSuccess -> {
                    LocalDateTime localDateTime = LocalDateTime.now();
                    logger.info("Currencies successfully added at {}", localDateTime);
                    return;
                })
                .doOnError(error -> logger.error("Couldn't add currencies", error))
                .subscribe();
    }

}
