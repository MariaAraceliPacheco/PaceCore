package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.dto.usuarios.UsuarioInsertDTO;
import com.example.demo.dto.usuarios.UsuarioResponseDTO;
import com.example.demo.entities.Usuario;
import com.example.demo.mappers.UsuarioMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class AppConfig {
	  @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	  
	  @Bean
	  public ObjectMapper objectMapper() {
	      return new ObjectMapper()
	          .registerModule(new JavaTimeModule())
	          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	  }

	  @Bean
	  public UsuarioMapper usuarioMapper() {
		  return new UsuarioMapper() {
			
			@Override
			public Usuario toEntity(UsuarioInsertDTO dto) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public UsuarioResponseDTO toDto(Usuario usuario) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	  }
}
