package org.isv.samples.shortener.settings;


import lombok.Data;
import org.apache.kafka.common.serialization.Serde;
import org.isv.samples.shortener.storage.UrlInfo;
import org.springframework.kafka.support.serializer.JsonSerde;

@Data
public class KafkaSettings {

    private final Serde<UrlInfo> storeSerde = new JsonSerde<>(UrlInfo.class);
    private String shortenerStoreTopic;
    private String shortenerStoreName;
    private String bootstrapServers;
}
