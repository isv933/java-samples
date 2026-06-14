package org.isv.samples.shortener.database;

public interface UrlStorage {
    void addUrl(String id, String url);
    String getUrl(String id);
}
