package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Tipoactividad;

@Repository
public interface TipoActividadRepository extends JpaRepository<Tipoactividad, Integer> {

	Tipoactividad findByNombre(String nombre);

	@Query("SELECT DISTINCT t from Entreno e JOIN Tipoactividad t ON (e.tipoactividad.id = t.id) WHERE e.usuario.id = :idUsuario")
	List<Tipoactividad> obtenerTipoActividadesDelDelUsuario(int idUsuario);

}
