package org.isv.samples.shortener.storage.database;

import org.isv.samples.shortener.storage.UrlInfo;
import org.junit.jupiter.api.Test;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;

import java.util.UUID;

import static org.mockito.Mockito.*;

class InsertRepositoryImplTest {

    @Test
    void shouldInsert() {
        var jdbcTemplate = mock(JdbcAggregateTemplate.class);
        var insertRepository = new InsertRepositoryImpl(jdbcTemplate);
        var url = UrlInfo.builder()
                .Id(UUID.randomUUID().toString()).Url("http://sample").build();
        insertRepository.insert(url);
        verify(jdbcTemplate, times(1)).insert(url);
    }
}