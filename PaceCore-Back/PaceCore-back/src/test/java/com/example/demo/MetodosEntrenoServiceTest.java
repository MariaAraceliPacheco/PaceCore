package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.demo.dto.intervalos.IntervaloInsertDTO;
import com.example.demo.tests.MetodosEntrenoService;

public class MetodosEntrenoServiceTest {

	public MetodosEntrenoService metodos = new MetodosEntrenoService();

	@Test
	void devuelveNullCuandoTodosFcIntervalosSonNull() {
		IntervaloInsertDTO i1 = new IntervaloInsertDTO();

		i1.setFcMedia(null);
		i1.setDuracion(LocalTime.of(0, 10, 0));

		IntervaloInsertDTO i2 = new IntervaloInsertDTO();

		i2.setFcMedia(null);
		i2.setDuracion(LocalTime.of(0, 5, 0));

		List<IntervaloInsertDTO> intervalos = List.of(i1, i2);

		// ejecutar el metodo
		Integer resultado = metodos.calcularFcMediaPonderada(intervalos);

		// verificar
		assertNull(resultado);
	}

	@Test
	void devuelveFcCuandoSoloHayUnIntervaloValido() {
		IntervaloInsertDTO i1 = new IntervaloInsertDTO();

		i1.setFcMedia(120);
		i1.setDuracion(LocalTime.of(0, 10, 0));

		IntervaloInsertDTO i2 = new IntervaloInsertDTO();

		i2.setFcMedia(null);
		i2.setDuracion(LocalTime.of(0, 5, 0));

		List<IntervaloInsertDTO> intervalos = List.of(i1, i2);

		// ejecutar el metodo
		Integer resultado = metodos.calcularFcMediaPonderada(intervalos);

		// verificar
		assertEquals(120, resultado);
	}

	@Test
	void devuelveFcCuandoVariosIntervalosValidos() {
		IntervaloInsertDTO i1 = new IntervaloInsertDTO();

		i1.setFcMedia(120);
		i1.setDuracion(LocalTime.of(0, 10, 0)); // 600 segundos

		IntervaloInsertDTO i2 = new IntervaloInsertDTO();

		i2.setFcMedia(80);
		i2.setDuracion(LocalTime.of(0, 5, 0)); // 300 segundos

		List<IntervaloInsertDTO> intervalos = List.of(i1, i2);

		// ejecutar el metodo
		Integer resultado = metodos.calcularFcMediaPonderada(intervalos);

		// verificar
		assertEquals(107, resultado);
	}

}
