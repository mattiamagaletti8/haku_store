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

import com.betacom.jpa.dto.input.IndirizzoReq;
import com.betacom.jpa.dto.input.ValidationGroups;
import com.betacom.jpa.dto.output.ResponseDTO;
import com.betacom.jpa.security.UtentePrincipal;
import com.betacom.jpa.services.interfaces.IIndirizzoServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Sarah
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/indirizzo")
public class IndirizzoController {
	private final IIndirizzoServices indS;

	@GetMapping("/list")
	public ResponseEntity<Object> list(@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		return ResponseEntity.ok(indS.listByUtente(principal.getIdUtente()));
	}

	@GetMapping("getById")
	public ResponseEntity<Object> getById(
			@RequestParam(required = true) Integer id,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		return ResponseEntity.ok(indS.getById(id, principal.getIdUtente(), principal.isAdmin()));
	}

	@PostMapping("create")
	public ResponseEntity<ResponseDTO> create(
			@RequestBody(required = true) @Validated(ValidationGroups.Create.class) IndirizzoReq req,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		indS.create(req, principal.getIdUtente());
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("created...")
				.build());
	}

	@PatchMapping("update")
	public ResponseEntity<ResponseDTO> update(
			@RequestBody(required = true) @Validated(ValidationGroups.Update.class) IndirizzoReq req,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		indS.update(req, principal.getIdUtente(), principal.isAdmin());
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("updated...")
				.build());
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<ResponseDTO> delete(
			@PathVariable(required = true) Integer id,
			@AuthenticationPrincipal UtentePrincipal principal) throws Exception {
		indS.delete(id, principal.getIdUtente(), principal.isAdmin());
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("deleted...")
				.build());
	}
}
