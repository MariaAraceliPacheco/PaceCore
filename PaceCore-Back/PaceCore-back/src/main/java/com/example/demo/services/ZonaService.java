package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.zonas.PorcentajesZonasDTO;
import com.example.demo.dto.zonas.ZonasUpdateDTO;
import com.example.demo.entities.Entreno;
import com.example.demo.entities.Intervalo;
import com.example.demo.entities.Usuario;
import com.example.demo.entities.ZonasUsuario;
import com.example.demo.repositories.EntrenoRepository;
import com.example.demo.repositories.UsuarioRepository;
import com.example.demo.repositories.ZonasUsuarioRepository;

@Service
public class ZonaService {

	private final ZonasUsuarioRepository repo;
	private final UsuarioRepository usuarioRepo;
	private final EntrenoRepository entrenoRepo;

	public ZonaService(ZonasUsuarioRepository repo, UsuarioRepository usuarioRepo, EntrenoRepository entrenoRepo) {
		super();
		this.repo = repo;
		this.usuarioRepo = usuarioRepo;
		this.entrenoRepo = entrenoRepo;
	}

	public List<ZonasUsuario> obtenerZonasPorUsuario(int id) {
		return repo.findByUsuarioIdOrderByNumeroZonaAsc(id);
	}

	@Transactional
	public void actualizarZonasUsuario(int idUsuario, List<ZonasUpdateDTO> dtos) {
		for (ZonasUpdateDTO a : dtos) {
			// obtener la zona original del usuario, luego se le cambiaran los datos
			// necesarios
			ZonasUsuario z = repo.findById(a.getId())
					.orElseThrow(() -> new RuntimeException("No se ha encontrado la zona del usuario"));

			// comprobar que la zona pertenece al usuario
			if (z.getUsuario().getId() != idUsuario) {
				throw new RuntimeException("no tienes permiso para editar esta zona");
			}

			z.setDescripcion(a.getDescripcion());
			z.setFcMaxima(a.getFc_maxima());
			z.setFcMinima(a.getFc_minima());
			z.setNombreZona(a.getNombre_zona());
			z.setNumeroZona(a.getNumero_zona());

			// como el metodo es transactional, se guardará todo al final del bucle
			repo.save(z);
		}
	}

	/**
	 * Este metodo servirá para que cuando el usuario se registre por primera vez,
	 * se generen a partir de su edad sus zonas por defecto usando la formula de la
	 * FC Maxima
	 * 
	 * @param idUsuario
	 * @return
	 */
	@Transactional
	public void crearZonaUsuario(int idUsuario) {
		Usuario u = usuarioRepo.findById(idUsuario)
				.orElseThrow(() -> new RuntimeException("No se ha encontrado el usuario"));
		int fcMaximaAbsoluta = (int) Math.round(208 - (0.7 * u.getEdad()));

		// estos son los porcentajes de cada zona
		// la primera columna es el porcentaje minimo de esa zona, y la segunda el
		// maximo, para obtener el rango completo
		double[][] rangos = { { 0.50, 0.60 }, // Z1
				{ 0.60, 0.70 }, // Z2
				{ 0.70, 0.80 }, // Z3
				{ 0.80, 0.90 }, // Z4
				{ 0.90, 1.00 } // Z5
		};

		String[] nombres = { "Z1", "Z2", "Z3", "Z4", "Z5" };
		String[] descripciones = { "Recuperación / Calentamiento", "Resistencia Aeróbica Baja",
				"Resistencia Aeróbica Media", "Umbral de Lactato", "Esfuerzo Máximo / VO2 Máx" };

		for (int i = 0; i < 5; i++) {
			ZonasUsuario zona = new ZonasUsuario();
			zona.setUsuario(u);
			zona.setNumeroZona(i + 1);
			zona.setNombreZona(nombres[i]);
			zona.setDescripcion(descripciones[i]);

			// el minimo de una es el maximo de la anterior mas o menos
			zona.setFcMinima((int) Math.round(fcMaximaAbsoluta * rangos[i][0]));
			zona.setFcMaxima((int) Math.round(fcMaximaAbsoluta * rangos[i][1]));

			repo.save(zona);
		}
	}

	public Integer determinarZona(List<ZonasUsuario> zonas, Integer pulsacionesMedias) {
		if (pulsacionesMedias == null || pulsacionesMedias <= 0) {
			return null;
		}

		for (ZonasUsuario z : zonas) {
			if (pulsacionesMedias >= z.getFcMinima() && pulsacionesMedias <= z.getFcMaxima()) {
				return z.getNumeroZona();
			}
		}

		// si está por debajo de la zona 1, se devuelve 1, si está por encima de la zona
		// 5, se devuelve 5
		if (!zonas.isEmpty() && pulsacionesMedias > zonas.get(4).getFcMaxima()) {
			return 5;
		}

		return 1;
	}

