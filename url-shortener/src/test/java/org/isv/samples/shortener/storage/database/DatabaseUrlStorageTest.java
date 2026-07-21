package org.isv.samples.shortener.storage.database;

import org.isv.samples.shortener.storage.UrlInfo;
import org.isv.samples.shortener.storage.database.postgres.PostgresUrlStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseUrlStorageTest {
    @Mock
    private JdbcAggregateTemplate jdbcTemplate;

    @Test
    public void shouldAddUrl() {
        var storage = new PostgresUrlStorage(jdbcTemplate);
        var url = UrlInfo.builder()
                .id(UUID.randomUUID().toString()).url("http://sample").build();

        storage.addUrl(url);
        verify(jdbcTemplate, times(1)).insert(url);
    }

    @Test
    public void shouldGetUrl() {
        var storage = new PostgresUrlStorage(jdbcTemplate);
        var url = UrlInfo.builder()
                .id(UUID.randomUUID().toString()).url("http://sample").build();
        when(jdbcTemplate.findById(url.getId(), UrlInfo.class)).thenReturn(url);
        Assertions.assertEquals(Optional.of(url), storage.getUrl(url.getId()));
    }


}
