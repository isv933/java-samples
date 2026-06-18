package org.isv.samples.shortener.storage.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.isv.samples.shortener.storage.UrlInfo;
import org.isv.samples.shortener.storage.UrlStorage;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

@RequiredArgsConstructor
public class KafkaUrlStorage implements UrlStorage {

    private final KafkaStreams streams;
    private final String storeName;
    private final String storeTopicName;
    private final KafkaTemplate<String, UrlInfo> kafkaTemplate;


    @Override
    public void addUrl(UrlInfo urlInfo) {
        kafkaTemplate.send(storeTopicName, urlInfo.getId(), urlInfo);
    }

    @Override
    public Optional<UrlInfo> getUrl(String id) {

        var store = streams.store(StoreQueryParameters
                .fromNameAndType(storeName,
                        QueryableStoreTypes.<String, UrlInfo>keyValueStore()));


        return Optional.ofNullable(store.get(id));
    }
}
