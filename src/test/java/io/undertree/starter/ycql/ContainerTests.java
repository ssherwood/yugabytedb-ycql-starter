package io.undertree.starter.ycql;

import io.undertree.starter.ycql.extensions.IntegrationTestExtension;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@SpringBootTest
@ExtendWith(IntegrationTestExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class ContainerTests {
    @Test
    void loadYCQLTestContainer() {
    }
}
