package com.sibdever.algo_data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.Collections;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class AlgoDataApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AlgoDataApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "5000"));
		app.run(args);
	}
}