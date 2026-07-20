package com.betacom.jpa.services.interfaces;

import com.betacom.jpa.dto.input.LoginReq;
import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.AuthResponseDTO;

// Proprietario: Sarah
public interface IAuthServices {
	AuthResponseDTO register(UtenteReq req) throws Exception;

	AuthResponseDTO login(LoginReq req) throws Exception;
}
