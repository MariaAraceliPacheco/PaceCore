package com.example.demo.mappers.usuario;

import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.demo.dto.usuarios.UsuarioInsertDTO;
import com.example.demo.dto.usuarios.UsuarioUpdateDTO;
import com.example.demo.entities.Usuario;

//esta interfaz sirve para centralizar reglas comunes
@MapperConfig
public interface UsuarioMapperConfig {

	// id se ignora porque lo genera la base de datos
	@Mapping(target = "id", ignore = true)

	// rol se fija a "USUARIO" para que se ponga automaticamente
	@Mapping(target = "rol", constant = "USUARIO")

	// fechaCreacion se ignora porque lo pone automaticamente Hibernate
	@Mapping(target = "fechaCreacion", ignore = true)

	// estas listas se ignoran porque no vienen del DTO
	@Mapping(target = "entrenos", ignore = true)
	@Mapping(target = "zonas", ignore = true)
	@Mapping(target = "entrenamientoSugerido", ignore = true)
	Usuario baseFromInsert(UsuarioInsertDTO dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "rol", ignore = true)
	@Mapping(target = "fechaCreacion", ignore = true)
	@Mapping(target = "entrenos", ignore = true)
	@Mapping(target = "zonas", ignore = true)
	// @Mapping(target = "entrenamientoSugerido", ignore = true)
	@Mapping(target = "password", ignore = true)
	void baseFromUpdate(UsuarioUpdateDTO dto, @MappingTarget Usuario entity);

}
