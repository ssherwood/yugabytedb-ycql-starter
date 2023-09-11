package io.undertree.starter.ycql.config;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import org.springframework.boot.autoconfigure.cassandra.DriverConfigLoaderBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Example Spring Boot configuration extensions for customizing the YCQL
 * driver and the YCQL Session.
 */
@Configuration
public class YugabyteDBConfig {

    @Bean
    public DriverConfigLoaderBuilderCustomizer driverCustomizer() {
        return driverConfigBuilder -> driverConfigBuilder
                .withDuration(DefaultDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, Duration.ofSeconds(5));
    }

// This config isn't required since the default load balancer is already PartitionAware... but just in case
// additional configurations are required.

//    @Bean
//    public CqlSessionBuilderCustomizer cqlSessionCustomizer() {
//        return cqlSessionBuilder -> cqlSessionBuilder
//                .withConfigLoader(
//                        DriverConfigLoader.programmaticBuilder()
//                                .withClass(DefaultDriverOption.LOAD_BALANCING_POLICY_CLASS, PartitionAwarePolicy.class)
//                                .build()
//                );
//    }
}
