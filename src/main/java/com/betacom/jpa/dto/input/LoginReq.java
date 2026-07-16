package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
// Classe separata da UtenteReq apposta: il login e' un'unica operazione che non condivide
// gruppi di validazione con nient'altro, non serve il pattern ValidationGroups qui
@Setter
@Getter
@ToString
public class LoginReq {
	@NotBlank(message = "auth.no.email")
	@Email(message = "utente.email.invalid")
	private String email;

	@NotBlank(message = "auth.no.password")
	private String password;
}
