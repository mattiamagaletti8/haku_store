package com.betacom.jpa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.jpa.dto.input.LoginReq;
import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.input.ValidationGroups;
import com.betacom.jpa.services.interfaces.IAuthServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
// Interamente pubblico (permitAll in SecurityConfig): senza questi due endpoint nessuno
// potrebbe mai ottenere un token per accedere al resto del sistema
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/auth")
public class AuthController {
	private final IAuthServices authS;

	// Gruppo "Create" di UtenteReq: nome/cognome/email/password diventano tutti obbligatori
	@PostMapping("register")
	public ResponseEntity<Object> register(
			@RequestBody(required = true) @Validated(ValidationGroups.Create.class) UtenteReq req) throws Exception {
		return ResponseEntity.ok(authS.register(req));
	}

	// @Valid semplice (non @Validated con gruppi): LoginReq non condivide la classe con nient'altro,
	// non serve il pattern ValidationGroups qui
	@PostMapping("login")
	public ResponseEntity<Object> login(@RequestBody(required = true) @Valid LoginReq req) throws Exception {
		return ResponseEntity.ok(authS.login(req));
	}
}
