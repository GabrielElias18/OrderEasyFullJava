package com.ejemplo.miproyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class OrderEasyApplication {


	public static void main(String[] args) {
		SpringApplication.run(OrderEasyApplication.class, args);
	}

}
