package com.betacom.jpa.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.betacom.jpa.repositories.IUtenteRepository;

import lombok.RequiredArgsConstructor;

// Proprietario: Sara e Mattia
@RequiredArgsConstructor
@Service
public class UtenteDetailsService implements UserDetailsService {

	private final IUtenteRepository utR;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return utR.findByEmail(email)
				.map(UtentePrincipal::new)
				.orElseThrow(() -> new UsernameNotFoundException(email));
	}
}
