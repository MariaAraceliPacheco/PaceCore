package com.example.demo.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.recomendaciones.BloqueEntrenamiento;
import com.example.demo.dto.recomendaciones.EntrenamientoSugeridoDTO;
import com.example.demo.dto.zonas.PorcentajesZonasDTO;
import com.example.demo.entities.EntrenamientoSugerido;
import com.example.demo.entities.EntrenamientoSugerido.EstadoSugerencia;
import com.example.demo.entities.Entreno;
import com.example.demo.entities.Intervalo;
import com.example.demo.entities.Usuario;
import com.example.demo.repositories.EntrenamientoSugeridoRepository;
import com.example.demo.repositories.EntrenoRepository;
import com.example.demo.repositories.TipoActividadRepository;
import com.example.demo.repositories.UsuarioRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RecomendadorService {

	private final EntrenamientoSugeridoRepository repo;
	private final EntrenoRepository entrenoRepo;
	private final ObjectMapper objectMapper; // esto sirve para convertir la lista de bloques a JSON
	private final UsuarioRepository usuarioRepo;
	private final ZonaService zonaService;
	private final TipoActividadRepository tipoActividadRepo;

	public RecomendadorService(EntrenamientoSugeridoRepository repo, EntrenoRepository entrenoRepo,
			ObjectMapper objectMapper, UsuarioRepository usuarioRepo, ZonaService zonaService,
			TipoActividadRepository tipoActividadRepo) {
		super();
		this.repo = repo;
		this.entrenoRepo = entrenoRepo;
		this.objectMapper = objectMapper;
		this.usuarioRepo = usuarioRepo;
		this.zonaService = zonaService;
		this.tipoActividadRepo = tipoActividadRepo;
	}

	@Transactional()
	public EntrenamientoSugeridoDTO generarSugerencia(int idUsuario) {
		LocalDateTime inicio = LocalDate.now().atStartOfDay(); // inicio del dia
		LocalDateTime fin = LocalDate.now().atTime(LocalTime.MAX); // fin del dia

		// si el usuario tiene un entrenamiento sugerido generado hoy, no se le vuelve a
		// crear otro
		Optional<EntrenamientoSugerido> existente = repo.findSugerenciaDeHoy(idUsuario, inicio, fin);

		// se le devuelve el que ya tiene
		if (existente.isPresent()) {
			return entidadToDto(existente.get());
		}

		LocalDateTime inicioMes = LocalDateTime.now().minusMonths(1);
		PorcentajesZonasDTO stats = zonaService.obtenerPorcentajesZonasPorUsuario(idUsuario, null, inicioMes,
				LocalDateTime.now());
		double kmsSemanales = obtenerKmsSemanalesUsuario(idUsuario);
		long diasSinEntrenar = obtenerDiasSinEntrenar(idUsuario);
		double mediaKms = obtenerMediaDeKmsMensuales(idUsuario);

		EntrenamientoSugerido entreno = determinarRegla(stats, kmsSemanales, diasSinEntrenar, inicioMes, idUsuario,
				mediaKms);

		return entidadToDto(entreno);
	}

	@Transactional
	public void actualizarEstado(int idEntrenamiento, EstadoSugerencia estado) {

		if (estado.equals(EstadoSugerencia.COMPLETADO)) {
			completarSugerencia(idEntrenamiento);
			return;
		}

		EntrenamientoSugerido e = repo.findById(idEntrenamiento)
				.orElseThrow(() -> new RuntimeException("No se ha encontrado el entrenamiento"));
		e.setEstado(estado);
		repo.save(e);
	}

	@Transactional
	public void completarSugerencia(int idSugerencia) {
		EntrenamientoSugerido e = repo.findById(idSugerencia)
				.orElseThrow(() -> new RuntimeException("No se ha encontrado el entrenamiento"));

		e.setEstado(EstadoSugerencia.COMPLETADO);
		repo.save(e);

		Entreno en = new Entreno();
		en.setUsuario(e.getUsuario());
		en.setTitulo(e.getTitulo());
		en.setFecha(Timestamp.valueOf(e.getFechaGeneracion()));
		en.setTiempoTotal(calcularTiempoTotalDesdeJson(e.getEstructuraJson()));
		en.setDescripcion(e.getDescripcion());

		double distanciaTotal = calcularDistanciaDesdeJson(e.getEstructuraJson());
		en.setDistancia(BigDecimal.valueOf(distanciaTotal));
		en.setTipoactividad(tipoActividadRepo.findByNombre("Correr"));

		entrenoRepo.save(en);
	}

	private EntrenamientoSugerido determinarRegla(PorcentajesZonasDTO stats, double kmSemanales, long diasSinEntrenar,
			LocalDateTime inicioMes, int idUsuario, double mediaKms) {
		if (diasSinEntrenar > 7) {
			// 1. lleva mucho tiempo parado?
			return crearEntrenoActivacion(idUsuario);
		} else if ((stats.getZona4() + stats.getZona5()) > 30) {
			// 2. se está pasando de intensidad?
			return crearEntrenoRecuperacion(idUsuario);
		} else if (stats.getZona2() < 60) {
			// 3. Se está pasando de volumen?
			return crearEntrenoBaseAerobica(idUsuario);
		} else if (kmSemanales > (mediaKms * 1.2)) {
			// 4. Le falta base aerobica?
			return crearEntrenoDescarga(idUsuario);
		} else if ((stats.getZona4() + stats.getZona5()) < 5) {
			// 5. Está estancado siempre en lo mismo?
			return crearEntrenoIntervalos(idUsuario);
		} else {
			// DEFAULT: mantenimiento
			return crearEntrenoMantenimiento(idUsuario);
		}
	}

	private EntrenamientoSugerido crearEntrenoActivacion(int idUsuario) {

		LocalDateTime inicioMes = LocalDateTime.now().minusMonths(1);
		double ritmoMedioUltimoMes = ritmoMedioEntrenamientosDelUltimoMes(idUsuario, inicioMes);

		if (ritmoMedioUltimoMes <= 0) {
			// Manejo de caso si no hay entrenos previos
			ritmoMedioUltimoMes = 7.0; // Un ritmo por defecto de 7:00 min/km
		}

		double ritmoMedioEnSegundos = ritmoMedioUltimoMes * 60;

		// 2. Logica de ritmos
		int ritmoSugeridoSegCalentamiento = (int) (ritmoMedioEnSegundos + 85);
		String ritmoFinalCalentamiento = formatearRitmo(ritmoSugeridoSegCalentamiento);

		int ritmoSugeridoSegPrincipal = (int) (ritmoMedioEnSegundos + 40);
		String ritmoFinalPrincipal = formatearRitmo(ritmoSugeridoSegPrincipal);

		// 3. Construcción de los bloques
		List<BloqueEntrenamiento> bloques = new ArrayList<>();
		String titulo = "Reactivacion Muscular";
		String descripcion = "Llevas unos días de descanso y tu cuerpo necesita despertar progresivamente. "
				+ "Hoy el objetivo no es la velocidad, sino volver a sentir el movimiento sin estrés para tu corazón.";

		bloques.add(new BloqueEntrenamiento("Calentamiento", 600, 1, "Trote muy suave", ritmoFinalCalentamiento));
		bloques.add(new BloqueEntrenamiento("Principal", 900, 2, "Mantente en zona 2", ritmoFinalPrincipal));
		bloques.add(new BloqueEntrenamiento("Enfriamiento", 300, 1, "Caminar", "Libre"));

		// 4. Llamar al método que guarda en la BD y convierte a JSON
		Usuario usuario = usuarioRepo.findById(idUsuario).orElseThrow();

		return guardarSugerencia(usuario, titulo, descripcion, bloques);
	}

	private EntrenamientoSugerido crearEntrenoRecuperacion(int idUsuario) {

		LocalDateTime inicioMes = LocalDateTime.now().minusMonths(1);
		double ritmoMedioUltimoMes = ritmoMedioEntrenamientosDelUltimoMes(idUsuario, inicioMes);

		if (ritmoMedioUltimoMes <= 0) {
			// Manejo de caso si no hay entrenos previos
			ritmoMedioUltimoMes = 7.0; // Un ritmo por defecto de 7:00 min/km
		}

		double ritmoMedioEnSegundos = ritmoMedioUltimoMes * 60;

		// 2. Lógica de ritmos
		int ritmoSugeridoSegPrincipal = (int) (ritmoMedioEnSegundos + 1.40);
		String ritmoFinalPrincipal = formatearRitmo(ritmoSugeridoSegPrincipal);

		// 3. Construccion de los bloques
		List<BloqueEntrenamiento> bloques = new ArrayList<>();
		String titulo = "Reparación y Asimilación";
		String descripcion = "El entrenamiento no termina cuando dejas de correr, sino cuando tu cuerpo asimila el esfuerzo. "
				+ "Hoy rodar suave es la mejor inversión para que tu próxima sesión intensa sea realmente efectiva.";

		bloques.add(new BloqueEntrenamiento("Principal", 1800, 1, "Mantente en zona 1", ritmoFinalPrincipal));

		// 4. Llamar al método que guarda en la BD y convierte a JSON
		Usuario usuario = usuarioRepo.findById(idUsuario).orElseThrow();

		return guardarSugerencia(usuario, titulo, descripcion, bloques);
	}

	private EntrenamientoSugerido crearEntrenoBaseAerobica(int idUsuario) {

		LocalDateTime inicioMes = LocalDateTime.now().minusMonths(1);
		double ritmoMedioUltimoMes = ritmoMedioEntrenamientosDelUltimoMes(idUsuario, inicioMes);

		if (ritmoMedioUltimoMes <= 0) {
			// Manejo de caso si no hay entrenos previos
			ritmoMedioUltimoMes = 7.0; // Un ritmo por defecto de 7:00 min/km
		}

		double ritmoMedioEnSegundos = ritmoMedioUltimoMes * 60;

		// 2. Lógica de ritmos
		int ritmoSugeridoSegCalentamiento = (int) (ritmoMedioEnSegundos * 1.45);
		String ritmoFinalCalentamiento = formatearRitmo(ritmoSugeridoSegCalentamiento);

		int ritmoSugeridoSegPrincipal = (int) (ritmoMedioEnSegundos * 1.15);
		String ritmoFinalPrincipal = formatearRitmo(ritmoSugeridoSegPrincipal);

		// 3. Construcción de los bloques
		List<BloqueEntrenamiento> bloques = new ArrayList<>();
		String titulo = "Construcción de Resistencia Base";
		String descripcion = "Para correr rápido, primero hay que ser eficiente. "
				+ "Este entrenamiento de baja intensidad pero mayor duración enseña a tu cuerpo a utilizar las grasas como combustible"
				+ " y fortalece tus capilares para transportar más oxígeno a los músculos.";

		bloques.add(new BloqueEntrenamiento("Calentamiento", 600, 1, "Trote muy suave", ritmoFinalCalentamiento));
		bloques.add(new BloqueEntrenamiento("Principal", 2400, 2, "Mantente en zona 2", ritmoFinalPrincipal));
		bloques.add(new BloqueEntrenamiento("Enfriamiento", 300, 1, "Caminar", ritmoFinalCalentamiento));

		// 4. Llamar al método que guarda en la BD y convierte a JSON
		Usuario usuario = usuarioRepo.findById(idUsuario).orElseThrow();

		return guardarSugerencia(usuario, titulo, descripcion, bloques);
	}

	private EntrenamientoSugerido crearEntrenoDescarga(int idUsuario) {

		LocalDateTime inicioMes = LocalDateTime.now().minusMonths(1);
		double ritmoMedioUltimoMes = ritmoMedioEntrenamientosDelUltimoMes(idUsuario, inicioMes);

		if (ritmoMedioUltimoMes <= 0) {
			// Manejo de caso si no hay entrenos previos
			ritmoMedioUltimoMes = 7.0; // Un ritmo por defecto de 7:00 min/km
		}

		double ritmoMedioEnSegundos = ritmoMedioUltimoMes * 60;

		// 2. Lógica de ritmos (Casteamos a int para evitar errores con formatearRitmo)
		int ritmoSugeridoSegPrincipal = (int) (ritmoMedioEnSegundos + 50);
		String ritmoFinalPrincipal = formatearRitmo(ritmoSugeridoSegPrincipal);

		// 3. Construccion de los bloques
		List<BloqueEntrenamiento> bloques = new ArrayList<>();
		String titulo = "Sesión de Descarga Preventiva";
		String descripcion = "Hemos detectado un aumento significativo en tus kilómetros semanales. "
				+ "Para evitar lesiones por sobrecarga y permitir que tus articulaciones se adapten al nuevo volumen, "
				+ "hoy realizaremos una sesión corta de impacto reducido.";

		bloques.add(new BloqueEntrenamiento("Principal", 1800, 2, "Mantente en zona 1/2", ritmoFinalPrincipal));

		// 4. Llamar al método que guarda en la BD y convierte a JSON
		Usuario usuario = usuarioRepo.findById(idUsuario).orElseThrow();

		return guardarSugerencia(usuario, titulo, descripcion, bloques);
	}

	private EntrenamientoSugerido crearEntrenoIntervalos(int idUsuario) {

		LocalDateTime inicioMes = LocalDateTime.now().minusMonths(1);
		double ritmoMedioUltimoMes = ritmoMedioEntrenamientosDelUltimoMes(idUsuario, inicioMes);

		if (ritmoMedioUltimoMes <= 0) {
			// Manejo de caso si no hay entrenos previos
			ritmoMedioUltimoMes = 7.0; // Un ritmo por defecto de 7:00 min/km
		}

		double ritmoMedioEnSegundos = ritmoMedioUltimoMes * 60;

		// 2. Lógica de ritmos (Casteamos a int para evitar errores con formatearRitmo)
		int ritmoSugeridoSegCalentamiento = (int) (ritmoMedioEnSegundos + 85);
		String ritmoFinalCalentamiento = formatearRitmo(ritmoSugeridoSegCalentamiento);

		int ritmoSugeridoIntervalos = (int) (ritmoMedioEnSegundos - 50);
		String ritmoFinalIntervalos = formatearRitmo(ritmoSugeridoIntervalos);

		// 3. Construcción de los bloques
		List<BloqueEntrenamiento> bloques = new ArrayList<>();
		String titulo = "Desafío de V02 Máximo";
		String descripcion = "Tus entrenamientos han sido muy constantes, pero para mejorar tu marca necesitamos estresar "
				+ "el sistema cardiovascular. Estas series cortas obligarán a tu corazón a ser más eficiente y a tus piernas a ganar potencia.";

		bloques.add(new BloqueEntrenamiento("Calentamiento", 600, 1, "Trote muy suave", ritmoFinalCalentamiento));

		// 6 intervalos de 2 minutos en zona 5
		for (int i = 0; i < 5; i++) {
			bloques.add(new BloqueEntrenamiento("Principal", 120, 5,
					"Alcanza la zona 4/5. No deberias poder hablar durante este bloque", ritmoFinalIntervalos));

			// El descanso (Z1) - Fundamental para bajar pulsaciones
			if (i < 6) { // No hace falta descanso después de la ultima serie porque lo siguiente es el
							// enfriamiento
				bloques.add(new BloqueEntrenamiento("Recuperación", 90, 1, "Trota muy suave o camina",
						ritmoFinalCalentamiento));
			}
		}

		bloques.add(new BloqueEntrenamiento("Enfriamiento", 600, 1, "Caminar", "Libre"));

		// 4. Llamar al método que guarda en la BD y convierte a JSON
		Usuario usuario = usuarioRepo.findById(idUsuario).orElseThrow();

		return guardarSugerencia(usuario, titulo, descripcion, bloques);
	}

	private EntrenamientoSugerido crearEntrenoMantenimiento(int idUsuario) {

		LocalDateTime inicioMes = LocalDateTime.now().minusMonths(1);
		double ritmoMedioUltimoMes = ritmoMedioEntrenamientosDelUltimoMes(idUsuario, inicioMes);

		if (ritmoMedioUltimoMes <= 0) {
			// Manejo de caso si no hay entrenos previos
			ritmoMedioUltimoMes = 7.0; // Un ritmo por defecto de 7:00 min/km
		}

		double ritmoMedioEnSegundos = ritmoMedioUltimoMes * 60;

		// 2. Lógica de ritmos (Casteamos a int para evitar errores con formatearRitmo)
		int ritmoSugeridoSegPrincipal = (int) (ritmoMedioEnSegundos);
		String ritmoFinalPrincipal = formatearRitmo(ritmoSugeridoSegPrincipal);

		// 3. Construccion de los bloques
		List<BloqueEntrenamiento> bloques = new ArrayList<>();
		String titulo = "Rodaje de Mantenimiento";
		String descripcion = "Estás manteniendo una buena constancia. La sesión de hoy busca estabilizar tus mejoras recientes "
				+ "y mantener el tono muscular sin acumular fatiga excesiva para los próximos días.";

		bloques.add(new BloqueEntrenamiento("Rodaje constante", 2400, 3, "Mantente en zona 2/3", ritmoFinalPrincipal));

		// 4. Llamar al método que guarda en la BD y convierte a JSON
		Usuario usuario = usuarioRepo.findById(idUsuario).orElseThrow();

		return guardarSugerencia(usuario, titulo, descripcion, bloques);
	}

	private EntrenamientoSugerido guardarSugerencia(Usuario user, String titulo, String descripcion,
			List<BloqueEntrenamiento> bloques) {
		try {
			String jsonEstructura = objectMapper.writeValueAsString(bloques);
			EntrenamientoSugerido sugerencia = new EntrenamientoSugerido();
			sugerencia.setUsuario(user);
			sugerencia.setTitulo(titulo);
			sugerencia.setDescripcion(descripcion);
			sugerencia.setEstructuraJson(jsonEstructura);
			sugerencia.setFechaGeneracion(LocalDateTime.now());
			sugerencia.setEstado(EstadoSugerencia.SUGERIDO);

			return repo.save(sugerencia);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error al generar el JSON del entrenamiento", e);
		}
	}

	/**
	 * Devuelve el ritmo medio del ultimo mes en segundos
	 * 
	 * @param idUsuario
	 * @param inicio
	 * @return
	 */
	@Transactional
	private double ritmoMedioEntrenamientosDelUltimoMes(int idUsuario, LocalDateTime inicio) {
		List<Entreno> entrenamientos = entrenoRepo.findEntrenosParaEstadisticas(idUsuario, null, inicio,
				LocalDateTime.now());

		double tiempoTotalSeg = 0;
		double distanciaTotalKm = 0;

		for (Entreno e : entrenamientos) {
			if (esRunning(e.getTipoactividad().getNombre()) && e.getIntervalos() == null
					&& e.getIntervalos().isEmpty()) {
				// si no tiene intervalos, se coge solamente los datos del entreno
				tiempoTotalSeg += e.getTiempoTotal();
				distanciaTotalKm += (e.getDistancia() != null) ? e.getDistancia().doubleValue() : 0;
			}

			if (esRunning(e.getTipoactividad().getNombre()) && !e.getIntervalos().isEmpty()) {
				for (Intervalo i : e.getIntervalos()) {
					tiempoTotalSeg += i.getDuracion();
					distanciaTotalKm += (i.getDistancia() != null) ? i.getDistancia().doubleValue() : 0;
				}
			}
		}

		// Ritmo = Minutos Totales / Kilómetros Totales
		if (distanciaTotalKm > 0) {
			return (tiempoTotalSeg / 60.0) / distanciaTotalKm;
		}

		return 0;
	}

	@Transactional
	private double obtenerMediaDeKmsMensuales(int idUsuario) {
		List<Entreno> entrenamientos = entrenoRepo.findEntrenosParaEstadisticas(idUsuario, null,
				LocalDateTime.now().minusMonths(1), LocalDateTime.now());
		double kms = 0;

		for (Entreno e : entrenamientos) {
			if (esRunning(e.getTipoactividad().getNombre())) {
				kms += e.getDistancia().doubleValue();
			}

			for (Intervalo i : e.getIntervalos()) {
				if (esRunning(i.getTipoactividad().getNombre())) {
					kms += i.getDistancia().doubleValue();
				}
			}
		}

		// la media es calculando el total de km del mes entre el numero de semanas del
		// mes (4)
		return kms / 4;
	}

	@Transactional
	private double obtenerKmsSemanalesUsuario(int idUsuario) {
		List<Entreno> entrenamientos = entrenoRepo.findEntrenosParaEstadisticas(idUsuario, null,
				LocalDateTime.now().minusWeeks(1), LocalDateTime.now());
		double kms = 0;

		for (Entreno e : entrenamientos) {
			if (esRunning(e.getTipoactividad().getNombre())) {
				kms += e.getDistancia().doubleValue();
			}

			for (Intervalo i : e.getIntervalos()) {
				if (esRunning(i.getTipoactividad().getNombre())) {
					kms += i.getDistancia().doubleValue();
				}
			}
		}

		return kms;
	}

	private long obtenerDiasSinEntrenar(int idUsuario) {
		Optional<LocalDateTime> fechaUltimo = entrenoRepo.findFechaUltimoEntreno(idUsuario);

		if (fechaUltimo.isEmpty()) {
			return 99; // se devuelve un valor alto para indicar que el usuario es nuevo o está
						// inactivo
		}

		return ChronoUnit.DAYS.between(fechaUltimo.get(), LocalDateTime.now());
	}

	private boolean esRunning(String tipo) {
		return tipo.toLowerCase().equals("correr") || tipo.toLowerCase().equals("trail");
	}

	public String calcularRitmoObjetivo(double ritmoMedioDecimal, double porcentajeIncremento) {
		// 1. Convertir a segundos totales (ritmoMedioDecimal ej: 8.5)
		double segundosTotales = ritmoMedioDecimal * 60;

		// 2. Aplicar incremento (porcentajeIncremento ej: 0.40 para zona 2)
		double ritmoObjetivoSegundos = segundosTotales * (1 + porcentajeIncremento);

		// 3. Formatear a String MM:SS
		int minutos = (int) (ritmoObjetivoSegundos / 60);
		int segundos = (int) (ritmoObjetivoSegundos % 60);

		return String.format("%d:%02d", minutos, segundos);
	}

	private String formatearRitmo(double segundosTotales) {
		if (segundosTotales <= 0) {
			return "Ritmo libre";
		}

		int totales = (int) Math.round(segundosTotales);

		int minutos = totales / 60;
		int segundos = totales % 60;

		// %d es para los minutos y %02d es para los segundos (se pone el 0 a la
		// izquierda si es menor de 10)
		return String.format("%d:%02d min/km", minutos, segundos);
	}

	public EntrenamientoSugeridoDTO entidadToDto(EntrenamientoSugerido entidad) {
		EntrenamientoSugeridoDTO dto = new EntrenamientoSugeridoDTO();
		dto.setId(entidad.getId());
		dto.setTitulo(entidad.getTitulo());
		dto.setDescripcion(entidad.getDescripcion());
		dto.setFechaGeneracion(entidad.getFechaGeneracion());
		dto.setEstado(entidad.getEstado());

		// Convertir el String JSON de la BD a List<BloqueEntrenamiento>
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<BloqueEntrenamiento> listaBloques = mapper.readValue(entidad.getEstructuraJson(),
					new TypeReference<List<BloqueEntrenamiento>>() {
					});
			dto.setBloques(listaBloques);
		} catch (JsonProcessingException e) {
			dto.setBloques(new ArrayList<>()); // Evitamos que el Front rompa si falla el parseo
		}

		return dto;
	}

	private double calcularDistanciaDesdeJson(String json) {
		double distanciaTotalKm = 0;

		try {
			List<BloqueEntrenamiento> bloques = objectMapper.readValue(json,
					new TypeReference<List<BloqueEntrenamiento>>() {
					});

			for (BloqueEntrenamiento bloque : bloques) {
				double ritmoSegundosPerKm = convertirRitmoASegundos(bloque.getRitmoObjetivo());

				if (ritmoSegundosPerKm > 0) {
					double distanciaBloque = (double) bloque.getDuracionSegundos() / ritmoSegundosPerKm;
					distanciaTotalKm += distanciaBloque;
				}
			}
		} catch (JsonProcessingException e) {
			System.err.println("Error al parsear el JSON de la sugerencia: " + e.getMessage());
		}

		return Math.round(distanciaTotalKm * 100.0) / 100.0;
	}

	// convierte un string de "5:30min/km" a 330 segundos
	private double convertirRitmoASegundos(String ritmoStr) {
		if (ritmoStr == null || ritmoStr.contains("Libre"))
			return 0;

		try {
			// Limpiamos el String para quedarnos solo con "5:30"
			String tiempo = ritmoStr.replace(" min/km", "").trim();
			String[] partes = tiempo.split(":");

			int minutos = Integer.parseInt(partes[0]);
			int segundos = Integer.parseInt(partes[1]);

			return (minutos * 60) + segundos;
		} catch (Exception e) {
			return 0;
		}
	}

	private long calcularTiempoTotalDesdeJson(String json) {
		long tiempoTotalSegundos = 0;

		try {
			List<BloqueEntrenamiento> bloques = objectMapper.readValue(json,
					new TypeReference<List<BloqueEntrenamiento>>() {
					});

			for (BloqueEntrenamiento bloque : bloques) {
				tiempoTotalSegundos += bloque.getDuracionSegundos();
			}
		} catch (JsonProcessingException e) {
			// TODO: handle exception
			System.err.println("Error al calcular el tiempo: " + e.getMessage());
		}

		return tiempoTotalSegundos;
	}
}
