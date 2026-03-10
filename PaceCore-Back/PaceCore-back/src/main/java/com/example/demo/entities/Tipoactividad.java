package com.example.demo.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the tipoactividad database table.
 * 
 */
@Entity
@NamedQuery(name="Tipoactividad.findAll", query="SELECT t FROM Tipoactividad t")
public class Tipoactividad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String nombre;

	//bi-directional many-to-one association to Entreno
	@OneToMany(mappedBy="tipoactividad")
	@JsonIgnore
	private List<Entreno> entrenos;

	//bi-directional many-to-one association to Intervalo
	@OneToMany(mappedBy="tipoactividad")
	@JsonIgnore
	private List<Intervalo> intervalos;

	public Tipoactividad() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Entreno> getEntrenos() {
		return this.entrenos;
	}

	public void setEntrenos(List<Entreno> entrenos) {
		this.entrenos = entrenos;
	}

	public Entreno addEntreno(Entreno entreno) {
		getEntrenos().add(entreno);
		entreno.setTipoactividad(this);

		return entreno;
	}

	public Entreno removeEntreno(Entreno entreno) {
		getEntrenos().remove(entreno);
		entreno.setTipoactividad(null);

		return entreno;
	}

	public List<Intervalo> getIntervalos() {
		return this.intervalos;
	}

	public void setIntervalos(List<Intervalo> intervalos) {
		this.intervalos = intervalos;
	}

	public Intervalo addIntervalo(Intervalo intervalo) {
		getIntervalos().add(intervalo);
		intervalo.setTipoactividad(this);

		return intervalo;
	}

	public Intervalo removeIntervalo(Intervalo intervalo) {
		getIntervalos().remove(intervalo);
		intervalo.setTipoactividad(null);

		return intervalo;
	}

}