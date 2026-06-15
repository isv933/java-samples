package org.isv.samples.shortener.storage;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("shortener_url")
public class UrlInfo {
    @Id
    private final String Id;
    private final String Url;
}
