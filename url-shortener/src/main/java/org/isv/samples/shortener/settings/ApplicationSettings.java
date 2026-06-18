package org.isv.samples.shortener.settings;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "shortener")
public class ApplicationSettings {
    private String baseUrl;
    private String storageType;
    private KafkaSettings kafka = new KafkaSettings();
}
