package com.example.demo.mappers.usuario;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.demo.dto.usuarios.UsuarioInsertDTO;
import com.example.demo.dto.usuarios.UsuarioResponseDTO;
import com.example.demo.dto.usuarios.UsuarioUpdateDTO;
import com.example.demo.entities.Usuario;

@Mapper(componentModel = "spring", config = UsuarioMapperConfig.class)
public interface UsuarioMapper {

	// id se ignora porque lo genera la base de datos
	@Mapping(target = "id", ignore = true)

	// rol se fija a "USUARIO" para que se ponga automaticamente
	@Mapping(target = "rol", constant = "USUARIO")

	// fechaCreacion se ignora porque lo pone automaticamente Hibernate
	@Mapping(target = "fechaCreacion", ignore = true)

	// estas listas se ignoran porque no vienen del DTO
	@Mapping(target = "entrenos", ignore = true)
	@Mapping(target = "zonas", ignore = true)
	// @Mapping(target = "entrenamientoSugerido", ignore = true)
	Usuario toEntityFromUsuarioInsertDTO(UsuarioInsertDTO dto);

	UsuarioResponseDTO toUsuarioResponseDTO(Usuario usuario);

	Usuario toEntityFromUsuarioResponseDTO(UsuarioResponseDTO dto);

	// con el @MappingTarget no se crea una entidad nueva con los valores del dto
	// solamente actualiza los campos del dto, y el resto los mantiene intactos
	@InheritConfiguration(name = "baseFromUpdate")
	void updateUsuarioFromDTO(UsuarioUpdateDTO dto, @MappingTarget Usuario entity);

	UsuarioUpdateDTO toUsuarioUpdateDTO(Usuario u);

}
