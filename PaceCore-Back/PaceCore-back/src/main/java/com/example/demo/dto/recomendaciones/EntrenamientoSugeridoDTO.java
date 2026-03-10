package com.example.demo.dto.recomendaciones;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entities.EntrenamientoSugerido.EstadoSugerencia;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Sugerencia de entrenamiento generada por el sistema")
public class EntrenamientoSugeridoDTO {

	@Schema(description = "ID de la sugerencia", example = "10")
	private int id;
	@Schema(description = "Título de la recomendación", example = "Rodaje de resistencia")
	private String titulo;
	@Schema(description = "Descripción general", example = "Sesión enfocada a mejorar la capacidad aeróbica")
	private String descripcion;
	private LocalDateTime fechaGeneracion;
	@Schema(description = "Lista de bloques que componen el entrenamiento")
	private List<BloqueEntrenamiento> bloques;
	@Schema(description = "Estado actual de la sugerencia (PENDIENTE, COMPLETADO, etc.)")
	private EstadoSugerencia estado;

	public EntrenamientoSugeridoDTO(int id, String titulo, String descripcion, LocalDateTime fechaGeneracion,
			List<BloqueEntrenamiento> bloques, EstadoSugerencia estado) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.fechaGeneracion = fechaGeneracion;
		this.bloques = bloques;
		this.estado = estado;
	}

	public EntrenamientoSugeridoDTO() {
		super();
	}

	public EstadoSugerencia getEstado() {
		return estado;
	}

	public void setEstado(EstadoSugerencia estado) {
		this.estado = estado;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDateTime getFechaGeneracion() {
		return fechaGeneracion;
	}

	public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public List<BloqueEntrenamiento> getBloques() {
		return bloques;
	}

	public void setBloques(List<BloqueEntrenamiento> bloques) {
		this.bloques = bloques;
	}

}
