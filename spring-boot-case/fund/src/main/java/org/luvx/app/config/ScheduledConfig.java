package org.luvx.app.config;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class ScheduledConfig implements SchedulingConfigurer {
    private final int size = 10;

    @Resource
    private ScheduledExecutorService executorService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(executorService);
    }

    @Bean
    public ScheduledExecutorService setExecutor() {
        return new ScheduledThreadPoolExecutor(size, new ThreadFactoryBuilder()
                .setNameFormat("schedule-executor-%d")
                .build());
    }
}
