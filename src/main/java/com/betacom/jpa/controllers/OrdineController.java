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

// ============================================================================
// PROPRIETARIO: Valerio — Modulo Ordini (Ordine / DettaglioOrdine / checkout)
// ============================================================================
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/ordine")
public class OrdineController {
	private final IOrdineServices ordS;

	// Nessun @PreAuthorize: basta essere autenticati. Il gruppo di validazione "Checkout" attiva
	// solo le regole idIndirizzo/metodoPagamento obbligatori, definite in OrdineReq
	@PostMapping("checkout")
	public ResponseEntity<Object> checkout(
			@RequestBody(required = true) @Validated(ValidationGroups.Checkout.class) OrdineReq req,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		// idUtente sempre dal principal autenticato, mai dal body: e' cosi' che nessuno puo'
		// fare checkout "per conto di" un altro utente
		return ResponseEntity.ok(ordS.checkout(principal.getIdUtente(), req));
	}

	// GET con filtri opzionali: il comportamento cambia in base al ruolo (vedi OrdineImpl.list),
	// ma il controller si limita a passare sia l'id del chiamante sia se e' admin, lasciando
	// la decisione "quali ordini mostrare davvero" al service
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

	// Solo ADMIN: cambia lo stato logistico. Gruppo di validazione "OrdineStato" (non Update):
	// richiede solo l'id, tutti gli altri campi di OrdineReq restano facoltativi
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
