package com.example.demo.entities;

import java.io.Serializable;
import jakarta.persistence.*;

/**
 * The persistent class for the zonas_usuario database table.
 * 
 */
@Entity
@Table(name = "zonas_usuario")
@NamedQuery(name = "ZonasUsuario.findAll", query = "SELECT z FROM ZonasUsuario z")
public class ZonasUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "fc_maxima")
	private int fcMaxima;

	@Column(name = "fc_minima")
	private int fcMinima;

	@Column(name = "nombre_zona")
	private String nombreZona;

	@JoinColumn(name = "usuario_id") // aqui se indica la columna que actua como fk
	@ManyToOne // un usuario tiene muchas zonas de entrenamiento
	// pero una zona de entrenamiento solo tiene un usuario
	private Usuario usuario;

	@Column(name = "numero_zona")
	private int numeroZona;

	@Column(name = "descripcion")
	private String descripcion;

	public ZonasUsuario() {
	}

	public int getNumeroZona() {
		return numeroZona;
	}

	public void setNumeroZona(int numeroZona) {
		this.numeroZona = numeroZona;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFcMaxima() {
		return this.fcMaxima;
	}

	public void setFcMaxima(int fcMaxima) {
		this.fcMaxima = fcMaxima;
	}

	public int getFcMinima() {
		return this.fcMinima;
	}

	public void setFcMinima(int fcMinima) {
		this.fcMinima = fcMinima;
	}

	public String getNombreZona() {
		return this.nombreZona;
	}

	public void setNombreZona(String nombreZona) {
		this.nombreZona = nombreZona;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}