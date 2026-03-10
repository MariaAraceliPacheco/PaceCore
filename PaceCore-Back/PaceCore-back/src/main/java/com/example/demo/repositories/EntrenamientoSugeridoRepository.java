package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.EntrenamientoSugerido;
import com.example.demo.entities.EntrenamientoSugerido.EstadoSugerencia;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntrenamientoSugeridoRepository extends JpaRepository<EntrenamientoSugerido, Integer> {

	List<EntrenamientoSugerido> findByUsuarioAndEstadoAndFechaGeneracionAfter(int usuarioId, EstadoSugerencia estado,
			LocalDateTime fecha);

	// si devuelve algo significa que ya se ha generado el entrenamiento sugerido de
	// hoy
	@Query("SELECT s from EntrenamientoSugerido s WHERE s.usuario.id = :idUsuario AND s.fechaGeneracion >= :inicioDia AND s.fechaGeneracion <= :finDia")
	Optional<EntrenamientoSugerido> findSugerenciaDeHoy(@Param("idUsuario") int idUsuario,
			@Param("inicioDia") LocalDateTime inicioDia, @Param("finDia") LocalDateTime finDia);

}
