package com.SecondClass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @title: ThreadPoolTaskConfig
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/11/15 20:38
 **/

@Configuration
@EnableAsync
public class ThreadPoolTaskConfig {

    private static final int corePoolSize = 10;       		// 核心线程数（默认线程数）
    private static final int maxPoolSize = 100;			    // 最大线程数
    private static final int keepAliveTime = 10;			// 允许线程空闲时间（单位：默认为秒）
    private static final int queueCapacity = 200;			// 缓冲队列数
    private static final String threadNamePrefix = "Async-Service-"; // 线程池名前缀

    @Bean(name = "taskExecutor") // bean的名称，默认为首字母小写的方法名
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);
        // 设置线程池的饱和策略
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        // 如果@Bean 就不需手动，会自动InitializingBean的afterPropertiesSet来调initialize
//        executor.initialize();
        return executor;
    }
}