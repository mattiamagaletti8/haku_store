package com.betacom.jpa.controllers;

import org.springframework.http.ResponseEntity;
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

import com.betacom.jpa.dto.input.RecensioneReq;
import com.betacom.jpa.dto.input.ValidationGroups;
import com.betacom.jpa.dto.output.ResponseDTO;
import com.betacom.jpa.security.UtentePrincipal;
import com.betacom.jpa.services.interfaces.IRecensioneServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Sarah
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/recensione")
public class RecensioneController {
	private final IRecensioneServices recS;

	// GET /rest/recensione/list?idProdotto=... — pubblico, nessun login richiesto (vedi SecurityConfig)
	@GetMapping("/list")
	public ResponseEntity<Object> list(@RequestParam(required = true) Integer idProdotto) throws Exception {
		return ResponseEntity.ok(recS.listByProdotto(idProdotto));
	}

	// POST /rest/recensione/create — utente autenticato: la recensione viene sempre creata a nome di chi chiama
	@PostMapping("create")
	public ResponseEntity<ResponseDTO> create(
			@RequestBody(required = true) @Validated(ValidationGroups.Create.class) RecensioneReq req,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		recS.create(req, principal.getIdUtente());
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("created...")
				.build());
	}

	// PATCH /rest/recensione/update — solo l'autore o un ADMIN (controllo fatto nel service)
	@PatchMapping("update")
	public ResponseEntity<ResponseDTO> update(
			@RequestBody(required = true) @Validated(ValidationGroups.Update.class) RecensioneReq req,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		recS.update(req, principal.getIdUtente(), principal.isAdmin());
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("updated...")
				.build());
	}

	// DELETE /rest/recensione/delete/{id} — solo l'autore o un ADMIN
	@DeleteMapping("delete/{id}")
	public ResponseEntity<ResponseDTO> delete(
			@PathVariable(required = true) Integer id,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		recS.delete(id, principal.getIdUtente(), principal.isAdmin());
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("deleted...")
				.build());
	}
}
