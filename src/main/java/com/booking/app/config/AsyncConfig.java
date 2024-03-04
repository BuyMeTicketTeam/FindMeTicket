package com.booking.app.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Log4j2
public class AsyncConfig implements AsyncConfigurer {

    @Override
    @Bean(name = "asyncExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(15);
        executor.setMaxPoolSize(200);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("custom-async-");
        executor.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor e) -> {
            log.error("Task Rejected: Thread pool is full. Increase the thread pool size.");
        });
        executor.initialize();
        return executor;
    }

}