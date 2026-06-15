package org.isv.samples.shortener.storage.database;

import org.isv.samples.shortener.storage.UrlInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShortenerRepository extends CrudRepository<UrlInfo, String>, InsertRepository<UrlInfo> {
}
