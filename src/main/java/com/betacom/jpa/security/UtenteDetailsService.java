package com.betacom.jpa.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.betacom.jpa.repositories.IUtenteRepository;

import lombok.RequiredArgsConstructor;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
// Il ponte tra Spring Security e il database: implementa l'interfaccia standard UserDetailsService
// richiesta da tutta l'infrastruttura Spring Security per sapere "come trovare un utente dato lo username"
@RequiredArgsConstructor
@Service
public class UtenteDetailsService implements UserDetailsService {

	// Collegamento diretto verso il modulo Utente: nessuna logica di business qui, solo lookup
	private final IUtenteRepository utR;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// Cerca per email (il nostro "username"), la mappa nel principal minimale, o lancia
		// l'eccezione standard di Spring Security se non esiste
		return utR.findByEmail(email)
				.map(UtentePrincipal::new)
				.orElseThrow(() -> new UsernameNotFoundException(email));
	}
}
