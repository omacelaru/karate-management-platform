package ro.unibuc.fmi.karate_management_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KarateAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KarateAuthServiceApplication.class, args);
	}

}
