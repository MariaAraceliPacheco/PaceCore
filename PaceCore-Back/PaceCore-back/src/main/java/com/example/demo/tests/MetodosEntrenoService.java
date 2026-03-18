package com.example.demo.tests;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.dto.intervalos.IntervaloInsertDTO;

@Component
public class MetodosEntrenoService {
	/**
	 * Calcular la media ponderada de varios intervalos Este metodo se suele usar
	 * para poner la FC Media en un entrenamiento con intervalos
	 * 
	 * @param intervalos
	 * @return
	 */
	public Integer calcularFcMediaPonderada(List<IntervaloInsertDTO> intervalos) {
		double sumaFCPonderada = 0;
		long tiempoTotalConFC = 0;

		for (IntervaloInsertDTO i : intervalos) {
			// Solo sumamos si el intervalo tiene dato de FC media
			if (i.getFcMedia() != null && i.getFcMedia() > 0) {
				long duracionSegundos = i.getDuracion().toSecondOfDay();

				sumaFCPonderada += (i.getFcMedia() * duracionSegundos);
				tiempoTotalConFC += duracionSegundos;
			}
		}

		// Si ningún intervalo tenía FC, devolvemos null o 0
		if (tiempoTotalConFC == 0)
			return null;

		// Retornamos el promedio redondeado
		return (int) Math.round(sumaFCPonderada / tiempoTotalConFC);
	}

	
	
}
