package org.isv.samples.shortener.ioc.database;

import org.isv.samples.shortener.storage.UrlStorage;
import org.isv.samples.shortener.storage.database.clickhouse.ClickHouseUrlStorage;
import org.isv.samples.shortener.storage.database.clickhouse.TableNameResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@Profile("clickhouse")
public class ClickhouseConfiguration {
    @Bean
    UrlStorage getUrlStorage(NamedParameterJdbcTemplate jdbcTemplate, TableNameResolver tableNameResolver) {
        return new ClickHouseUrlStorage(jdbcTemplate, tableNameResolver);
    }

}
