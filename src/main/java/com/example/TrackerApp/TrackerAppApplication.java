package com.example.TrackerApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.TrackerApp.repository")
public class TrackerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackerAppApplication.class, args);
	}

}
