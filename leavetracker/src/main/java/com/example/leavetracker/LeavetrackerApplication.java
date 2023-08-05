package com.example.leavetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.example.leavetracker","com.example.leavetracker.repos"})
public class LeavetrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeavetrackerApplication.class, args);
	}

}
