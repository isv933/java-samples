package org.isv.samples.shortener.storage;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UrlStorage {
    void addUrl(UrlInfo urlInfo);
    Optional<UrlInfo> getUrl(String id);
}
