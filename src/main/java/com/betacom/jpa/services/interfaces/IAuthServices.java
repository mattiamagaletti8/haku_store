package com.betacom.jpa.services.interfaces;

import com.betacom.jpa.dto.input.ForgotPasswordReq;
import com.betacom.jpa.dto.input.LoginReq;
import com.betacom.jpa.dto.input.ResetPasswordReq;
import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.AuthResponseDTO;

// Proprietario: Sarah
public interface IAuthServices {
	AuthResponseDTO register(UtenteReq req) throws Exception;

	AuthResponseDTO login(LoginReq req) throws Exception;

	void forgotPassword(ForgotPasswordReq req) throws Exception;

	void resetPassword(ResetPasswordReq req) throws Exception;
}
