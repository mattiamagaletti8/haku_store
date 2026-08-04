package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Sarah
@Setter
@Getter
@ToString
public class ForgotPasswordReq {
	@NotBlank(message = "auth.no.email")
	@Email(message = "utente.email.invalid")
	private String email;
}
