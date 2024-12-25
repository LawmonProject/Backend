package com.lawmon.lawmon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.lawmon.lawmon.repository")
public class LawmonApplication {

	public static void main(String[] args) {
		SpringApplication.run(LawmonApplication.class, args);
	}

}
