package io.undertree.starter.ycql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.YugabyteDBYCQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestStarterApplication {

    @Bean
    @ServiceConnection
    YugabyteDBYCQLContainer ycqlContainer() {
        return new YugabyteDBYCQLContainer(DockerImageName.parse("yugabytedb/yugabyte:latest"));
    }

    public static void main(String[] args) {
        SpringApplication.from(StarterApplication::main).with(TestStarterApplication.class).run(args);
    }
}
