package com.coresaken.memApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MemAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemAppApplication.class, args);
	}

}
