package io.undertree.starter.ycql.extensions;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.YugabyteDBYCQLContainer;
import org.testcontainers.containers.YugabyteDBYSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 *
 */
public class IntegrationTestExtension implements BeforeAllCallback, AfterAllCallback {
    static final DockerImageName YUGABYTEDB_IMAGE = DockerImageName.parse("yugabytedb/yugabyte:2.18.4.0-b52");
    static final String ENTRYPOINT = "bin/yugabyted start --background=false";

    static final YugabyteDBYCQLContainer ysqlDB = new YugabyteDBYCQLContainer(YUGABYTEDB_IMAGE)
            //.withInitScript()
            .withCommand(ENTRYPOINT);

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        ysqlDB.start();

        System.setProperty("spring.cassandra.contact-points", ysqlDB.getContactPoint().toString());
        System.setProperty("spring.cassandra.port", ysqlDB.getMappedPort(9042).toString());
        System.setProperty("spring.cassandra.username", ysqlDB.getUsername());
        System.setProperty("spring.cassandra.password", ysqlDB.getPassword());
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        // ysqlDB.stop();
        // ^ don't stop the container after each test class!
        // This assumes Ryuk will kill the YugabyteDB container later...
    }
}