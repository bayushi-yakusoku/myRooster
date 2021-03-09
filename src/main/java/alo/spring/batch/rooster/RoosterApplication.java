package alo.spring.batch.rooster;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class RoosterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoosterApplication.class, args);
	}

}
