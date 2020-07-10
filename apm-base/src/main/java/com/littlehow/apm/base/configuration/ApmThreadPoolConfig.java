package com.littlehow.apm.base.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 主要是apm使用的一些线程池配置
 * 1.集成到系统中的jar包在数据上报以及心跳等使用的异步线程池
 * 2.apm默认收集系统中使用到的异步任务和定时任务的线程池配置
 * @author littlehow
 */
@Configuration
@Slf4j
public class ApmThreadPoolConfig {

    @Autowired
    private OuterProperties outerProperties;

    private static AtomicInteger idCreator = new AtomicInteger(1);

    @ConditionalOnMissingBean(ScheduledThreadPoolExecutor.class)
    @Bean("apmSchedulePool")
    public ScheduledThreadPoolExecutor createSchedulePool() {
        return new ScheduledThreadPoolExecutor(outerProperties.getSchedulePoolSize(), (runnable) ->
                new Thread(runnable, "apm-schedule-thread-" + idCreator.getAndIncrement())
        );
    }

    @Bean("apmThreadPoolExecutor")
    public ThreadPoolExecutor createThreadPool() {
        return new ThreadPoolExecutor(outerProperties.getThreadPoolSize(), outerProperties.getThreadPoolSize(),
                0, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(outerProperties.getThreadPoolQueueSize()),
                runnable -> new Thread(runnable, "apm-trace-thread-" + idCreator.getAndIncrement()), new MyDiscardPolicy());
    }

    /**
     * 线程池工作队列满后将直接抛弃
     */
    static class MyDiscardPolicy extends  ThreadPoolExecutor.DiscardPolicy {
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            log.warn("apm thread pool's queue is full");
        }
    }
}
