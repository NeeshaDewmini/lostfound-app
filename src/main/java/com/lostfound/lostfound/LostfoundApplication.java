package com.lostfound.lostfound;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class LostfoundApplication {

	public static void main(String[] args) {
		SpringApplication.run(LostfoundApplication.class, args);
	}

}
