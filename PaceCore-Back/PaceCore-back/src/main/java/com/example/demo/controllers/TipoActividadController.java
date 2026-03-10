package com.example.demo.controllers;

import java.time.LocalDateTime;
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

import com.example.demo.dto.tipoActividad.ActividadConteoDTO;
import com.example.demo.dto.tipoActividad.TipoActividadDTO;
import com.example.demo.dto.tipoActividad.TipoActividadResultDTO;
import com.example.demo.entities.Tipoactividad;
import com.example.demo.services.TipoActividadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/actividades")
@Tag(name = "Tipos de Actividad", description = "Gestión de los tipos de actividades físicas disponibles")
public class TipoActividadController {

	private final TipoActividadService servicio;

	public TipoActividadController(TipoActividadService servicio) {
		super();
		this.servicio = servicio;
	}

	@Operation(summary = "Listar todas las actividades", description = "Obtiene el catálogo completo de tipos de actividad")
	@ApiResponse(responseCode = "200", description = "Lista recuperada")
	@GetMapping
	public List<Tipoactividad> obtenerTodasLasActividades() {
		return servicio.obtenerTodosLosTiposActividad();
	}

	@Operation(summary = "Obtener actividad", description = "Obtiene un tipo de actividad por su ID")
	@ApiResponse(responseCode = "200", description = "Actividad encontrada")
	@GetMapping("/{id}")
	public ResponseEntity<Tipoactividad> obtenerActividadPorId(@PathVariable int id) {
		return servicio.getTipoActividadPorId(id);
	}

	@Operation(summary = "Crear actividad", description = "Registra un nuevo tipo de actividad en el sistema")
	@ApiResponse(responseCode = "200", description = "Actividad creada")
	@PostMapping
	public ResponseEntity<Tipoactividad> crearActividad(@RequestBody TipoActividadDTO dto) {
		try {
			Tipoactividad actividad = servicio.crear(dto);
			return ResponseEntity.ok(actividad);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Eliminar actividad", description = "Borra un tipo de actividad si no tiene dependencias")
	@ApiResponse(responseCode = "200", description = "Actividad eliminada")
	@ApiResponse(responseCode = "409", description = "Conflicto: la actividad tiene entrenos asociados")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminarActividad(@PathVariable int id) {
		try {
			servicio.eliminarTipoActividad(id);
			return ResponseEntity.ok("Tipo de actividad eliminada correctamente");
		} catch (IllegalStateException e) {
			return ResponseEntity.status(409).body(e.getMessage());
		}

	}

	@Operation(summary = "Modificar actividad", description = "Actualiza el nombre o icono de una actividad")
	@ApiResponse(responseCode = "200", description = "Actividad actualizada")
	@PutMapping("/{id}")
	public ResponseEntity<Tipoactividad> modificarActividad(@RequestBody TipoActividadDTO dto, @PathVariable int id) {
		try {
			Tipoactividad tipoactividadModificada = servicio.modificarTipoActividad(id, dto);
			return ResponseEntity.ok(tipoactividadModificada);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build(); // Devuelve un 404 si no existe
		}
	}

	@Operation(summary = "Recuento por tipo", description = "Obtiene cuántas veces se ha realizado cada actividad en un periodo")
	@ApiResponse(responseCode = "200", description = "Recuento generado")
	@GetMapping("/recuentoTiposActividad/{id}")
	public ResponseEntity<?> obtenerTotalEntrenosUsuario(@PathVariable int id,
			@RequestParam(required = false) LocalDateTime inicio, @RequestParam(required = false) LocalDateTime fin) {
		List<ActividadConteoDTO> response = servicio.obtenerConteoTiposActividad(id, inicio, fin);

		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Actividades registradas por usuario", description = "Obtiene los tipos de actividad únicos que un usuario ha realizado")
	@ApiResponse(responseCode = "200", description = "Lista de actividades")
	@GetMapping("/tiposActividadUsuario/{id}")
	public ResponseEntity<?> obtenerTipoActividadesDelUsuario(@PathVariable int id) {
		List<TipoActividadResultDTO> result = servicio.obtenerTiposActividadesDelUsuario(id);

		return ResponseEntity.ok(result);
	}

}
