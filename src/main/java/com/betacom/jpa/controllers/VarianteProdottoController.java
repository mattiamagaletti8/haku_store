package com.betacom.jpa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.betacom.jpa.dto.input.ValidationGroups;
import com.betacom.jpa.dto.input.VarianteProdottoReq;
import com.betacom.jpa.dto.output.ResponseDTO;
import com.betacom.jpa.services.interfaces.IVarianteProdottoServices;

import lombok.RequiredArgsConstructor; 
import lombok.extern.slf4j.Slf4j;      
// Proprietario: Mattia
@Slf4j
@RequiredArgsConstructor
@RestController                                
@RequestMapping("/rest/varianteProdotto")     
public class VarianteProdottoController {
	private final IVarianteProdottoServices varS;

	// GET /rest/varianteProdotto/list?idProdotto=... - restituisce tutte le varianti di un prodotto, accessibile a chiunque
	@GetMapping("/list")
	public ResponseEntity<Object> list(@RequestParam(required = true) Integer idProdotto) throws Exception {
		return ResponseEntity.ok(varS.listByProdotto(idProdotto));
	}

	// GET /rest/varianteProdotto/getById?id=... - restituisce una variante, accessibile a chiunque
	@GetMapping("getById")
	public ResponseEntity<Object> getById(@RequestParam(required = true) Integer id) throws Exception {
		return ResponseEntity.ok(varS.getById(id));
	}

	// POST /rest/varianteProdotto/create - crea una variante, solo per utenti ADMIN
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("create")
	public ResponseEntity<ResponseDTO> create(
			@RequestBody(required = true) @Validated(ValidationGroups.Create.class) VarianteProdottoReq req) throws Exception {
		varS.create(req);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("created...")
				.build());
	}

	// PATCH /rest/varianteProdotto/update - modifica una variante, solo per utenti ADMIN
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("update")
	public ResponseEntity<ResponseDTO> update(
			@RequestBody(required = true) @Validated(ValidationGroups.Update.class) VarianteProdottoReq req) throws Exception {
		varS.update(req);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("updated...")
				.build());
	}

	// DELETE /rest/varianteProdotto/delete/{id} - elimina una variante, solo per utenti ADMIN
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("delete/{id}")
	public ResponseEntity<ResponseDTO> delete(@PathVariable(required = true) Integer id) throws Exception {
		varS.delete(id);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("deleted...")
				.build());
	}
}
