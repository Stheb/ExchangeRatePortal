package org.example.exchangerateportal.jobs;

import org.example.exchangerateportal.service.ExchangeService;
import org.example.exchangerateportal.client.LbLtClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class PopulateExchangeRatesJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(PopulateExchangeRatesJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LbLtClient lbLtClient = (LbLtClient) jobExecutionContext.getJobDetail().getJobDataMap().get("lbLtClient");
        ExchangeService exchangeService = (ExchangeService) jobExecutionContext.getJobDetail().getJobDataMap().get("exchangeService");

        lbLtClient.getExchangeRateToday()
                .flatMap(exchangeService::updateExchangeRateToday)
                .doOnSuccess(onSuccess -> {
                    LocalDateTime localDateTime = LocalDateTime.now();
                    logger.info("Exchange rates updated successfully at {}", localDateTime);
                    return;
                })
                .doOnError(error -> logger.error("Error updating exchange rates", error))
                .subscribe();
    }
}
