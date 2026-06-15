package org.isv.samples.shortener.storage.database;


import lombok.RequiredArgsConstructor;
import org.isv.samples.shortener.storage.UrlInfo;
import org.isv.samples.shortener.storage.UrlStorage;

import java.util.Optional;

@RequiredArgsConstructor
public class DatabaseUrlStorage implements UrlStorage {
    private final ShortenerRepository repository;

    @Override
    public void addUrl(UrlInfo urlInfo) {
        repository.insert(urlInfo);
    }

    @Override
    public Optional<UrlInfo> getUrl(String id) {
        return repository.findById(id);
    }

}
