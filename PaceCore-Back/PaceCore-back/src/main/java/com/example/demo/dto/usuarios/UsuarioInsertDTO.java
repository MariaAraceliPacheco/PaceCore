package com.example.demo.dto.usuarios;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para registrar un nuevo usuario")
public class UsuarioInsertDTO {

	@NotBlank(message = "El nombre no puede estar vacio")
	@Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
	private String nombre;

	@Email(message = "El email tiene que tener formato de email juan.perez@example.com")
	@NotBlank(message = "El email no puede estar vacio")
	@Schema(description = "Correo electrónico único", example = "juan.perez@example.com")
	private String email;

	@NotBlank(message = "El password no puede estar vacio")
	@Size(min = 6, max = 64)
	@Schema(description = "Contraseña de acceso", example = "password123")
	private String password;

	@Size(max = 255, message = "La descripcion no puede tener mas de 255 caracteres")
	@Schema(description = "Breve descripción o biografía", example = "Entusiasta del running")
	private String descripcion;

	@DecimalMin("0")
	@DecimalMax("400")
	@Schema(description = "Peso en kilogramos", example = "75.5")
	private BigDecimal peso;

	// como este es de tipo BigDecimal, se usa la notacion @DecimalMin y @DecimalMax
	@DecimalMin("0")
	@DecimalMax("350")
	@Schema(description = "Altura en centimetros", example = "180")
	private BigDecimal altura;
	@Schema(description = "Edad del usuario", example = "30")

	// este es un tipo numero Entero, por lo que se usa la anotacion @Min y @Max
	@Min(0)
	@Max(150)
	@NotNull(message = "La edad no puede estar vacia")
	private Integer edad;

	public UsuarioInsertDTO(String nombre, String email, String password, String descripcion, Integer edad,
			BigDecimal peso, BigDecimal altura) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.password = password;
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

}
