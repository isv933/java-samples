package org.isv.samples.shortener.storage.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.isv.samples.shortener.settings.KafkaSettings;
import org.isv.samples.shortener.storage.UrlInfo;

public class KafkaStoreTopologyBuilder {


    public static void buildTopology(KafkaSettings settings, StreamsBuilder streamsBuilder) {

        streamsBuilder.globalTable(settings.getShortenerStoreTopic(),
                Consumed.with(Serdes.String(), settings.getStoreSerde()),
                Materialized.<String, UrlInfo, KeyValueStore<Bytes, byte[]>>as(settings.getShortenerStoreName())
                        .withKeySerde(Serdes.String())
                        .withValueSerde(settings.getStoreSerde()));


    }

}
