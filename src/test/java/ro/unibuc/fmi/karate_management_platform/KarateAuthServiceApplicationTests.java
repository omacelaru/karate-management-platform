package ro.unibuc.fmi.karate_management_platform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ro.unibuc.fmi.karate_management_platform.config.TestConfig;

@SpringBootTest
@Import(TestConfig.class)
class KarateAuthServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
