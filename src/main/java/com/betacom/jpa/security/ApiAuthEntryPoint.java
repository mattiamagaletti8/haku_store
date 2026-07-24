package com.betacom.jpa.security;

import java.io.IOException;

// classi di Spring Security per gestire la risposta quando manca l'autenticazione
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.betacom.jpa.services.interfaces.IMessaggioServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// Proprietario: Sara e Mattia
@RequiredArgsConstructor   // Lombok genera il costruttore con il campo final qui sotto
@Component                 // Spring Security usa questa classe quando manca il login o il token non è valido
public class ApiAuthEntryPoint implements AuthenticationEntryPoint {

	// serve per recuperare il testo del messaggio di errore da restituire
	private final IMessaggioServices msgS;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);    // imposta il codice di risposta 401 (non autenticato)
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);  // dice al client che la risposta è in formato JSON
		response.getWriter().write(JsonMsg.responseDTOJson(msgS.get("auth.unauthorized"))); // scrive il messaggio di errore nel body della risposta
	}
}
