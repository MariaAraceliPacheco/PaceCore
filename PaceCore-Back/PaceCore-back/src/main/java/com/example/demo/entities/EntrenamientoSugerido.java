package com.example.demo.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * The persistent class for the entrenamiento_sugerido database table.
 * 
 */
@Entity
@Table(name = "entrenamiento_sugerido")
@NamedQuery(name = "EntrenamientoSugerido.findAll", query = "SELECT e FROM EntrenamientoSugerido e")
public class EntrenamientoSugerido implements Serializable {
	public enum EstadoSugerencia {
		SUGERIDO, ACEPTADO, RECHAZADO, COMPLETADO
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 500)
	private String descripcion;

	@Enumerated(EnumType.STRING)
	private EstadoSugerencia estado;

	@Column(columnDefinition = "TEXT")
	private String estructuraJson;

	private LocalDateTime fechaGeneracion;

	private String titulo;

	@JoinColumn(name = "usuario_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;

	public EntrenamientoSugerido() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EstadoSugerencia getEstado() {
		return this.estado;
	}

	public void setEstado(EstadoSugerencia estado) {
		this.estado = estado;
	}

	public String getEstructuraJson() {
		return this.estructuraJson;
	}

	public void setEstructuraJson(String estructuraJson) {
		this.estructuraJson = estructuraJson;
	}

	public LocalDateTime getFechaGeneracion() {
		return this.fechaGeneracion;
	}

	public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}