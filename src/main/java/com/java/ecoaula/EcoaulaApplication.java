package com.java.ecoaula;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcoaulaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcoaulaApplication.class, args);
	}

}
