package com.project.gangrg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GangrgApplication {

	public static void main(String[] args) {
		SpringApplication.run(GangrgApplication.class, args);
	}

}
