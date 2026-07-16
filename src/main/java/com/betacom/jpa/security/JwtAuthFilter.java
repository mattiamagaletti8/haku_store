package com.betacom.jpa.security;

import java.io.IOException;

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

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
// Il filtro che gira su OGNI richiesta HTTP, prima ancora che arrivi a un controller:
// registrato in SecurityConfig con addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final String HEADER = "Authorization";
	private static final String PREFIX = "Bearer ";

	private final JwtService jwtService;
	private final UtenteDetailsService utenteDetailsService;

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		// Se manca l'header o non inizia con "Bearer ", la richiesta prosegue NON autenticata
		// (non e' un errore qui: potrebbe essere un endpoint pubblico come /rest/categoria/list)
		String header = request.getHeader(HEADER);
		if (header == null || !header.startsWith(PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = header.substring(PREFIX.length());
		String email;
		try {
			// Estrae l'email dal token, verificandone la firma nel processo
			email = jwtService.extractEmail(token);
		} catch (Exception e) {
			// Token corrotto o scaduto: stesso comportamento del caso "nessun header",
			// la richiesta prosegue non autenticata (sara' poi ApiAuthEntryPoint a rispondere 401
			// se l'endpoint richiedeva autenticazione)
			filterChain.doFilter(request, response);
			return;
		}

		// Solo se c'e' un'email valida E non c'e' gia' un'autenticazione nel contesto
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = utenteDetailsService.loadUserByUsername(email);
			// isTokenValid ricontrolla firma+scadenza+corrispondenza email: solo se tutto torna,
			// l'utente viene "riconosciuto" per il resto della richiesta
			if (jwtService.isTokenValid(token, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// Da questo punto in poi, @AuthenticationPrincipal nei controller e @PreAuthorize
				// funzionano perche' il SecurityContext e' stato popolato qui
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		filterChain.doFilter(request, response);
	}
}
