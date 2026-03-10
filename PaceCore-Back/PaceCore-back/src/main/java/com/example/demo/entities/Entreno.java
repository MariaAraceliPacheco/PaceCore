package com.example.demo.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the entreno database table.
 * 
 */
@Entity
@NamedQuery(name = "Entreno.findAll", query = "SELECT e FROM Entreno e")
public class Entreno implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(precision = 7, scale = 2)
	private BigDecimal distancia;

	private Timestamp fecha;

	@Column(name = "tiempo_total")
	private Long tiempoTotal;

	// bi-directional many-to-one association to Usuario
	@ManyToOne
	private Usuario usuario;

	// bi-directional many-to-one association to Tipoactividad
	@ManyToOne
	@JoinColumn(name = "tipo_actividad_id")
	private Tipoactividad tipoactividad;

	private String descripcion;

	private String titulo;

	private BigDecimal desnivel;

	@Column(name = "zona_alcanzada")
	private Integer zonaAlcanzada;

	// bi-directional many-to-one association to Intervalo
	// En caso de que cuando se elimine un entreno, si el entreno tiene intervalos
	// asociados, se eliminan todos sus intervalos asociados
	@OneToMany(mappedBy = "entreno", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JsonIgnore
	private List<Intervalo> intervalos;

	@Column(name = "fc_media")
	private Integer fcMedia;

	@Column(name = "fc_maxima")
	private Integer fcMaxima;

	public Entreno() {
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

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getDistancia() {
		return this.distancia;
	}

	public void setDistancia(BigDecimal distancia) {
		this.distancia = distancia;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public Long getTiempoTotal() {
		return this.tiempoTotal;
	}

	public void setTiempoTotal(Long tiempoTotal) {
		this.tiempoTotal = tiempoTotal;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Tipoactividad getTipoactividad() {
		return this.tipoactividad;
	}

	public void setTipoactividad(Tipoactividad tipoactividad) {
		this.tipoactividad = tipoactividad;
	}

	public List<Intervalo> getIntervalos() {
		return this.intervalos;
	}

	public void setIntervalos(List<Intervalo> intervalos) {
		this.intervalos = intervalos;
	}

	public Intervalo addIntervalo(Intervalo intervalo) {
		getIntervalos().add(intervalo);
		intervalo.setEntreno(this);

		return intervalo;
	}

	public Intervalo removeIntervalo(Intervalo intervalo) {
		getIntervalos().remove(intervalo);
		intervalo.setEntreno(null);

		return intervalo;
	}

}