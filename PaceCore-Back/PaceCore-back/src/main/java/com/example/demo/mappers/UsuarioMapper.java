package com.example.demo.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.dto.usuarios.UsuarioInsertDTO;
import com.example.demo.dto.usuarios.UsuarioResponseDTO;
import com.example.demo.entities.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

	//id se ignora porque lo genera la base de datos
	@Mapping(target = "id", ignore = true)
	
	//rol se fija a "USUARIO" para que se ponga automaticamente
	@Mapping(target = "rol", constant = "USUARIO")
	
	//fechaCreacion se ignora porque lo pone automaticamente Hibernate
	@Mapping(target = "fechaCreacion", ignore = true)
	
	//estas listas se ignoran porque no vienen del DTO
	@Mapping(target = "entrenos", ignore = true)
	@Mapping(target = "zonas", ignore = true)
	@Mapping(target = "entrenamientoSugerido", ignore = true)
	Usuario toEntity(UsuarioInsertDTO dto);
	
	UsuarioResponseDTO toDto(Usuario usuario);
}
