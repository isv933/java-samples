package org.isv.samples.shortener.storage;

import java.util.Optional;

public interface UrlStorage {
    void addUrl(UrlInfo urlInfo);

    Optional<UrlInfo> getUrl(String id);
}
