package org.isv.samples.shortener.ioc;

import org.isv.samples.shortener.service.UrlShortenerService;
import org.isv.samples.shortener.settings.ApplicationSettings;
import org.isv.samples.shortener.storage.UrlStorage;
import org.isv.samples.shortener.storage.database.DatabaseUrlStorage;
import org.isv.samples.shortener.storage.database.ShortenerRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories(basePackages = "org.isv.samples.shortener.storage.**")
public class ApplicationConfiguration {

    @Bean
    UrlStorage getUrlStorage(ApplicationSettings settings, ApplicationContext context) {
        if (settings.getStorageType().equals("database")) {
            var repository = context.getBean(ShortenerRepository.class);
            return new DatabaseUrlStorage(repository);
        } else {
            throw new RuntimeException("Unsupported storage type " + settings.getStorageType());
        }
    }

    @Bean
    UrlShortenerService getShortenerService(ApplicationSettings settings, UrlStorage storage) {
        return new UrlShortenerService(settings.getBaseUrl(), storage);
    }

}
