package io.undertree.starter.ycql;

import io.undertree.starter.ycql.extensions.IntegrationTestExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(IntegrationTestExtension.class)
class StarterApplicationTests {

	@Test
	void contextLoads() {
	}

}
