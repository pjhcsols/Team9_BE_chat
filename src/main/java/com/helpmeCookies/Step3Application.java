package com.helpmeCookies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class Step3Application {

	public static void main(String[] args) {
		SpringApplication.run(Step3Application.class, args);
	}
}
