package org.isv.samples.shortener.service;

import org.isv.samples.shortener.storage.UrlInfo;
import org.isv.samples.shortener.storage.UrlStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {
    private final String baseUrl = "http://samples.com/1";
    @Mock
    private UrlStorage storage;
    private UrlShortenerService service;

    @BeforeEach
    void setUp() {
        service = new UrlShortenerService(baseUrl, storage);
    }

    @Test
    public void shouldAddUrl() {

        var originalUrl = "http://www.googles.com";
        var resultUrl = service.addUrl(originalUrl);
        verify(storage, times(1))
                .addUrl(argThat(url -> url.getUrl().equals(originalUrl) &&
                        url.getId() != null));
        Assertions.assertEquals(baseUrl, resultUrl.substring(0, baseUrl.length()));
        Assertions.assertTrue(Pattern.compile("[^/]+$").matcher(resultUrl).find());
    }

    @Test
    public void shouldReturnOriginalUrl() {
        var originalUrl = "http://www.googles.com";
        var id = UUID.randomUUID().toString();
        when(storage.getUrl(id)).thenReturn(Optional.of(UrlInfo.builder().id(id).url(originalUrl).build()));

        Assertions.assertEquals(originalUrl, service.getOriginalUrl(id).orElseThrow());
    }


}