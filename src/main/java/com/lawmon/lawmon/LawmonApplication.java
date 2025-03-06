package com.lawmon.lawmon;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.lawmon.lawmon.repository")
public class LawmonApplication implements CommandLineRunner {

	@Value("${jwt.secret:default-secret-key}")  // ✅ 기본값 추가
	private String jwtSecret;

	public static void main(String[] args) {
		SpringApplication.run(LawmonApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("🔵 JWT Secret Key from @Value: " + jwtSecret);
		System.out.println("🔵 JWT Secret Key from System.getenv(): " + System.getenv("JWT_SECRET"));
	}
}
