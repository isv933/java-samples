package org.isv.samples.shortener.ioc;

import org.isv.samples.shortener.service.UrlShortenerService;
import org.isv.samples.shortener.settings.ApplicationSettings;
import org.isv.samples.shortener.storage.UrlStorage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
@Import({DatabaseConfiguration.class, KafkaConfiguration.class})
public class ApplicationConfiguration {

    @Bean
    @Primary
    UrlStorage getUrlStorage(ApplicationSettings settings, ApplicationContext context) {
        return context.getBean(settings.getStorageType(), UrlStorage.class);
    }

    @Bean
    UrlShortenerService getShortenerService(ApplicationSettings settings, UrlStorage storage) {
        return new UrlShortenerService(settings.getBaseUrl(), storage);
    }

}
