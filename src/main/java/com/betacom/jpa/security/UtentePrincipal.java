package com.betacom.jpa.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.betacom.jpa.enums.Roles;
import com.betacom.jpa.models.Utente;

import lombok.Getter;

@Getter
public class UtentePrincipal implements UserDetails {

	private final Integer idUtente;
	private final String email;
	private final String password;
	private final Roles ruolo;

	public UtentePrincipal(Utente ut) {
		this.idUtente = ut.getIdUtente();
		this.email = ut.getEmail();
		this.password = ut.getPassword();
		this.ruolo = ut.getRuolo();
	}

	public boolean isAdmin() {
		return ruolo == Roles.ADMIN;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + ruolo.name()));
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}
}
