package com.betacom.jpa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.jpa.dto.input.CarrelloReq;
import com.betacom.jpa.dto.input.DettaglioCarrelloReq;
import com.betacom.jpa.dto.input.ValidationGroups;
import com.betacom.jpa.dto.output.ResponseDTO;
import com.betacom.jpa.security.UtentePrincipal;
import com.betacom.jpa.services.interfaces.ICarrelloServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Pier
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/carrello")
public class CarrelloController {
	private final ICarrelloServices carS;

	@GetMapping
	public ResponseEntity<Object> getCarrello(@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		return ResponseEntity.ok(carS.getCarrello(principal.getIdUtente()));
	}

	@PostMapping("items")
	public ResponseEntity<ResponseDTO> addItem(
			@RequestBody(required = true) @Validated(ValidationGroups.Create.class) DettaglioCarrelloReq req,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		carS.addItem(principal.getIdUtente(), req);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("added...")
				.build());
	}

	@PatchMapping("items")
	public ResponseEntity<ResponseDTO> updateItem(
			@RequestBody(required = true) @Validated(ValidationGroups.Update.class) DettaglioCarrelloReq req,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		carS.updateItemQuantity(principal.getIdUtente(), req.getIdVariante(), req.getQuantita());
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("updated...")
				.build());
	}

	@DeleteMapping("items/{idVariante}")
	public ResponseEntity<ResponseDTO> removeItem(
			@PathVariable(required = true) Integer idVariante,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		carS.removeItem(principal.getIdUtente(), idVariante);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("deleted...")
				.build());
	}

	@PostMapping("coupon")
	public ResponseEntity<ResponseDTO> applyCoupon(
			@RequestBody(required = true) @Validated(ValidationGroups.Coupon.class) CarrelloReq req,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		carS.applyCoupon(principal.getIdUtente(), req.getCodiceCoupon());
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("coupon applicato...")
				.build());
	}

	@DeleteMapping("coupon")
	public ResponseEntity<ResponseDTO> removeCoupon(@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		carS.removeCoupon(principal.getIdUtente());
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("coupon rimosso...")
				.build());
	}

	@DeleteMapping("clear")
	public ResponseEntity<ResponseDTO> clear(@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		carS.clear(principal.getIdUtente());
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("carrello svuotato...")
				.build());
	}
}
