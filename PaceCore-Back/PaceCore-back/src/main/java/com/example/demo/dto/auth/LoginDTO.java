package com.example.demo.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto para las credenciales de inicio de sesión")
public class LoginDTO {

	@NotBlank(message = "El email no puede estar vacio")
	@Email(message = "El campo email debe llevar formato de email usuario@example.com")
	@Schema(description = "Correo electrónico del usuario", example = "usuario@example.com")
	private String email;
	
	@Schema(description = "Contraseña del usuario", example = "password123")
	@Size(min = 6, max = 64, message = "La contraseña debe tener entre 6 y 64 caracteres")
	private String password;

	public LoginDTO(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public LoginDTO() {
		super();
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

	public void setPassword(String pass) {
		this.password = pass;
	}

}
