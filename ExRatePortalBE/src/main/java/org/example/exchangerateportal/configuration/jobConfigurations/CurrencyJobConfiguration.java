package org.example.exchangerateportal.configuration.jobConfigurations;

import org.example.exchangerateportal.jobs.PopulateCurrencyJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CurrencyJobConfiguration extends BaseQuartzConfiguration {

    @Bean
    public JobDetail currencyJobDetail() {
        return createJobDetail(
                PopulateCurrencyJob.class,
                "Populate currency table job",
                "populateCurrencyJob"
        );
    }

    @Bean
    public Trigger currencyTrigger(JobDetail currencyJobDetail) {
        return createTrigger(
                currencyJobDetail,
                "populateCurrencyTable",
                "Populate currency table trigger",
                createSimpleScheduleBuilder()
        );
    }

    @Bean
    public Scheduler currencyScheduler(Trigger currencyTrigger, JobDetail currencyJobDetail)
            throws SchedulerException {
        return createScheduler(currencyJobDetail, currencyTrigger);
    }

}
