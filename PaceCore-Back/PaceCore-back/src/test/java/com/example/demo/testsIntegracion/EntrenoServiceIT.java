package com.example.demo.testsIntegracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dto.entrenos.EntrenoCreateDTO;
import com.example.demo.dto.intervalos.IntervaloInsertDTO;
import com.example.demo.dto.usuarios.UsuarioInsertDTO;
import com.example.demo.entities.Entreno;
import com.example.demo.entities.Tipoactividad;
import com.example.demo.entities.Usuario;
import com.example.demo.repositories.EntrenoRepository;
import com.example.demo.repositories.TipoActividadRepository;
import com.example.demo.services.AuthService;
import com.example.demo.services.EntrenoService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class EntrenoServiceIT {

	@Autowired
	private EntrenoService entrenoService;

	@Autowired
	private TipoActividadRepository tipoActividadRepo;

	@Autowired
	private EntrenoRepository entrenoRepo;

	@Autowired
	private AuthService authService;

	@Test
	void crearEntrenoSinIntervalos_guardaEntrenoCorrectamente() {

		UsuarioInsertDTO us = new UsuarioInsertDTO();
		us.setEdad(20);
		us.setEmail("paco2@gmail.com");
		us.setNombre("paco");
		us.setPassword("1234");

		Usuario u = authService.register(us);

		Tipoactividad ta = new Tipoactividad();
		ta.setNombre("nadar");
		ta.setId(10);
		ta = tipoActividadRepo.save(ta);

		EntrenoCreateDTO dto = new EntrenoCreateDTO();
		dto.setTitulo("entreno sin intervalos");
		dto.setDistancia(BigDecimal.valueOf(5));
		dto.setFecha(Timestamp.valueOf(LocalDateTime.now()));
		dto.setTipo_actividad_id(ta.getId());
		dto.setFcMedia(150);
		dto.setFcMaxima(170);
		dto.setId_usuario(u.getId());
		dto.setIntervalos(null); // caso 1 sin intervalos
		dto.setTiempo_total(LocalTime.of(0, 30, 0));
		
		Entreno entrenoguardado = entrenoService.crearEntrenoCompleto(dto);

		// comprobar que se ha guardado en la BD
		assertNotNull(entrenoguardado.getId());
		assertTrue(entrenoRepo.findById(entrenoguardado.getId()).isPresent());

		// comprobar valors copiados del DTO
		assertEquals(BigDecimal.valueOf(5), entrenoguardado.getDistancia());
		assertEquals(LocalTime.of(0, 30, 0).toSecondOfDay(), entrenoguardado.getTiempoTotal());
		assertEquals(150, entrenoguardado.getFcMedia());
		assertEquals(170, entrenoguardado.getFcMaxima());
		assertEquals("entreno sin intervalos", entrenoguardado.getTitulo());

		// comprobar relaciones
		assertEquals(u.getId(), entrenoguardado.getUsuario().getId());
		assertEquals(ta.getId(), entrenoguardado.getTipoactividad().getId());

		// comprobar que no hay intervalos
		assertNull(entrenoguardado.getIntervalos());

		// comprobar que se asignó la zona (las zonas existen porque se crean al crear
		// el usuario)
		assertEquals(3, entrenoguardado.getZonaAlcanzada());
	}

	@Test
	void crearEntrenoConIntervalos_guardaEntrenoCorrectamente() {

		UsuarioInsertDTO us = new UsuarioInsertDTO();
		us.setEdad(20);
		us.setEmail("paco2@gmail.com");
		us.setNombre("paco");
		us.setPassword("1234");

		Usuario u = authService.register(us);

		Tipoactividad ta = new Tipoactividad();
		ta.setNombre("nadar");
		ta.setId(10);
		ta = tipoActividadRepo.save(ta);

		EntrenoCreateDTO dto = new EntrenoCreateDTO();
		dto.setTitulo("entreno con intervalos");
		dto.setDistancia(BigDecimal.valueOf(5));
		dto.setFecha(Timestamp.valueOf(LocalDateTime.now()));
		dto.setTipo_actividad_id(ta.getId());
		dto.setFcMedia(150);
		dto.setFcMaxima(170);
		dto.setId_usuario(u.getId());

		List<IntervaloInsertDTO> lista = new ArrayList<IntervaloInsertDTO>();
		IntervaloInsertDTO iDto = new IntervaloInsertDTO();
		iDto.setDistancia(BigDecimal.valueOf(3));
		iDto.setDuracion(LocalTime.of(0, 4, 0));
		iDto.setFcMaxima(140);
		iDto.setFcMedia(120);
		iDto.setTipoActividadId(ta.getId());
		iDto.setZona_alcanzada(3);

		IntervaloInsertDTO iDto2 = new IntervaloInsertDTO();
		iDto2.setDistancia(BigDecimal.valueOf(3));
		iDto2.setDuracion(LocalTime.of(0, 1, 0));
		iDto2.setFcMaxima(190);
		iDto2.setFcMedia(170);
		iDto2.setTipoActividadId(ta.getId());
		iDto2.setZona_alcanzada(5);

		lista.add(iDto);
		lista.add(iDto2);

		dto.setIntervalos(lista); // caso 2 con intervalos
		dto.setTiempo_total(LocalTime.of(0, 30, 0));

		Entreno entrenoguardado = entrenoService.crearEntrenoCompleto(dto);

		// comprobar que se ha guardado en la BD
		assertNotNull(entrenoguardado.getId());
		assertTrue(entrenoRepo.findById(entrenoguardado.getId()).isPresent());

		// comprobar valors copiados del DTO
		assertEquals(BigDecimal.valueOf(6), entrenoguardado.getDistancia());
		assertEquals(LocalTime.of(0, 5, 0).toSecondOfDay(), entrenoguardado.getTiempoTotal());
		assertEquals(130, entrenoguardado.getFcMedia());
		assertEquals(190, entrenoguardado.getFcMaxima());
		assertEquals("entreno con intervalos", entrenoguardado.getTitulo());

		// comprobar relaciones
		assertEquals(u.getId(), entrenoguardado.getUsuario().getId());
		assertEquals(ta.getId(), entrenoguardado.getTipoactividad().getId());

		// comprobar que no hay intervalos
		assertFalse(entrenoguardado.getIntervalos().isEmpty());

		// comprobar que se asignó la zona (las zonas existen porque se crean al crear
		// el usuario)
		assertEquals(4, entrenoguardado.getZonaAlcanzada());
	}

}
