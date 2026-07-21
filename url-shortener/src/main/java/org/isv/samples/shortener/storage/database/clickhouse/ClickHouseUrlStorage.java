package org.isv.samples.shortener.storage.database.clickhouse;

import lombok.RequiredArgsConstructor;
import org.isv.samples.shortener.storage.UrlInfo;
import org.isv.samples.shortener.storage.UrlStorage;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
public class ClickHouseUrlStorage implements UrlStorage {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TableNameResolver tableNameResolver;

    @Override
    public void addUrl(UrlInfo urlInfo) {
        var query = String.format("INSERT INTO %s (id,url) SETTINGS async_insert=1, wait_for_async_insert=1 VALUES(:id, :url)",
                tableNameResolver.getTableName(UrlInfo.class));

        jdbcTemplate.update(query, Map.of(
                "id", urlInfo.getId(),
                "url", urlInfo.getUrl()));
    }

    @Override
    public Optional<UrlInfo> getUrl(String id) {
        try {
            var query =
                    String.format("SELECT id,url from %s WHERE id=:id", tableNameResolver.getTableName(UrlInfo.class));

            return Optional.ofNullable(jdbcTemplate.query(query,
                    Map.of("id", id), (ResultSetExtractor<UrlInfo>)
                            x -> UrlInfo.builder().id(x.getString("id"))
                                    .url(x.getString("url")).build()));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }
}
