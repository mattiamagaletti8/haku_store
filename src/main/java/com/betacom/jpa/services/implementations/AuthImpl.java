package com.betacom.jpa.services.implementations;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.LoginReq;
import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.AuthResponseDTO;
import com.betacom.jpa.exceptions.ApiException;
import com.betacom.jpa.mapping.UtenteMap;
import com.betacom.jpa.models.Utente;
import com.betacom.jpa.security.JwtService;
import com.betacom.jpa.security.UtentePrincipal;
import com.betacom.jpa.services.interfaces.IAuthServices;
import com.betacom.jpa.services.interfaces.IUtenteServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
// Il punto d'ingresso dell'autenticazione: nessuna logica di password/token duplicata altrove,
// tutto passa da qui prima di raggiungere JwtService/PasswordEncoder
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthImpl implements IAuthServices {

	// Riusa IUtenteServices invece di IUtenteRepository direttamente: la creazione di un utente
	// segue sempre la stessa logica (hash password, ruolo forzato a CLIENTE), niente duplicazione
	private final IUtenteServices utenteS;
	private final PasswordEncoder passwordEncoder;
	// Collegamento verso l'infrastruttura JWT: qui si genera il token, non nel controller
	private final JwtService jwtService;

	@Override
	public AuthResponseDTO register(UtenteReq req) throws Exception {
		log.debug("register {}", req);
		// Delega la creazione vera e propria a UtenteImpl.create (stessa validazione duplicati email,
		// stesso hashing password, ruolo forzato a CLIENTE)
		utenteS.create(req);
		Utente ut = utenteS.getEntityByEmail(req.getEmail());
		// Login automatico subito dopo la registrazione: l'utente non deve rifare login separatamente
		return buildAuthResponse(ut);
	}

	@Override
	public AuthResponseDTO login(LoginReq req) throws Exception {
		log.debug("login {}", req.getEmail());
		Utente ut = utenteS.getEntityByEmail(req.getEmail());

		// Confronto sicuro: passwordEncoder.matches ricalcola l'hash della password in chiaro e lo
		// confronta con quello salvato, non decifra mai la password salvata (BCrypt e' one-way)
		if (!passwordEncoder.matches(req.getPassword(), ut.getPassword()))
			throw new ApiException("auth.badcredentials");

		return buildAuthResponse(ut);
	}

	private AuthResponseDTO buildAuthResponse(Utente ut) {
		// Costruisce il principal minimale (id/email/password/ruolo) a partire dall'entity completa
		UtentePrincipal principal = new UtentePrincipal(ut);
		String token = jwtService.generateToken(principal);

		return AuthResponseDTO.builder()
				.token(token)
				.tokenType("Bearer")
				// Convertito da millisecondi a secondi: il frontend lavora in secondi
				.expiresIn(jwtService.getExpirationMs() / 1000)
				.utente(UtenteMap.buildUtenteDTO(ut))
				.build();
	}

}
