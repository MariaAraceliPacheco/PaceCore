package com.example.demo.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.tipoActividad.ActividadConteoDTO;
import com.example.demo.entities.Entreno;

@Repository
public interface EntrenoRepository extends JpaRepository<Entreno, Integer> {

	// Se usa left join para que salgan los entrenamientos que no tengan intervalos
	@Query("SELECT DISTINCT e FROM Entreno e LEFT JOIN FETCH e.intervalos WHERE e.usuario.id = :usuarioId order by e.fecha desc")
	List<Entreno> findAllWithIntervalosByUsuario(@Param("usuarioId") Integer usuarioid);

	// JPQL no trabaja con nombres de columnas, trabaja con entidades. Por lo que a
	// la hora de seleccionar el id del tipo de actividad del entrenamiento, es como
	// si seleccionasemos la propiedad llamada "tipoactividad" de la entidad
	// "Entreno" y accedieramos a su id
	@Query("SELECT DISTINCT e FROM Entreno e LEFT JOIN FETCH e.intervalos WHERE e.usuario.id = :usuarioId and e.tipoactividad.id = :tipoActividadId order by e.fecha desc")
	List<Entreno> findAllWithIntervalosByUsuarioAndTipoActividad(@Param("usuarioId") Integer usuarioid,
			@Param("tipoActividadId") Integer tipoActividadId);

	// con "left join e.intervalos.i" se accede a cada intervalo asociado al entreno
	// COALESCE(SUM(...), 0) evita que se devuelva null si no hay entrenos o
	// intervalos
	@Query("""
			    SELECT COALESCE(SUM(e.distancia), 0) + COALESCE(SUM(i.distancia), 0)
			    FROM Entreno e
			    LEFT JOIN e.intervalos i
			    WHERE e.usuario.id = :usuarioId
			""")
	BigDecimal getKmTotalesByUsuario(@Param("usuarioId") Integer usuarioId);

	@Query("""
			    SELECT COALESCE(SUM(e.desnivel), 0) + COALESCE(SUM(i.desnivel), 0)
			    FROM Entreno e
			    LEFT JOIN e.intervalos i
			    WHERE e.usuario.id = :usuarioId
			""")
	BigDecimal getDesnivelTotalByUsuario(@Param("usuarioId") Integer usuarioId);

	// se usa el join fetch para que se traiga todos los intervalos de golpe,
	// haciendo un Eager loading manual, ya que por defecto en las relaciones
	// OneToMany, hibernate las pone como LAZY
	@Query("SELECT e FROM Entreno e JOIN FETCH e.intervalos WHERE e.usuario.id = :usuarioId AND (:tipoActividadId IS NULL OR e.tipoactividad.id = :tipoActividadId)"
			+ " AND (e.fecha BETWEEN :inicio AND :fin)")
	List<Entreno> findEntrenosParaEstadisticas(@Param("usuarioId") int usuarioId,
			@Param("tipoActividadId") Integer tipoActividadId, @Param("inicio") LocalDateTime inicio,
			@Param("fin") LocalDateTime fin);

	/*
	 * Esta consulta se usará en el TipoActividadService Pero se crea en el
	 * EntrenoRepository porque la tabla principal a la que le hace la consulta es
	 * la de Entreno A la hora de decirle el objeto que se va a crear
	 * ActividadConteoDTO, es importante especificarle la ruta entera de la clase,
	 * porque si no, Hibernate no lo coge
	 */
	@Query("SELECT new com.example.demo.dto.tipoActividad.ActividadConteoDTO(e.tipoactividad.nombre, COUNT(e)) "
			+ "FROM Entreno e  WHERE e.usuario.id = :usuarioId " // si inicio viene como null o la fecha es posterior a
																	// inicio y si fin es null o la fecha es anterior a
																	// fin...
			+ "AND (:inicio IS NULL OR e.fecha >= :inicio ) AND (:fin IS NULL OR e.fecha <= :fin) GROUP BY e.tipoactividad.nombre")
	List<ActividadConteoDTO> obtenerConteoActividades(@Param("usuarioId") int usuarioId,
			@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

	@Query("SELECT MAX(e.fecha) FROM Entreno e WHERE e.usuario.id = :idUsuario")
	Optional<LocalDateTime> findFechaUltimoEntreno(@Param("idUsuario") int idUsuario);
	
}
