package org.isv.samples.shortener.ioc;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.isv.samples.shortener.settings.ApplicationSettings;
import org.isv.samples.shortener.storage.UrlInfo;
import org.isv.samples.shortener.storage.UrlStorage;
import org.isv.samples.shortener.storage.kafka.KafkaStoreTopologyBuilder;
import org.isv.samples.shortener.storage.kafka.KafkaUrlStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Properties;

@Configuration
@Lazy
public class KafkaConfiguration {


    @Bean("kafka")
    public Properties getKafkaProperties(ApplicationSettings settings) {
        var properties = new Properties();


        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, settings.getKafka().getBootstrapServers());
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "url-shortener-app");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "1000");

        return properties;
    }

    @Bean
    public KafkaStreams getKafkaStreams(ApplicationSettings settings,
                                        @Qualifier("kafka") Properties properties) {
        var streamsBuilder = new StreamsBuilder();
        KafkaStoreTopologyBuilder.buildTopology(settings.getKafka(), streamsBuilder);
        var kafkaStreams = new KafkaStreams(streamsBuilder.build(), properties);
        kafkaStreams.start();

        return kafkaStreams;
    }

    @Bean
    public KafkaTemplate<String, UrlInfo> getKafkaTemplate(ProducerFactory<String, UrlInfo> producerFactory) {

        return new KafkaTemplate<>(producerFactory);
    }


    @Bean
    public ProducerFactory<String, UrlInfo> producerFactory(ApplicationSettings settings) {
        return new DefaultKafkaProducerFactory<>(
                new java.util.HashMap<>() {{
                    put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, settings.getKafka().getBootstrapServers());
                    put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
                    put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                            settings.getKafka().getStoreSerde().serializer().getClass());
                }}
        );
    }

    @Bean("kafka")
    public UrlStorage getUrlStorage(ApplicationSettings settings, KafkaStreams streams,
                                    KafkaTemplate<String, UrlInfo> kafkaTemplate) {

        return new KafkaUrlStorage(streams,
                settings.getKafka().getShortenerStoreName(),
                settings.getKafka().getShortenerStoreTopic(), kafkaTemplate);

    }


}
