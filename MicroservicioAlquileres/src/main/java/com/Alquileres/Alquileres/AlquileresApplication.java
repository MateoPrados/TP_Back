package com.Alquileres.Alquileres;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.Alquileres.Alquileres")
public class AlquileresApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlquileresApplication.class, args);
	}

}
