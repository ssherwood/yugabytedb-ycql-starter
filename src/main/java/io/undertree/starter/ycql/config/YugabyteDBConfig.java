package io.undertree.starter.ycql.config;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.internal.core.connection.ExponentialReconnectionPolicy;
import com.datastax.oss.driver.internal.core.retry.DefaultRetryPolicy;
import com.yugabyte.oss.driver.internal.core.loadbalancing.PartitionAwarePolicy;
import io.undertree.starter.ycql.ycql.ExtendedCassandraRepositoryImpl;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.autoconfigure.cassandra.DriverConfigLoaderBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.SessionFactory;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.time.Duration;

/**
 * Example Spring Boot configuration extensions for customizing the YCQL
 * driver and the YCQL Session.
 * <p>
 * Ref: CassandraDataAutoConfiguration and CassandraAutoConfiguration
 */
@Configuration
@EnableCassandraRepositories(basePackages = "io.undertree.starter.ycql", repositoryBaseClass = ExtendedCassandraRepositoryImpl.class)
public class YugabyteDBConfig {

    @Bean
    public DriverConfigLoaderBuilderCustomizer customize() {
        return builder -> builder
                .withString(DefaultDriverOption.PROTOCOL_VERSION, "V4")
                .withDuration(DefaultDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, Duration.ofSeconds(5));
    }

    /**
     * This config isn't required since the default load balancer is already PartitionAware... but just in case
     * additional configurations are required.
     */
    @Bean
    public CqlSessionBuilderCustomizer cqlSessionCustomizer() {
        return cqlSessionBuilder -> cqlSessionBuilder
                //.withSslContext() <- this can be done by boot's "spring.ssl.bundle.pem":
                .withConfigLoader(
                        DriverConfigLoader.programmaticBuilder()
                                .withClass(DefaultDriverOption.LOAD_BALANCING_POLICY_CLASS, PartitionAwarePolicy.class) // Yugabyte Tablet Aware
                                .withClass(DefaultDriverOption.RETRY_POLICY_CLASS, DefaultRetryPolicy.class) // this is default, very conservative
                                .withClass(DefaultDriverOption.RECONNECTION_POLICY_CLASS, ExponentialReconnectionPolicy.class)
                                .withInt(DefaultDriverOption.RECONNECTION_BASE_DELAY, 1) // same as default
                                .withInt(DefaultDriverOption.RECONNECTION_MAX_DELAY, 60) // same as default
                                .build()
                );
    }

    @Bean
    public CassandraTemplate cassandraTemplate(SessionFactory sessionFactory, CassandraConverter converter) {
        var template = new CassandraTemplate(sessionFactory, converter);
        template.setUsePreparedStatements(true); // TODO just to test perf differences (if any)
        // so far it seems like ~5% CPU savings on YBDB with it enabled
        return template;
    }
}
