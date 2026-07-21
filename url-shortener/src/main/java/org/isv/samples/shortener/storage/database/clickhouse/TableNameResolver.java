package org.isv.samples.shortener.storage.database.clickhouse;


import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TableNameResolver {
    private final RelationalMappingContext mappingContext;

    public String getTableName(Class<?> clazz) {
        var entityMapper = mappingContext.getRequiredPersistentEntity(clazz);

        return entityMapper.getTableName().getReference();
    }
}