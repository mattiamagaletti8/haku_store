package com.betacom.jpa.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
@Service
public class JwtService {

	// Letto da application.properties (jwt.secret=${jwt_secret}, nessun default: va fornito via env var)
	@Value("${jwt.secret}")
	private String secret;

	// jwt.expiration-ms=${jwt_expiration_ms:3600000} — default 1 ora se non specificato
	@Value("${jwt.expiration-ms}")
	private long expirationMs;

	public String generateToken(UtentePrincipal principal) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + expirationMs);

		return Jwts.builder()
				// subject = email: e' cosi' che extractEmail/isTokenValid identificano l'utente dal token
				.subject(principal.getUsername())
				// I claim idUtente/ruolo evitano una query al database ogni volta che servono
				// (es. UtentePrincipal.isAdmin() legge il ruolo direttamente dal token, non dal DB)
				.claim("idUtente", principal.getIdUtente())
				.claim("ruolo", principal.getRuolo().name())
				.issuedAt(now)
				.expiration(expiration)
				// Firma il token con la chiave HMAC derivata dal secret: senza questa firma,
				// nessuno potrebbe fidarsi che il token non sia stato alterato
				.signWith(getSigningKey())
				.compact();
	}

	public long getExpirationMs() {
		return expirationMs;
	}

	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		String email = extractEmail(token);
		// Entrambe le condizioni devono essere vere: l'email nel token corrisponde all'utente
		// caricato E il token non e' scaduto
		return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}

	private <T> T extractClaim(String token, Function<Claims, T> resolver) {
		// parseSignedClaims verifica la firma nel processo stesso: se il token e' stato alterato
		// o firmato con una chiave diversa, questa chiamata lancia eccezione prima di restituire nulla
		Claims claims = Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return resolver.apply(claims);
	}

	private SecretKey getSigningKey() {
		// La STESSA identica chiave firma ogni token generato e verifica ogni token ricevuto:
		// se il secret cambiasse, tutti i token gia' emessi diventerebbero invalidi
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}
}
