package org.isv.samples.shortener.ioc;

import org.isv.samples.shortener.ioc.database.PostgresConfiguration;
import org.isv.samples.shortener.service.UrlShortenerService;
import org.isv.samples.shortener.settings.ApplicationSettings;
import org.isv.samples.shortener.storage.UrlStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PostgresConfiguration.class, PostgresConfiguration.class, KafkaConfiguration.class})
public class ApplicationConfiguration {
    @Bean
    UrlShortenerService getShortenerService(ApplicationSettings settings, UrlStorage storage) {
        return new UrlShortenerService(settings.getBaseUrl(), storage);
    }

}
