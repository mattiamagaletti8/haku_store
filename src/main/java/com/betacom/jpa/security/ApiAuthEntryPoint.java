package com.betacom.jpa.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.betacom.jpa.services.interfaces.IMessaggioServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
// Cosa risponde Spring Security quando manca un token valido, PRIMA che la richiesta
// arrivi a un controller: registrato in SecurityConfig dentro .exceptionHandling(...)
@RequiredArgsConstructor
@Component
public class ApiAuthEntryPoint implements AuthenticationEntryPoint {

	// Collegamento verso il sistema i18n condiviso: stesso meccanismo usato da ExceptionManager
	private final IMessaggioServices msgS;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		// Scrive a mano lo stesso formato {"msg": "..."} di ogni altro errore del backend, anche se qui
		// Jackson e Spring MVC non sono ancora entrati in gioco: la security filter chain agisce
		// prima che la richiesta raggiunga il livello dei controller/ExceptionManager
		response.getWriter().write(JsonMsg.responseDTOJson(msgS.get("auth.unauthorized")));
	}
}
