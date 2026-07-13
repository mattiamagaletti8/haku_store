package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UtenteReq {
	@NotNull(groups = ValidationGroups.Update.class, message = "utente.no.id")
	private Integer id;

	@NotNull(groups = ValidationGroups.Create.class, message = "utente.no.nome")
	@NotBlank(groups = ValidationGroups.Create.class, message = "utente.no.nome")
	private String nome;

	@NotNull(groups = ValidationGroups.Create.class, message = "utente.no.cognome")
	@NotBlank(groups = ValidationGroups.Create.class, message = "utente.no.cognome")
	private String cognome;

	@NotNull(groups = ValidationGroups.Create.class, message = "utente.no.email")
	@NotBlank(groups = ValidationGroups.Create.class, message = "utente.no.email")
	@Email(groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "utente.email.invalid")
	private String email;

	@NotNull(groups = ValidationGroups.Create.class, message = "utente.no.password")
	@Size(min = 8, groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "utente.password.short")
	private String password;

	private String telefono;

	/**
	 * Applicato solo se il chiamante e' ADMIN; ignorato in fase di self-registrazione/self-update.
	 */
	private String ruolo;
}
