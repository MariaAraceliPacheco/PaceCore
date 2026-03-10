package com.example.demo.dto.usuarios;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos necesarios para registrar un nuevo usuario")
public class UsuarioInsertDTO {

	@Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
	private String nombre;
	@Schema(description = "Correo electrónico único", example = "juan.perez@example.com")
	private String email;
	@Schema(description = "Contraseña de acceso", example = "password123")
	private String password;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Timestamp fecha_creacion;

	@Schema(description = "Breve descripción o biografía", example = "Entusiasta del running")
	private String descripcion;
	@Schema(description = "Peso en kilogramos", example = "75.5")
	private BigDecimal peso;
	@Schema(description = "Altura en metros", example = "1.80")
	private BigDecimal altura;
	@Schema(description = "Edad del usuario", example = "30")
	private Integer edad;

	public UsuarioInsertDTO(String nombre, String email, String password, Timestamp fecha_creacion, String descripcion,
			Integer edad, BigDecimal peso, BigDecimal altura) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		this.fecha_creacion = fecha_creacion;
		this.descripcion = descripcion;
		this.peso = peso;
		this.altura = altura;
		this.edad = edad;
	}

	public UsuarioInsertDTO() {
		super();
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(Timestamp fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

}
