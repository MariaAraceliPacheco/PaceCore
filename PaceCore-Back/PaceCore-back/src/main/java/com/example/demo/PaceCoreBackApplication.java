package com.example.demo;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.repositories.UsuarioRepository;

@SpringBootApplication
public class PaceCoreBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaceCoreBackApplication.class, args);
	}
	@Bean
	public CommandLineRunner testDataSource(DataSource ds) {
		return args -> {
			System.out.println("Conectando a la base de datos...");
			System.out.println("URL: " + ds.getConnection().getMetaData().getURL());
			System.out.println("BD: " + ds.getConnection().getCatalog());
		};
	}
	
	@Bean
	CommandLineRunner test(UsuarioRepository repo) {
	    return args -> {
	        System.out.println("Usuarios en la BD:");
	        repo.findAll().forEach(System.out::println);
	    };
	}

}
