package org.isv.samples.shortener.ioc.database;

import org.isv.samples.shortener.storage.UrlStorage;
import org.isv.samples.shortener.storage.database.postgres.PostgresUrlStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;

@Configuration
@Profile("postgres")
public class PostgresConfiguration {
    @Bean
    UrlStorage getUrlStorage(JdbcAggregateTemplate jdbcTemplate) {
        return new PostgresUrlStorage(jdbcTemplate);
    }

}
