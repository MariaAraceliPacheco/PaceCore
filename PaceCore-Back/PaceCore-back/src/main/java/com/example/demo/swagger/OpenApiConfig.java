package com.example.demo.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		final String securitySchemeName = "BearerAuth";

		// para que swagger sepa que esta api usa jwt, será necesario definirselo aqui.
		// A partir del metodo .addList() para delante ya es lo que hay que poner para
		// definirselo.
		// esto permitirá que al usar swagger muestre el input para meter el jwt y nos
		// permita usar los endpoints privados (definidos con el candado)
		return new OpenAPI()
				.info(new Info().title("PaceCore API").version("1.0")
						.description("API para gestión de usuarios, entrenos, intervalos y tipo de actividad"))
				.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
				.components(new Components().addSecuritySchemes(securitySchemeName, new SecurityScheme()
						.name("Authorization").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
	}
}
