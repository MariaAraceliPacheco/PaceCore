package com.example.demo.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String email;

	@Column(name = "fecha_creacion")
	private Timestamp fechaCreacion;

	private String nombre;

	private String password;

	private String rol;

	private BigDecimal peso;

	private BigDecimal altura;

	private String descripcion;

	private Integer edad;

	// bi-directional many-to-one association to Entreno
	@OneToMany(mappedBy = "usuario")
	@JsonIgnore // Spring no devolverá este atributo cuando se haga un GET del usuario
	private List<Entreno> entrenos;

	// un usuario puede tener varias zonas de entrenamiento (5)
	// con mappedBy es como si se le estuviera diciendo a JPA que la configuracion de
	// como se unen estas dos tablas (el nombre de columna fk) está en el atributo
	// llamado "usuario" de la entidad ZonasUsuario
	@OneToMany(mappedBy = "usuario")
	@JsonIgnore
	private List<ZonasUsuario> zonas;

	public Usuario() {
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public BigDecimal getAltura() {
		return altura;
	}

	public void setAltura(BigDecimal altura) {
		this.altura = altura;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return this.rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public List<Entreno> getEntrenos() {
		return this.entrenos;
	}

	public void setEntrenos(List<Entreno> entrenos) {
		this.entrenos = entrenos;
	}

	public Entreno addEntreno(Entreno entreno) {
		getEntrenos().add(entreno);
		entreno.setUsuario(this);

		return entreno;
	}

	public Entreno removeEntreno(Entreno entreno) {
		getEntrenos().remove(entreno);
		entreno.setUsuario(null);

		return entreno;
	}

	public List<ZonasUsuario> getZonas() {
		return zonas;
	}

	public void setZonas(List<ZonasUsuario> zonas) {
		this.zonas = zonas;
	}
	
}