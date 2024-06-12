package com.common.config;

import com.common.job.TaskJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    private static final String ID = "SUMMERDAY";

    @Bean
    public JobDetail jobDetail1() {
        return JobBuilder.newJob(TaskJob.class)
                .withIdentity(ID + " 01")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger1(@Autowired JobDetail jobDetail) {
        // 简单的调度计划的构造器
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5) // 频率
                .repeatForever(); // 次数

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(ID + " 01Trigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
