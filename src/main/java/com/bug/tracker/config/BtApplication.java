package com.bug.tracker.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackages = {"com.bug.tracker"})
@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableScheduling
@EntityScan(basePackages = "com.bug.tracker")
public class BtApplication {

	public static void main(String[] args) {
		SpringApplication.run(BtApplication.class, args);
		System.out.println("Application Started");
	}

}
