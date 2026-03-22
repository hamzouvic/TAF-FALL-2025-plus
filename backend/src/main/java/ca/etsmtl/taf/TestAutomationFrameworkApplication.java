package ca.etsmtl.taf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication(scanBasePackages = {"ca.etsmtl.taf"})
@EnableMongoAuditing
@EnableDiscoveryClient
public class TestAutomationFrameworkApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TestAutomationFrameworkApplication.class, args);
	}

	public void run(String... args) throws Exception {
		System.out.println("Team 3 Services is Running!");
		//this.eurekaItem.test();
	}
}