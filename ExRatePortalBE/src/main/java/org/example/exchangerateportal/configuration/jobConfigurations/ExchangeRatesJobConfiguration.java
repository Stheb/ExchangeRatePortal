package org.example.exchangerateportal.configuration.jobConfigurations;

import org.example.exchangerateportal.jobs.PopulateExchangeRatesJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeRatesJobConfiguration extends BaseQuartzConfiguration {

    @Bean
    public JobDetail exchangeRateJobDetail() {
        return createJobDetail(
                PopulateExchangeRatesJob.class,
                "Populate Exchange Rate job",
                "populateExchangeRateJob"
        );
    }

    @Bean
    public Trigger exchangeRateTrigger(JobDetail exchangeRateJobDetail) {
        return createTrigger(
                exchangeRateJobDetail,
                "populateExchangeRateJob",
                "Populate Exchange Rate job",
                createSimpleScheduleBuilder()
        );
    }

    @Bean
    public Scheduler exchangeRateScheduler(Trigger exchangeRateTrigger, JobDetail exchangeRateJobDetail)
            throws SchedulerException {
        return createScheduler(exchangeRateJobDetail, exchangeRateTrigger);
    }

    @Override
    protected SimpleScheduleBuilder createSimpleScheduleBuilder() {
        return SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInHours(24)
                .repeatForever();
    }
}
