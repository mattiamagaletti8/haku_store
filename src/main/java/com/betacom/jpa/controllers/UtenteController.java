package com.betacom.jpa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.jpa.dto.input.ChangePasswordReq;
import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.input.ValidationGroups;
import com.betacom.jpa.dto.output.ResponseDTO;
import com.betacom.jpa.security.UtentePrincipal;
import com.betacom.jpa.services.interfaces.IUtenteServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Sarah
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/utente")
public class UtenteController {
	private final IUtenteServices utS;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/list")
	public ResponseEntity<Object> list() throws Exception {
		return ResponseEntity.ok(utS.list());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("create")
	public ResponseEntity<Object> create(
			@RequestBody(required = true) @Validated(ValidationGroups.Create.class) UtenteReq req) throws Exception {
		return ResponseEntity.ok(utS.createByAdmin(req));
	}

	@GetMapping("/me")
	public ResponseEntity<Object> me(@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		return ResponseEntity.ok(utS.getById(principal.getIdUtente(), principal.getIdUtente(), false));
	}

	@GetMapping("getById")
	public ResponseEntity<Object> getById(
			@RequestParam(required = true) Integer id,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		return ResponseEntity.ok(utS.getById(id, principal.getIdUtente(), principal.isAdmin()));
	}

	@PatchMapping("update")
	public ResponseEntity<ResponseDTO> update(
			@RequestBody(required = true) @Validated(ValidationGroups.Update.class) UtenteReq req,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		utS.update(req, principal.getIdUtente(), principal.isAdmin());
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("updated...")
				.build());
	}

	@PatchMapping("changePassword")
	public ResponseEntity<ResponseDTO> changePassword(
			@RequestBody(required = true) @Valid ChangePasswordReq req,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		utS.changePassword(principal.getIdUtente(), req);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("Password aggiornata con successo.")
				.build());
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<ResponseDTO> delete(
			@PathVariable(required = true) Integer id,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		String msg = utS.delete(id, principal.getIdUtente(), principal.isAdmin());
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg(msg)
				.build());
	}

}
