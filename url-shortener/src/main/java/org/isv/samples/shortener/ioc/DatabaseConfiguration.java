package org.isv.samples.shortener.ioc;

import org.isv.samples.shortener.storage.UrlStorage;
import org.isv.samples.shortener.storage.database.DatabaseUrlStorage;
import org.isv.samples.shortener.storage.database.ShortenerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories(basePackages = "org.isv.samples.shortener.storage.**")
@Lazy
public class DatabaseConfiguration {

    @Bean("database")
    UrlStorage getUrlStorage(ShortenerRepository repository) {
        return new DatabaseUrlStorage(repository);
    }
}
