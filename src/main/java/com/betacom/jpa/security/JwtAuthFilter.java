package com.betacom.jpa.security;

import java.io.IOException;

// classi di Spring Security che servono per autenticare l'utente a partire dal token
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// Proprietario: Sara e Mattia
@RequiredArgsConstructor   // Lombok genera il costruttore con i 2 campi final qui sotto
@Component                 // Spring lo registra come filtro da applicare alle richieste
public class JwtAuthFilter extends OncePerRequestFilter { // OncePerRequestFilter: garantisce che il filtro giri una sola volta per richiesta

	private static final String HEADER = "Authorization"; // nome dell'header HTTP dove sta il token
	private static final String PREFIX = "Bearer ";       // il token arriva sempre preceduto da "Bearer "

	// crea e legge i token JWT
	private final JwtService jwtService;
	// carica l'utente dal database in base all'email trovata nel token
	private final UtenteDetailsService utenteDetailsService;

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		String header = request.getHeader(HEADER);   // legge l'header Authorization della richiesta
		if (header == null || !header.startsWith(PREFIX)) {
			// niente token: si passa avanti senza autenticare, ci penserà Spring Security a bloccare se serve
			filterChain.doFilter(request, response);
			return;
		}

		String token = header.substring(PREFIX.length());  // toglie il prefisso "Bearer " e tiene solo il token
		String email;
		try {
			email = jwtService.extractEmail(token);  // legge l'email contenuta nel token
		} catch (Exception e) {
			// token non leggibile o scaduto: si passa avanti senza autenticare
			filterChain.doFilter(request, response);
			return;
		}

		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// se non c'è già un utente autenticato in questa richiesta, prova ad autenticarlo
			UserDetails userDetails = utenteDetailsService.loadUserByUsername(email);
			if (jwtService.isTokenValid(token, userDetails)) {
				// token valido: crea l'oggetto di autenticazione che vuole Spring Security
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken); // salva l'utente autenticato per questa richiesta
			}
		}

		filterChain.doFilter(request, response); // passa la richiesta al prossimo filtro/controller
	}
}
