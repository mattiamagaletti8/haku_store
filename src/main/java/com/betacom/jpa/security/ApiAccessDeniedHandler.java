package com.betacom.jpa.security;

import java.io.IOException;

// classi di Spring Security per gestire la risposta quando l'accesso è negato
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.betacom.jpa.services.interfaces.IMessaggioServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// Proprietario: Sara e Mattia
@RequiredArgsConstructor   // Lombok genera il costruttore con il campo final qui sotto
@Component                 // Spring Security usa questa classe quando un utente non ha i permessi giusti
public class ApiAccessDeniedHandler implements AccessDeniedHandler {

	// serve per recuperare il testo del messaggio di errore da restituire
	private final IMessaggioServices msgS;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
			throws IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);       // imposta il codice di risposta 403 (accesso negato)
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);  // dice al client che la risposta è in formato JSON
		response.getWriter().write(JsonMsg.responseDTOJson(msgS.get("auth.forbidden"))); // scrive il messaggio di errore nel body della risposta
	}
}
