package com.myatos.net.issues;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IssuesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IssuesServiceApplication.class, args);
	}

}
