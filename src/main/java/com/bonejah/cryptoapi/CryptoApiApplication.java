package com.bonejah.cryptoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@EnableJpaRepositories
@EnableCaching
@SpringBootApplication
public class CryptoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoApiApplication.class, args);
	}

}
