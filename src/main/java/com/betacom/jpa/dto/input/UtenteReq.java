package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
// Usato sia per la registrazione (AuthController.register) sia per l'update del proprio profilo
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
	// @Email vale sia in Create che in Update: se l'email viene cambiata, deve restare un'email valida
	@Email(groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "utente.email.invalid")
	private String email;

	@NotNull(groups = ValidationGroups.Create.class, message = "utente.no.password")
	// Lunghezza minima 8: unica regola di robustezza sulla password, applicata sia in creazione che in update
	@Size(min = 8, groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "utente.password.short")
	private String password;

	// Nessuna validazione: il telefono e' sempre facoltativo
	private String telefono;

	/**
	 * Applicato solo se il chiamante e' ADMIN; ignorato in fase di self-registrazione/self-update.
	 */
	// Stringa e non l'enum Roles direttamente: il service decide se onorarla o ignorarla
	// in base a chi sta chiamando (un cliente non puo' auto-promuoversi ad ADMIN passando questo campo)
	private String ruolo;
}
