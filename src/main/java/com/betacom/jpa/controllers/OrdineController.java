package com.betacom.jpa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.jpa.dto.input.OrdineReq;
import com.betacom.jpa.dto.input.ValidationGroups;
import com.betacom.jpa.dto.output.ResponseDTO;
import com.betacom.jpa.security.UtentePrincipal;
import com.betacom.jpa.services.interfaces.IOrdineServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Valerio
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/ordine")
public class OrdineController {
	private final IOrdineServices ordS;

	@PostMapping("checkout")
	public ResponseEntity<Object> checkout(
			@RequestBody(required = true) @Validated(ValidationGroups.Checkout.class) OrdineReq req,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		return ResponseEntity.ok(ordS.checkout(principal.getIdUtente(), req));
	}

	@GetMapping("/list")
	public ResponseEntity<Object> list(
			@RequestParam(required = false) Integer idUtente,
			@RequestParam(required = false) String stato,
			@RequestParam(required = false) String statoPagamento,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		return ResponseEntity.ok(ordS.list(principal.getIdUtente(), principal.isAdmin(), idUtente, stato, statoPagamento));
	}

	@GetMapping("getById")
	public ResponseEntity<Object> getById(
			@RequestParam(required = true) Integer id,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		return ResponseEntity.ok(ordS.getById(id, principal.getIdUtente(), principal.isAdmin()));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("updateStato")
	public ResponseEntity<ResponseDTO> updateStato(
			@RequestBody(required = true) @Validated(ValidationGroups.OrdineStato.class) OrdineReq req) throws Exception {
		ordS.updateStato(req);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("updated...")
				.build());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("updateStatoPagamento")
	public ResponseEntity<ResponseDTO> updateStatoPagamento(
			@RequestBody(required = true) @Validated(ValidationGroups.OrdineStato.class) OrdineReq req) throws Exception {
		ordS.updateStatoPagamento(req);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("updated...")
				.build());
	}
}
