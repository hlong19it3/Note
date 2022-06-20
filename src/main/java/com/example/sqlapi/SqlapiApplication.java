package com.example.sqlapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SqlapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqlapiApplication.class, args);
	}

}
