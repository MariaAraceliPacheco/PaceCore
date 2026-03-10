package com.example.demo.security;

import java.security.Key;

import com.example.demo.entities.Usuario;

import jakarta.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class AutenticadorJWT {
	private static Key key = null;

	// este metodo se encarga de generar el token JWT a partir del usuario?
	public static String codificaJWT(Usuario u) {
		return Jwts.builder().setSubject(String.valueOf(u.getId()))
				.signWith(getGeneratedKey(), SignatureAlgorithm.HS512).compact();
	}

	public static int getIdUsuarioDesdeJWT(String jwt) {
		try {
			String stringIdUsuario = Jwts.parserBuilder().setSigningKey(getGeneratedKey()).build().parseClaimsJws(jwt)
					.getBody().getSubject();

			return Integer.parseInt(stringIdUsuario);
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}

	public static String getIdUsuarioDesdeJWT2(String jwt) {
		try {
			return Jwts.parserBuilder().setSigningKey(getGeneratedKey()).build().parseClaimsJws(jwt).getBody()
					.getSubject();

		} catch (Exception ex) {
			ex.printStackTrace();
			return "-1";
		}
	}

	public static int getIdUsuarioDesdeJwtIncrustadoEnRequest(HttpServletRequest request) {
		String autHeader = request.getHeader("Authorization");

		if (autHeader != null && autHeader.startsWith("Bearer ")) {
			String jwt = autHeader.substring(7);
			return getIdUsuarioDesdeJWT(jwt);
		}

		return -1;
	}

	// esto genera una clave aleatoria con la que encriptar los tokens
	// esto genería una clave diferente por cada vez que se llame a este metodo
	// (supongo)
	private static Key getGeneratedKey() {
		if (key == null) {
			key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		}

		return key;
	}
}
