package org.isv.samples.shortener.storage.kafka;

import org.isv.samples.shortener.storage.UrlInfo;
import org.isv.samples.shortener.storage.UrlStorage;

import java.util.Optional;

public class KafkaUrlStorage implements UrlStorage {
    @Override
    public void addUrl(UrlInfo urlInfo) {

    }

    @Override
    public Optional<UrlInfo> getUrl(String id) {
        return Optional.empty();
    }
}