	@Transactional(readOnly = true)
	public PorcentajesZonasDTO obtenerPorcentajesZonasPorUsuario(int idUsuario, Integer tipoActividadId,
			LocalDateTime inicio, LocalDateTime fin) {
		List<Entreno> entrenos = new ArrayList<Entreno>();

		// CORRECCIÓN: Si tipoActividadId NO es null, usamos el método que filtra.
		// Si es null, traemos todos los entrenos del usuario.

		if (inicio != null && fin != null) {
			entrenos = entrenoRepo.findEntrenosParaEstadisticas(idUsuario, tipoActividadId, inicio, fin);
		} else if (tipoActividadId != null) {
			entrenos = entrenoRepo.findAllWithIntervalosByUsuarioAndTipoActividad(idUsuario, tipoActividadId);
		} else {
			entrenos = entrenoRepo.findAllWithIntervalosByUsuario(idUsuario);
		}

		/*
		 * Se usa un array de 6 indices para evitar estar restando todo el rato -1
		 * cuando especifiquemos la zona (zona - 1). Con 6 indices, para especificar la
		 * zona solo bastaria con poner "segundosAcumulados[i.getZonaAlcanzada()]" y ya.
		 * Asi el indice 1 hace referencia a la zona 1
		 */
		double[] segundosAcumulados = new double[6]; // para los indices del 1 al 5
		double segundosTotalesConDatoZonaAlcanzada = 0;

		for (Entreno e : entrenos) {
			// CASO A: El entreno tiene detalles (intervalos)
			if (e.getIntervalos() != null && !e.getIntervalos().isEmpty()) {
				for (Intervalo i : e.getIntervalos()) {
					// FILTRO CLAVE: ¿El intervalo es del tipo que buscamos?
					// Si tipoActividadId es null, entran todos. Si no, comparamos IDs.
					boolean coincideTipo = (tipoActividadId == null)
							|| (i.getTipoactividad() != null && i.getTipoactividad().getId().equals(tipoActividadId));

					if (coincideTipo && i.getZonaAlcanzada() != null) {
						int zona = i.getZonaAlcanzada();
						if (zona >= 1 && zona <= 5) {
							segundosAcumulados[zona] += i.getDuracion();
							segundosTotalesConDatoZonaAlcanzada += i.getDuracion();
						}
					}
				}
			}
			// CASO B: El entreno es una unidad simple (sin intervalos)
			else {
				// Verificamos si el entreno global coincide con el tipo buscado
				boolean coincideTipo = (tipoActividadId == null)
						|| (e.getTipoactividad() != null && e.getTipoactividad().getId().equals(tipoActividadId));

				if (coincideTipo && e.getZonaAlcanzada() != null) {
					int zona = e.getZonaAlcanzada();
					if (zona >= 1 && zona <= 5) {
						segundosAcumulados[zona] += e.getTiempoTotal();
						segundosTotalesConDatoZonaAlcanzada += e.getTiempoTotal();
					}
				}
			}
		}

		PorcentajesZonasDTO dto = new PorcentajesZonasDTO();
		if (segundosTotalesConDatoZonaAlcanzada > 0) {
			// aqui es donde se obtiene los prcentajes de tiempo que el usuario ha estado en
			// cada zona
			dto.setZona1(redondear((segundosAcumulados[1] / segundosTotalesConDatoZonaAlcanzada) * 100));
			dto.setZona2(redondear((segundosAcumulados[2] / segundosTotalesConDatoZonaAlcanzada) * 100));
			dto.setZona3(redondear((segundosAcumulados[3] / segundosTotalesConDatoZonaAlcanzada) * 100));
			dto.setZona4(redondear((segundosAcumulados[4] / segundosTotalesConDatoZonaAlcanzada) * 100));
			dto.setZona5(redondear((segundosAcumulados[5] / segundosTotalesConDatoZonaAlcanzada) * 100));
		}
		return dto;
	}

	private double redondear(double valor) {
		// al multiplicar por 100.0, redondear y luego divitir por 100.0, lo que se hace
		// es cortar el numero para quedarse solo con 2 decimales
		// en caso de que quisieramos solamente 1 decimal, multiplicariamos y
		// dividiriamos por 10.0
		// y si no queremos decimales, directamente no se multiplica ni divide el valor,
		// solo se le hace el Math.round y ya
		return Math.round(valor * 100.0) / 100.0; // para obtener 2 decimales
	}

}
