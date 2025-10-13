package com.davicesar.batismo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BatismoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatismoApplication.class, args);
	}

}
