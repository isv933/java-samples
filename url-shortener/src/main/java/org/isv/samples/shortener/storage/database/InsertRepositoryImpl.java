package org.isv.samples.shortener.storage.database;

import lombok.RequiredArgsConstructor;
import org.isv.samples.shortener.storage.UrlInfo;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class InsertRepositoryImpl implements InsertRepository<UrlInfo> {
    private final JdbcAggregateTemplate jdbcTemplate;

    @Override
    public void insert(UrlInfo value) {
        jdbcTemplate.insert(value);
    }
}
