package com.climbing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class ClimbingBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClimbingBackendApplication.class, args);
	}
}
