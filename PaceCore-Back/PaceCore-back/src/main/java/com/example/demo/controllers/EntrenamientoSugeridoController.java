package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.recomendaciones.EntrenamientoSugeridoDTO;
import com.example.demo.entities.EntrenamientoSugerido.EstadoSugerencia;
import com.example.demo.services.RecomendadorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/recomendaciones")
@Tag(name = "Recomendaciones", description = "Generación de sugerencias inteligentes para entrenamientos")
public class EntrenamientoSugeridoController {

	private final RecomendadorService service;

	public EntrenamientoSugeridoController(RecomendadorService service) {
		super();
		this.service = service;
	}

	@Operation(summary = "Generar recomendación", description = "Analiza el historial del usuario y genera una sugerencia para el próximo entrenamiento")
	@ApiResponse(responseCode = "200", description = "Sugerencia generada")
	@GetMapping("/{id}")
	public ResponseEntity<?> generarRecomendacionEntrenamiento(@PathVariable int id) {
		EntrenamientoSugeridoDTO response = service.generarSugerencia(id);

		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Actualizar estado de sugerencia", description = "Marca una sugerencia como acepatada, rechazada o pendiente")
	@ApiResponse(responseCode = "200", description = "Estado actualizado")
	@PutMapping("/{id}/estado/{nuevoEstado}")
	public void actualizarEstado(@PathVariable int id, @PathVariable EstadoSugerencia nuevoEstado) {
		service.actualizarEstado(id, nuevoEstado);
	}
}
