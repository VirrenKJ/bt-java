package com.bug.tracker.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackages = {"com.bug.tracker"})
@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
@EnableScheduling
@EntityScan(basePackages = "com.bug.tracker")
public class BtApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(BtApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println("\n" + "****************************************Application Started****************************************" + "\n");
  }
}
