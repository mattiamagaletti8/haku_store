package com.betacom.jpa.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.betacom.jpa.services.interfaces.IMessaggioServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
// Il gemello di ApiAuthEntryPoint per il caso "sei autenticato ma non hai il ruolo giusto"
// (es. un CLIENTE che chiama un endpoint @PreAuthorize("hasRole('ADMIN')"))
@RequiredArgsConstructor
@Component
public class ApiAccessDeniedHandler implements AccessDeniedHandler {

	private final IMessaggioServices msgS;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
			throws IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		// Registrato insieme a ApiAuthEntryPoint dentro .exceptionHandling(...) in SecurityConfig,
		// cosi' i due casi (401 vs 403) sono gestiti in un unico punto coerente con lo stesso formato di risposta
		response.getWriter().write(JsonMsg.responseDTOJson(msgS.get("auth.forbidden")));
	}
}
