package org.isv.samples.shortener.storage.database;

import org.isv.samples.shortener.storage.UrlInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseUrlStorageTest {
    @Mock
    private ShortenerRepository repository;

    @Test
    public void shouldAddUrl() {
        var storage = new DatabaseUrlStorage(repository);
        var url = new UrlInfo(UUID.randomUUID().toString(), "http://sample");
        storage.addUrl(url);
        verify(repository, times(1)).insert(url);
    }

    @Test
    public void shouldGetUrl() {
        var storage = new DatabaseUrlStorage(repository);
        var url = new UrlInfo(UUID.randomUUID().toString(), "http://sample");
        when(storage.getUrl(url.getId())).thenReturn(Optional.of(url));
        Assertions.assertEquals(Optional.of(url), storage.getUrl(url.getId()));


    }


}
