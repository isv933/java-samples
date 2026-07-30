package org.isv.samples.shortener.ioc;

import org.isv.samples.shortener.ioc.database.PostgresConfiguration;
import org.isv.samples.shortener.service.UrlShortenerService;
import org.isv.samples.shortener.settings.ApplicationSettings;
import org.isv.samples.shortener.storage.UrlStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@Import({PostgresConfiguration.class, PostgresConfiguration.class, KafkaConfiguration.class})
@EnableAsync
public class ApplicationConfiguration {
    @Bean
    UrlShortenerService getShortenerService(ApplicationSettings settings, UrlStorage storage) {
        return new UrlShortenerService(settings.getBaseUrl(), storage);
    }

    @Bean
    Executor getExecutor(ApplicationSettings settings) {
        var executor = new ThreadPoolTaskExecutor();
        executor.setQueueCapacity(settings.getNumThreads()*2);
        executor.setCorePoolSize(settings.getNumThreads());
        executor.setMaxPoolSize(settings.getNumThreads()*2);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }

}
