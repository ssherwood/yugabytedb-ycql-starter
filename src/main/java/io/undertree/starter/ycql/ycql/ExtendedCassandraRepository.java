package io.undertree.starter.ycql.ycql;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ExtendedCassandraRepository<T, ID> extends CassandraRepository<T, ID> {
    <S extends T> S saveWithTTL(S entity, int ttl);
}