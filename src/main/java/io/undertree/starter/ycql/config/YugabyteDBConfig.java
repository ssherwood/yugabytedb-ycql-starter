package io.undertree.starter.ycql.config;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
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
import java.util.UUID;

/**
 * Example Spring Boot configuration extensions for customizing the YCQL
 * driver and the YCQL Session.
 * <p>
 * Ref: CassandraDataAutoConfiguration and CassandraAutoConfiguration
 */
@Configuration
@EnableCassandraRepositories(basePackages = "io.undertree.starter.ycql", repositoryBaseClass = ExtendedCassandraRepositoryImpl.class)
public class YugabyteDBConfig {

    /**
     * Use this to override/configure driver level settings that can not be specified
     * with Spring Boot config parameters.
     *
     * @return
     */
    @Bean
    public DriverConfigLoaderBuilderCustomizer customize() {
        return builder -> builder
                .withString(DefaultDriverOption.PROTOCOL_VERSION, "V4")
                .withClass(DefaultDriverOption.LOAD_BALANCING_POLICY_CLASS, PartitionAwarePolicy.class)
                .withClass(DefaultDriverOption.RETRY_POLICY_CLASS, DefaultRetryPolicy.class) // this is default, very conservative
                .withClass(DefaultDriverOption.RECONNECTION_POLICY_CLASS, ExponentialReconnectionPolicy.class)
                .withInt(DefaultDriverOption.RECONNECTION_BASE_DELAY, 1) // same as default
                .withInt(DefaultDriverOption.RECONNECTION_MAX_DELAY, 60)
                .withDuration(DefaultDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, Duration.ofSeconds(5));
    }


    /**
     *
     * @return
     */
    @Bean
    public CqlSessionBuilderCustomizer cqlSessionCustomizer() {
        return cqlSessionBuilder -> cqlSessionBuilder
                //.withNodeDistanceEvaluator() <- TODO find out if this will restrict connection to local DC only
                .withClientId(UUID.fromString("a78e660b-135e-4ba4-8415-a136e5cf372a"));
                //.withSslContext() <- better to use `spring.ssl.bundle.pem`
                // WARNING: don't override withConfigLoader() here - it breaks some SDC autoconfig properties
    }

    @Bean
    public CassandraTemplate cassandraTemplate(SessionFactory sessionFactory, CassandraConverter converter) {
        var template = new CassandraTemplate(sessionFactory, converter);
        // customize the template here:
        return template;
    }
}
