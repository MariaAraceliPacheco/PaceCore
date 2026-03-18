package com.example.demo.mappers.zonas;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.demo.dto.zonas.ZonasUpdateDTO;
import com.example.demo.entities.ZonasUsuario;


@Mapper(componentModel = "spring")
public interface ZonasMapper {

	@Mapping(source = "fc_minima", target = "fcMinima")
	@Mapping(source = "fc_maxima", target = "fcMaxima")
	@Mapping(source = "nombre_zona", target = "nombreZona")
	@Mapping(source = "numero_zona", target = "numeroZona")
	@Mapping(target = "usuario", ignore = true)
	void updateFromZonasUpdateDTO(ZonasUpdateDTO dto, @MappingTarget ZonasUsuario entity);
	
}
