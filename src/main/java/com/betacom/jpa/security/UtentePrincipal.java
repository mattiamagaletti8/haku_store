package com.betacom.jpa.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.betacom.jpa.enums.Roles;
import com.betacom.jpa.models.Utente;

import lombok.Getter;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
// L'identita' che finisce nel SecurityContext — quello che @AuthenticationPrincipal inietta
// in ogni controller di tutto il backend
@Getter
public class UtentePrincipal implements UserDetails {

	private final Integer idUtente;
	private final String email;
	private final String password;
	private final Roles ruolo;

	// Costruito da un Utente reale: copia solo i 4 campi che servono all'autenticazione,
	// mai l'intera entity JPA (niente indirizzi/ordini/recensioni trascinati dentro il principal)
	public UtentePrincipal(Utente ut) {
		this.idUtente = ut.getIdUtente();
		this.email = ut.getEmail();
		this.password = ut.getPassword();
		this.ruolo = ut.getRuolo();
	}

	// Il metodo che ogni service richiama per i controlli di proprieta' manuali
	// (update/delete/getById su Utente, Indirizzo, Recensione, Ordine)
	public boolean isAdmin() {
		return ruolo == Roles.ADMIN;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Questa singola riga e' cio' che rende davvero funzionante @PreAuthorize("hasRole('ADMIN')")
		// su tutti i controller del backend: il prefisso "ROLE_" e' una convenzione richiesta da Spring Security
		return List.of(new SimpleGrantedAuthority("ROLE_" + ruolo.name()));
	}

	@Override
	public String getUsername() {
		// Il nostro "username" e' l'email, non un campo separato
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}
}
