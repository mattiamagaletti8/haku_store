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

// Proprietario: Sarah
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthImpl implements IAuthServices {

	private final IUtenteServices utenteS;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	@Override
	public AuthResponseDTO register(UtenteReq req) throws Exception {
		log.debug("register {}", req);
		utenteS.create(req);
		Utente ut = utenteS.getEntityByEmail(req.getEmail());
		return buildAuthResponse(ut);
	}

	@Override
	public AuthResponseDTO login(LoginReq req) throws Exception {
		log.debug("login {}", req.getEmail());
		Utente ut = utenteS.getEntityByEmail(req.getEmail());

		if (!passwordEncoder.matches(req.getPassword(), ut.getPassword()))
			throw new ApiException("auth.badcredentials");

		return buildAuthResponse(ut);
	}

	private AuthResponseDTO buildAuthResponse(Utente ut) {
		UtentePrincipal principal = new UtentePrincipal(ut);
		String token = jwtService.generateToken(principal);

		return AuthResponseDTO.builder()
				.token(token)
				.tokenType("Bearer")
				.expiresIn(jwtService.getExpirationMs() / 1000)
				.utente(UtenteMap.buildUtenteDTO(ut))
				.build();
	}

}
