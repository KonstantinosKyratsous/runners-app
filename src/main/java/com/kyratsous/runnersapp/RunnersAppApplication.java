package com.kyratsous.runnersapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RunnersAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RunnersAppApplication.class, args);
	}

}
