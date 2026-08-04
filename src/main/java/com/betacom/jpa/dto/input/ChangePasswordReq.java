package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Sarah
@Setter
@Getter
@ToString
public class ChangePasswordReq {
	@NotBlank(message = "utente.no.password")
	private String oldPassword;

	@NotBlank(message = "utente.no.password")
	@Size(min = 8, message = "utente.password.short")
	private String newPassword;
}
