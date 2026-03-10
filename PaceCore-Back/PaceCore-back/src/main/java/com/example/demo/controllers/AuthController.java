package com.example.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.auth.LoginDTO;
import com.example.demo.dto.auth.LoginResponse;
import com.example.demo.dto.usuarios.UsuarioInsertDTO;
import com.example.demo.dto.usuarios.UsuarioResponseDTO;
import com.example.demo.entities.Usuario;
import com.example.demo.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para el inicio de sesión y registro de usuarios")
public class AuthController {

	private final AuthService servicio;

	public AuthController(AuthService servicio) {
		super();
		this.servicio = servicio;
	}

	@Operation(summary = "Iniciar sesión", description = "Autentica a un usuario y devuelve un token JWT junto con sus datos")
	@ApiResponse(responseCode = "200", description = "Login exitoso")
	@ApiResponse(responseCode = "401", description = "Credenciales inválidas")
	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
		LoginResponse u = servicio.login(dto);
		return ResponseEntity.ok(u);
	}

	@Operation(summary = "Registrar usuario", description = "Crea una nueva cuenta de usuario en el sistema")
	@ApiResponse(responseCode = "200", description = "Usuario registrado correctamente")
	@ApiResponse(responseCode = "500", description = "Error interno al procesar el registro")
	@PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> register(@RequestBody UsuarioInsertDTO dto) {
		try {
			Usuario u = servicio.register(dto);
			UsuarioResponseDTO e = new UsuarioResponseDTO();
			e.setId(u.getId());
			e.setNombre(u.getNombre());
			e.setEmail(u.getEmail());
			System.out.println("La contraseña del dto en el insert es: " + dto.getPassword());
			e.setAltura(u.getAltura());
			e.setDescripcion(u.getDescripcion());
			e.setRol(u.getRol());
			e.setPeso(u.getPeso());

			return ResponseEntity.ok(u);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: " + e.getMessage());
		}
	}

	@Operation(summary = "Obtener perfil actual", description = "Obtiene los detalles del usuario basándose en su token JWT")
	@ApiResponse(responseCode = "200", description = "Perfil recuperado correctamente")
	@GetMapping(path = "/me")
	public ResponseEntity<?> obtenerUsuarioLogueadoPorSuToken(@RequestParam String token) {
		UsuarioResponseDTO u = servicio.obtenerUsuarioPorJwt(token);
		return ResponseEntity.ok(u);
	}
}
