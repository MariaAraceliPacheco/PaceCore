package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.MediaType;
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

import com.example.demo.dto.entrenos.EntrenoCreateDTO;
import com.example.demo.dto.entrenos.EntrenoUpdateDTO;
import com.example.demo.dto.entrenos.EntrenosUsuarioDTO;
import com.example.demo.entities.Entreno;
import com.example.demo.services.EntrenoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/entrenos")
@Tag(name = "Entrenamientos", description = "Gestión de sesiones de entrenamiento")
public class EntrenoController {

	private final EntrenoService service;

	public EntrenoController(EntrenoService service) {
		this.service = service;
	}

	@Operation(summary = "Crear entrenamiento", description = "Registra una nueva sesión de entrenamiento completa con sus intervalos")
	@ApiResponse(responseCode = "200", description = "Entrenamiento creado")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Entreno crear(@RequestBody EntrenoCreateDTO e) {
		return service.crearEntrenoCompleto(e);
	}

	@Operation(summary = "Modificar entrenamiento", description = "Actualiza los datos de un entrenamiento existente")
	@ApiResponse(responseCode = "200", description = "Entrenamiento actualizado")
	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> modificarEntreno(@RequestBody EntrenoUpdateDTO e, @PathVariable int id) {
		EntrenoUpdateDTO response = service.modificarEntreno(e, id);
		return ResponseEntity.ok(response);
	}

	// este metodo no devolverá la zona alcanzada porque no se va a usar en el front
	@Operation(summary = "Listar entrenamientos", description = "Obtiene todos los entrenamientos registrados en el sistema")
	@ApiResponse(responseCode = "200", description = "Lista de entrenamientos")
	@GetMapping
	public List<Entreno> obtenerEntrenos() {
		return service.obtenerTodosLosEntrenos();
	}

	@Operation(summary = "Obtener entrenamiento", description = "Obtiene los detalles de un entrenamiento por su ID")
	@ApiResponse(responseCode = "200", description = "Entrenamiento encontrado")
	@GetMapping("/{id}")
	public ResponseEntity<EntrenosUsuarioDTO> obtenerEntrenoPorId(@PathVariable int id) {
		EntrenosUsuarioDTO entreno = service.obtenerEntrenoPorId(id);
		return ResponseEntity.ok(entreno);

	}

	@Operation(summary = "Eliminar entrenamiento", description = "Borra un entrenamiento del sistema")
	@ApiResponse(responseCode = "200", description = "Entrenamiento eliminado")
	@DeleteMapping("/{id}")
	public void eliminarEntreno(@PathVariable int id) {
		service.eliminarEntreno(id);
	}

	@Operation(summary = "Listar entrenamientos por usuario", description = "Obtiene los entrenamientos de un usuario específico, con filtro opcional por actividad")
	@ApiResponse(responseCode = "200", description = "Lista de entrenamientos del usuario")
	@GetMapping("/entrenosPorUsuario/{id}")
	public ResponseEntity<List<EntrenosUsuarioDTO>> obtenerEntrenosConIntervalosPorIdUsuario(@PathVariable int id,
			@RequestParam(required = false) Integer tipoActividadId) {
		// tipoActividadId es un Integer para poder permitirle ser null
		List<EntrenosUsuarioDTO> result = service.obtenerEntrenosConIntervalosPorIdUsuario(id, tipoActividadId);
		return ResponseEntity.ok(result);

	}

	@Operation(summary = "Recalcular zonas (Admin)", description = "Fuerza el recalculo de zonas para todos los entrenos existentes")
	@ApiResponse(responseCode = "200", description = "Recálculo finalizado")
	@GetMapping("/admin/recalcularZonas")
	public void recalcularZonas() {
		service.recalcularZonasExistentes();
	}

}
