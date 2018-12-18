package cn.zhengyk.sync.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: Yakai Zheng（zhengyk@cloud-young.com）
 * @date: Created on 2018/12/18
 * @description:
 * @version: 1.0
 */
@EnableAsync
@Configuration
public class ThreadPoolConfig {

    @Bean("canal")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("canal-");
        // CallerRunsPolicy 重试添加当前的任务，他会自动重复调用execute()方法
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
