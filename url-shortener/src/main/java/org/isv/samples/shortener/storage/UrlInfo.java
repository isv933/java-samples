package org.isv.samples.shortener.storage;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("shortener_url")
@Jacksonized
public class UrlInfo {
    @Id
    private final String id;
    private final String url;
}
