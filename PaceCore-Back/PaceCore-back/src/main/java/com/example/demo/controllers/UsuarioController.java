package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.usuarios.UsuarioEstadisticasDTO;
import com.example.demo.dto.usuarios.UsuarioEstadisticasSemanalesDTO;
import com.example.demo.dto.usuarios.UsuarioInsertDTO;
import com.example.demo.dto.usuarios.UsuarioUpdateDTO;
import com.example.demo.entities.Usuario;
import com.example.demo.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Gestión de Usuarios", description = "Operaciones CRUD y estadísticas de usuarios")
public class UsuarioController {

	private final UsuarioService service;

	public UsuarioController(UsuarioService service) {
		this.service = service;
	}

	@Operation(summary = "Crear usuario", description = "Registra un nuevo usuario directamente (sin flujo de auth)")
	@ApiResponse(responseCode = "200", description = "Usuario creado con éxito")
	@PostMapping
	public Usuario crear(@RequestBody UsuarioInsertDTO u) {
		return service.crear(u);
	}

	@Operation(summary = "Listar usuarios", description = "Obtiene una lista con todos los usuarios registrados")
	@ApiResponse(responseCode = "200", description = "Lista recuperada")
	@GetMapping
	public List<Usuario> obtenerUsuarios() {
		return service.obtenerTodosLosUsuarios();
	}

	@Operation(summary = "Obtener usuario", description = "Busca un usuario por su ID único")
	@ApiResponse(responseCode = "200", description = "Usuario encontrado")
	@ApiResponse(responseCode = "404", description = "Usuario no encontrado")
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable int id) {
		return service.getUsuarioPorId(id);
	}

	@Operation(summary = "Eliminar usuario", description = "Borra un usuario del sistema")
	@ApiResponse(responseCode = "200", description = "Usuario eliminado")
	@DeleteMapping("/{id}")
	public void eliminarUsuario(@PathVariable int id) {
		service.eliminarUsuario(id);
	}

	@Operation(summary = "Modificar usuario", description = "Actualiza los datos de un usuario existente")
	@ApiResponse(responseCode = "200", description = "Usuario actualizado")
	@ApiResponse(responseCode = "404", description = "Usuario no encontrado")
	@PutMapping("/{id}")
	public ResponseEntity<Usuario> modificarUsuario(@RequestBody UsuarioUpdateDTO u, @PathVariable int id) {
		try {
			Usuario user = service.modificarUsuario(u, id);
			return ResponseEntity.ok(user);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}

	}

	@Operation(summary = "Estadísticas generales", description = "Obtiene el resumen de estadísticas de un usuario")
	@ApiResponse(responseCode = "200", description = "Estadísticas recuperadas")
	@GetMapping("/estadisticas/{id}")
	public ResponseEntity<UsuarioEstadisticasDTO> obtenerEstadisticasUsuario(@PathVariable int id) {
		UsuarioEstadisticasDTO stats = new UsuarioEstadisticasDTO();
		stats = service.obtenerEstadisticasUsuario(id);
		return ResponseEntity.ok(stats);
	}

	// este devuelve las estadisticas semanales sin diferenciar del tipo de
	// actividad
	/*
	 * @GetMapping("/estadisticas/semanales/{id}") public
	 * ResponseEntity<List<UsuarioEstadisticasSemanalesDTO>>
	 * obtenerEstadisticasSemanalesUsuario(
	 * 
	 * @PathVariable int id) { List<UsuarioEstadisticasSemanalesDTO> estadisticas =
	 * service.obtenerEstadisticasSemanalesUsuario(id); return
	 * ResponseEntity.ok(estadisticas); }
	 */

	@Operation(summary = "Estadísticas semanales", description = "Obtiene estadísticas semanales filtrables por tipo de actividad")
	@ApiResponse(responseCode = "200", description = "Lista de estadísticas semanales")
	@GetMapping("/estadisticas/semanales/{id}")
	public ResponseEntity<List<UsuarioEstadisticasSemanalesDTO>> obtenerEstadisticasSemanalesUsuarioFiltradasPorTipoActividad(
			@PathVariable int id, @RequestParam(required = false) Integer idTipoActividad) {
		List<UsuarioEstadisticasSemanalesDTO> estadisticas = service
				.obtenerEstadisticasSemanalesUsuarioFiltradasPorTipoActividad(id, idTipoActividad);
		return ResponseEntity.ok(estadisticas);
	}

}
