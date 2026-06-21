package org.isv.samples.shortener.storage.kafka;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.isv.samples.shortener.storage.UrlInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class KafkaUrlStorageTest {

    private final String topicName = "storeTopic";
    private final String storeName = "storeName";
    @Mock
    private KafkaStreams streams;
    @Mock
    private KafkaTemplate<String, UrlInfo> kafkaTemplate;
    @Mock
    private ReadOnlyKeyValueStore<String, UrlInfo> store;
    private KafkaUrlStorage kafkaStorage;

    @BeforeEach
    void setup() {
        kafkaStorage = new KafkaUrlStorage(streams, storeName, topicName, kafkaTemplate);
    }


    @Test
    public void shouldAddUrl() {
        var testUrl = UrlInfo.builder().Id(UUID.randomUUID().toString()).Url("http://sample").build();

        var sendResult = mock(CompletableFuture.class);

        when(kafkaTemplate.send(any(), any(), any())).thenReturn(sendResult);
        kafkaStorage.addUrl(testUrl);
        verify(kafkaTemplate, times(1)).send(topicName, testUrl.getId(), testUrl);

        try {
            verify(sendResult, times(1)).get();
        } catch (ExecutionException | InterruptedException | CancellationException ex0) {
            Assertions.fail();
        }
    }

    @Test
    public void shouldGetUrl() {
        var id = UUID.randomUUID().toString();
        when(streams.store(any())).thenReturn(store);

        var testUrl = UrlInfo.builder().Id(UUID.randomUUID().toString()).Url("http://sample").build();

        when(store.get(id)).thenReturn(testUrl);

        Assertions.assertEquals(testUrl, kafkaStorage.getUrl(id).orElseThrow());
        Assertions.assertTrue(kafkaStorage.getUrl("not_exist").isEmpty());
        verify(streams, times(2)).store(argThat(arg ->

                arg.storeName().equals(storeName) &&
                        arg.queryableStoreType() instanceof QueryableStoreTypes.KeyValueStoreType));

    }


}