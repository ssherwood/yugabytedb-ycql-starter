package io.undertree.starter.ycql.ycql;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.data.cassandra.repository.query.CassandraEntityInformation;
import org.springframework.data.cassandra.repository.support.SimpleCassandraRepository;
import org.springframework.util.Assert;

/**
 * From https://stackoverflow.com/questions/34505376/ttl-support-in-spring-boot-application-using-spring-data-cassandra
 *
 * @param <T>
 * @param <ID>
 */
public class ExtendedCassandraRepositoryImpl<T, ID> extends SimpleCassandraRepository<T, ID> implements ExtendedCassandraRepository<T, ID> {

    private final CassandraOperations operations;
    private final CassandraEntityInformation<T, ID> entityInformation;

    public ExtendedCassandraRepositoryImpl(CassandraEntityInformation<T, ID> metadata, CassandraOperations operations) {
        super(metadata, operations);

        this.entityInformation = metadata;
        this.operations = operations;
    }

    public <S extends T> S saveWithTTL(S entity, int ttl) {
        Assert.notNull(entity, "Entity must not be null");
        operations.insert(entity, InsertOptions.builder().ttl(ttl).build());
        return entity;
    }
}