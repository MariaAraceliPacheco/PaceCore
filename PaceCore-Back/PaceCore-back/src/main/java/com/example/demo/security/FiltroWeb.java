package com.example.demo.security;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FiltroWeb implements Filter {

	/*
	 * Este filtro es como un guardia que se ejecuta antes de llegar a cualquier
	 * controller Su trabajo es decidir si deja pasar una peticion o la bloquea
	 * (dependerá del jwt que se envie)
	 * 
	 */

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/*
	 * En este metodo se definiran los endpoints PUBLICOS de la api
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		// una uri es una cadena de caracteres que identifica de forma unica a un
		// recurso en internet
		// puede ser de dos tipos, URL o URN
		// la URL indica donde se encuentra un recurso y como acceder a el
		// URN proporciona un nombre permanente a un recurso, sin indicar su ubicacion
		String uri = request.getRequestURI();
		String method = request.getMethod();

		// Permitir preflight CORS
		/*
		 * preftlight CORS es una solicitud de verificacion automatica que el navegador
		 * envia antes de realizar una peticion cross-origin que no cumple con los
		 * criterios de una solicitud simple. Sirve para asegurarse de que el servidor
		 * está preparado para aceptar una solicitud real
		 */
		if ("OPTIONS".equalsIgnoreCase(method)) {
			chain.doFilter(req, res);
			return;
		}

		// Endpoints públicos
		if (uri.equals("/auth/login") || uri.equals("/auth/register") || uri.startsWith("/v3/api-docs")
		        || uri.startsWith("/swagger-ui")
		        || uri.startsWith("/swagger-ui.html")) {
			chain.doFilter(req, res);
			return;
		}

		// Comprobar JWT
		Integer userId = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request);

		// si se ha enviado un id correcto, se envia al ciente el userId
		if (userId != null && userId != -1) {
			request.setAttribute("userId", userId);

			// esto pasa la peticion al siguiente paso de la cadena
			// ese siguiente paso puede ser otro filtro (si hay mas), o el controller que
			// corresponde al endpoint
			chain.doFilter(req, res);
			return;
		}

		// Bloquear acceso
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
	}

	@Override
	public void destroy() {

	}

}
