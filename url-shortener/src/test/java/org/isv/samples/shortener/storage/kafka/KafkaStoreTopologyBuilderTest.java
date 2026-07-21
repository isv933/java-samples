package org.isv.samples.shortener.storage.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.TopologyTestDriver;
import org.isv.samples.shortener.settings.KafkaSettings;
import org.isv.samples.shortener.storage.UrlInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.retry.support.RetryTemplate;

import java.time.Duration;
import java.util.UUID;

class KafkaStoreTopologyBuilderTest {
    @Test
    public void testTopology() {
        var streamBuilder = new StreamsBuilder();
        var settings = new KafkaSettings();
        settings.setBootstrapServers("localhost:9092");
        settings.setShortenerStoreName("store_name");
        settings.setShortenerStoreTopic("store_topic");
        KafkaStoreTopologyBuilder.buildTopology(settings, streamBuilder);
        try (var testDriver = new TopologyTestDriver(streamBuilder.build())) {

            var inputTopic = testDriver.createInputTopic(settings.getShortenerStoreTopic(),
                    Serdes.String().serializer(), settings.getStoreSerde().serializer());

            var store = testDriver.<String, UrlInfo>getKeyValueStore(settings.getShortenerStoreName());
            var testUrl = UrlInfo.builder()
                    .id(UUID.randomUUID().toString()).url("http://sample").build();

            inputTopic.pipeInput(testUrl.getId(), testUrl);

            var retryTemplate = RetryTemplate.builder().maxAttempts((10))
                    .fixedBackoff(Duration.ofMillis(100)).build();


            var resultUrl = retryTemplate.execute((x) -> {
                var storeUrl = store.get(testUrl.getId());
                if (storeUrl == null) {
                    throw new RuntimeException("No key found");
                }

                return storeUrl;
            });

            Assertions.assertEquals(testUrl, resultUrl);
        }

    }
}