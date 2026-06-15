package org.isv.samples.shortener.storage.database;

public interface InsertRepository<T> {
    void insert(T value);
}
