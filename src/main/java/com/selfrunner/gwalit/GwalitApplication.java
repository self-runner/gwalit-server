package com.selfrunner.gwalit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class GwalitApplication {

	public static void main(String[] args) {

		SpringApplication.run(GwalitApplication.class, args);
		System.out.println("Server is running");
	}

}
