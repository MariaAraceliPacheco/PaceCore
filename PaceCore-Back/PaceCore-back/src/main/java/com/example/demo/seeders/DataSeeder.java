package com.example.demo.seeders;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.demo.entities.Entreno;
import com.example.demo.entities.Intervalo;
import com.example.demo.entities.Tipoactividad;
import com.example.demo.entities.Usuario;
import com.example.demo.repositories.EntrenoRepository;
import com.example.demo.repositories.IntervaloRepository;
import com.example.demo.repositories.TipoActividadRepository;
import com.example.demo.repositories.UsuarioRepository;

// @Component
public class DataSeeder {

	private final String[] tiposActividad = { "Correr", "Andar", "Bicicleta de montaña", "Bicicleta", "Trail" };

	// @Bean
	CommandLineRunner initUsuarios(UsuarioRepository usuarioRepo, EntrenoRepository entrenoRepo,
			TipoActividadRepository actividadRepo, IntervaloRepository intervaloRepo) {
		return args -> {

			// ----------------- Insertar usuarios ------------------
			// Si se quiere evitar duplicados al reiniciar la app
			List<Usuario> usuarios = new ArrayList<>();
			if (usuarioRepo.count() == 0) {
				for (int i = 1; i <= 5; i++) {
					Usuario u = new Usuario();
					u.setNombre("Usuario" + i);
					u.setEmail("usuario" + i + "@mail.com");
					u.setRol("USUARIO");
					u.setPassword("pass" + i);
					u.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
					usuarioRepo.save(u);
					usuarios.add(u);
				}
			} else {
				usuarios = usuarioRepo.findAll(); // obtener los existentes
			}

			// ----------------- Insertar TipoActividad ------------------
			// hace falta crear el repo del tipo de actividad primero antes de crear los
			// entrenos
			List<Tipoactividad> actividades = new ArrayList<>();
			if (actividadRepo.count() == 0) {
				for (int i = 0; i < tiposActividad.length; i++) {
					Tipoactividad ta = new Tipoactividad();
					ta.setNombre(tiposActividad[i]);
					actividadRepo.save(ta);
					actividades.add(ta);
				}
			} else {
				actividades = actividadRepo.findAll();
			}

			// ----------------- Insertar entrenos ------------------
			if (entrenoRepo.count() < 10) {
				Random rand = new Random();
				for (int i = 1; i <= 10; i++) {
					Entreno e = new Entreno();
					e.setDistancia(new BigDecimal(rand.nextInt(5000))); // distancia en metros
					//e.setTiempoTotal(new Time(rand.nextInt(3600) * 1000)); // tiempo aleatorio
					e.setFecha(new Timestamp(System.currentTimeMillis()));

					// asignar un usuario aleatorio
					Usuario u = usuarios.get(rand.nextInt(usuarios.size()));
					e.setUsuario(u);
					Tipoactividad ta = actividades.get(rand.nextInt(actividades.size()));
					e.setTipoactividad(ta);
					entrenoRepo.save(e);
				}
			}

			// ----------------- Insertar intervalos ------------------
			// hace falta acceder al la lita de tipoActividad para poder acceder a sus IDs,
			// y acceder a la lista de los entrenos
			// y a partir de ahi, generar lo intervalos poniendo una duraccion y ritmo o
			// distancia concreta

			List<Entreno> entrenos = new ArrayList<Entreno>();
			if (entrenoRepo.count() != 0) {
				entrenos = entrenoRepo.findAll();
			}

			if (intervaloRepo.count() <= 10) {
				for (int i = 0; i < 10; i++) {
					Intervalo in = new Intervalo();
					Random rand = new Random();
					Random rand2 = new Random();
					// asignar entreno aleatorio
					Entreno e = entrenos.get(rand.nextInt(entrenos.size()));
					//Asignar tipoActividad aleatoria
					Tipoactividad ta = actividades.get(rand2.nextInt(actividades.size()));
					in.setEntreno(e);
					in.setTipoactividad(ta);
					// in.setDuracion(new LocalTime(rand.nextInt(3600) * 100));
					in.setDistancia(new BigDecimal(rand.nextInt(5000)));
					intervaloRepo.save(in);
				}
			}
		};
	}

}
