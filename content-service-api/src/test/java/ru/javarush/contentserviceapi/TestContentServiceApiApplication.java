package ru.javarush.contentserviceapi;

import org.springframework.boot.SpringApplication;

public class TestContentServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(ContentServiceApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
