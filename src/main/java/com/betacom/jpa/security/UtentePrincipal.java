package com.betacom.jpa.security;

import java.util.Collection;
import java.util.List;

// classi di Spring Security che rappresentano un utente autenticato e i suoi permessi
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.betacom.jpa.enums.Roles;
import com.betacom.jpa.models.Utente;

import lombok.Getter;

// Proprietario: Sara e Mattia
@Getter   // Lombok genera i getter per tutti i campi qui sotto
public class UtentePrincipal implements UserDetails { // "adatta" il nostro Utente al formato richiesto da Spring Security

	private final Integer idUtente;
	private final String email;
	private final String password;
	private final Roles ruolo;

	public UtentePrincipal(Utente ut) {
		// copia i dati dall'entità Utente presa dal database
		this.idUtente = ut.getIdUtente();
		this.email = ut.getEmail();
		this.password = ut.getPassword();
		this.ruolo = ut.getRuolo();
	}

	public boolean isAdmin() {
		// metodo comodo per controllare velocemente se l'utente è admin
		return ruolo == Roles.ADMIN;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Spring Security vuole i ruoli con il prefisso "ROLE_" (es. ROLE_ADMIN)
		return List.of(new SimpleGrantedAuthority("ROLE_" + ruolo.name()));
	}

	@Override
	public String getUsername() {
		// per Spring Security lo "username" è l'email
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}
}
