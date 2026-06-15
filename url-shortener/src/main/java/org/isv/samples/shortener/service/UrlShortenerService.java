package org.isv.samples.shortener.service;


import lombok.RequiredArgsConstructor;
import org.isv.samples.shortener.storage.UrlInfo;
import org.isv.samples.shortener.storage.UrlStorage;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class UrlShortenerService {
    private final String baseUrl;
    private final UrlStorage storage;

    public String addUrl(String originalUrl) {
        var dbUrl = new UrlInfo(UUID.randomUUID().toString(), originalUrl);
        storage.addUrl(dbUrl);
        return generateShortUrl(dbUrl);
    }

    public Optional<String> getOriginalUrl(String id) {
        return storage.getUrl(id).map(UrlInfo::getUrl);
    }

    private String generateShortUrl(UrlInfo urlInfo) {
        return baseUrl + "/" + urlInfo.getId();
    }
}
