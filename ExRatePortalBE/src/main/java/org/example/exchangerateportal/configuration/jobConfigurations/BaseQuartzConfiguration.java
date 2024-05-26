package org.example.exchangerateportal.configuration.jobConfigurations;

import org.example.exchangerateportal.service.CurrencyService;
import org.example.exchangerateportal.service.ExchangeService;
import org.example.exchangerateportal.client.LbLtClient;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class BaseQuartzConfiguration {

    @Autowired
    private LbLtClient lbLtClient;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ExchangeService exchangeService;

    protected JobDetail createJobDetail(Class<? extends Job> jobClass, String jobDescription, String jobIdentity) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("lbLtClient", lbLtClient);
        jobDataMap.put("currencyService", currencyService);
        jobDataMap.put("exchangeService", exchangeService);

        return JobBuilder.newJob()
                .ofType(jobClass)
                .withDescription(jobDescription)
                .withIdentity(jobIdentity)
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    protected Trigger createTrigger(JobDetail jobDetail, String triggerIdentity, String triggerDescription, SimpleScheduleBuilder simpleScheduleBuilder) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(triggerIdentity)
                .withDescription(triggerDescription)
                .usingJobData(jobDetail.getJobDataMap())
                .withSchedule(simpleScheduleBuilder)
                .build();
    }

    protected Scheduler createScheduler(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
        defaultScheduler.scheduleJob(jobDetail, trigger);
        defaultScheduler.start();
        return defaultScheduler;
    }

    protected SimpleScheduleBuilder createSimpleScheduleBuilder() {
        return SimpleScheduleBuilder.simpleSchedule();
    }

}
