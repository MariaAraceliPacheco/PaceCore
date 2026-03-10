
package com.example.demo.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the intervalo database table.
 * 
 */
@Entity
@NamedQuery(name = "Intervalo.findAll", query = "SELECT i FROM Intervalo i")
public class Intervalo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private Long duracion;

	@Column(precision = 7, scale = 2)
	private BigDecimal distancia;

	// como en la tabla intervalo se tiene un id referenciando al entreno,
	// habrá que usar la anotacion @Joincolumn especificando el nombre de la columna
	// Para que hibernate sepa que este atributo Entreno se refiere a esa columna de
	// la tabla
	// bi-directional many-to-one association to Entreno
	@ManyToOne
	@JoinColumn(name = "entreno_id")
	private Entreno entreno;

	// bi-directional many-to-one association to Tipoactividad
	@JoinColumn(name = "tipo_actividad_id")
	@JsonIgnore
	@ManyToOne
	private Tipoactividad tipoactividad;

	private BigDecimal desnivel;

	@Column(name = "fc_media")
	private Integer fcMedia;

	@Column(name = "fc_maxima")
	private Integer fcMaxima;

	@Column(name = "zona_alcanzada")
	private Integer zonaAlcanzada;

	public Intervalo() {
	}

	public Integer getZonaAlcanzada() {
		return zonaAlcanzada;
	}

	public void setZonaAlcanzada(Integer zonaAlcanzada) {
		this.zonaAlcanzada = zonaAlcanzada;
	}

	public Integer getFcMedia() {
		return fcMedia;
	}

	public void setFcMedia(Integer fcMedia) {
		this.fcMedia = fcMedia;
	}

	public Integer getFcMaxima() {
		return fcMaxima;
	}

	public void setFcMaxima(Integer fcMaxima) {
		this.fcMaxima = fcMaxima;
	}

	public BigDecimal getDesnivel() {
		return desnivel;
	}

	public void setDesnivel(BigDecimal desnivel) {
		this.desnivel = desnivel;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Long getDuracion() {
		return this.duracion;
	}

	public void setDuracion(Long duracion) {
		this.duracion = duracion;
	}

	public BigDecimal getDistancia() {
		return distancia;
	}

	public void setDistancia(BigDecimal distancia) {
		this.distancia = distancia;
	}

	public Entreno getEntreno() {
		return this.entreno;
	}

	public void setEntreno(Entreno entreno) {
		this.entreno = entreno;
	}

	public Tipoactividad getTipoactividad() {
		return this.tipoactividad;
	}

	public void setTipoactividad(Tipoactividad tipoactividad) {
		this.tipoactividad = tipoactividad;
	}

}