package com.betacom.jpa.services.implementations;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.betacom.jpa.dto.input.ForgotPasswordReq;
import com.betacom.jpa.dto.input.LoginReq;
import com.betacom.jpa.dto.input.ResetPasswordReq;
import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.AuthResponseDTO;
import com.betacom.jpa.exceptions.ApiException;
import com.betacom.jpa.mapping.UtenteMap;
import com.betacom.jpa.models.Utente;
import com.betacom.jpa.repositories.IUtenteRepository;
import com.betacom.jpa.security.JwtService;
import com.betacom.jpa.security.UtentePrincipal;
import com.betacom.jpa.services.interfaces.IAuthServices;
import com.betacom.jpa.services.interfaces.IUtenteServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Sarah
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthImpl implements IAuthServices {

	private static final int RESET_TOKEN_VALIDITA_MINUTI = 30;

	private final IUtenteServices utenteS;
	private final IUtenteRepository utR;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final EmailService emailService;

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

		if (!ut.isAttivo())
			throw new ApiException("auth.disabilitato");

		return buildAuthResponse(ut);
	}

	@Transactional
	@Override
	public void forgotPassword(ForgotPasswordReq req) throws Exception {
		log.debug("forgotPassword {}", req.getEmail());
		// non si rivela se l'email esiste o meno: risposta sempre generica lato controller
		utR.findByEmail(req.getEmail()).ifPresent(ut -> {
			ut.setResetToken(UUID.randomUUID().toString());
			ut.setResetTokenScadenza(LocalDateTime.now().plusMinutes(RESET_TOKEN_VALIDITA_MINUTI));
			emailService.inviaResetPassword(ut, ut.getResetToken());
		});
	}

	@Transactional
	@Override
	public void resetPassword(ResetPasswordReq req) throws Exception {
		log.debug("resetPassword");
		Utente ut = utR.findByResetToken(req.getToken())
				.orElseThrow(() -> new ApiException("auth.token.nonvalido"));

		if (ut.getResetTokenScadenza() == null || ut.getResetTokenScadenza().isBefore(LocalDateTime.now()))
			throw new ApiException("auth.token.nonvalido");

		ut.setPassword(passwordEncoder.encode(req.getNewPassword()));
		ut.setResetToken(null);
		ut.setResetTokenScadenza(null);
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
