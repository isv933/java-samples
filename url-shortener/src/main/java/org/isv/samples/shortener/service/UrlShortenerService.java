package org.isv.samples.shortener.service;


import lombok.RequiredArgsConstructor;
import org.isv.samples.shortener.storage.UrlInfo;
import org.isv.samples.shortener.storage.UrlStorage;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class UrlShortenerService {
    private final String baseUrl;
    private final UrlStorage storage;

    @Async
    public CompletableFuture<String> addUrl(String originalUrl) {
        var dbUrl = UrlInfo.builder()
                .id(UUID.randomUUID().toString()).url(originalUrl).build();
        storage.addUrl(dbUrl);
        return CompletableFuture.completedFuture(generateShortUrl(dbUrl));
    }

    public Optional<String> getOriginalUrl(String id) {
        return storage.getUrl(id).map(UrlInfo::getUrl);
    }

    private String generateShortUrl(UrlInfo urlInfo) {
        return baseUrl + "/" + urlInfo.getId();
    }
}
