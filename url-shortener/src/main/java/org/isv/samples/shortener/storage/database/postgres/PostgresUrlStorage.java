package org.isv.samples.shortener.storage.database.postgres;


import lombok.RequiredArgsConstructor;
import org.isv.samples.shortener.storage.UrlInfo;
import org.isv.samples.shortener.storage.UrlStorage;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;

import java.util.Optional;

@RequiredArgsConstructor
public class PostgresUrlStorage implements UrlStorage {
    private final JdbcAggregateTemplate jdbcTemplate;

    @Override
    public void addUrl(UrlInfo urlInfo) {
        jdbcTemplate.insert(urlInfo);
    }

    @Override
    public Optional<UrlInfo> getUrl(String id) {
        return Optional.ofNullable(jdbcTemplate.findById(id, UrlInfo.class));
    }

}
