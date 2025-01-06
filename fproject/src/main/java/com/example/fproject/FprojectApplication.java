package com.example.fproject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FprojectApplication.class, args);

	}
	@Bean
	public CommandLineRunner commandLineRunner(){
		return runner->{
			System.out.println("DONE");
		};
	}
}
