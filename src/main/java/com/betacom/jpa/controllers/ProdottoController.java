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

import com.betacom.jpa.dto.input.ProdottoReq;
import com.betacom.jpa.dto.input.ValidationGroups;
import com.betacom.jpa.dto.output.ResponseDTO;
import com.betacom.jpa.services.interfaces.IProdottoServices;

import lombok.RequiredArgsConstructor; 
import lombok.extern.slf4j.Slf4j;      
// Proprietario: Mattia
@Slf4j
@RequiredArgsConstructor
@RestController                       
@RequestMapping("/rest/prodotto")      
public class ProdottoController {
	private final IProdottoServices prodS;

	// GET /rest/prodotto/list - restituisce i prodotti filtrati per categoria/marca/nome (parametri opzionali), accessibile a chiunque
	@GetMapping("/list")
	public ResponseEntity<Object> list(
			@RequestParam(required = false) Integer idCategoria,
			@RequestParam(required = false) String marca,
			@RequestParam(required = false) String nome
			) throws Exception {
		return ResponseEntity.ok(prodS.list(idCategoria, marca, nome));
	}

	// GET /rest/prodotto/getById?id=... - restituisce un prodotto, accessibile a chiunque
	@GetMapping("getById")
	public ResponseEntity<Object> getById(@RequestParam(required = true) Integer id) throws Exception {
		return ResponseEntity.ok(prodS.getById(id));
	}

	// GET /rest/prodotto/inEvidenza - i prodotti piu' venduti, per la home, accessibile a chiunque
	@GetMapping("inEvidenza")
	public ResponseEntity<Object> inEvidenza() throws Exception {
		return ResponseEntity.ok(prodS.selectInEvidenza());
	}

	// GET /rest/prodotto/novita - gli ultimi prodotti aggiunti, per la home, accessibile a chiunque
	@GetMapping("novita")
	public ResponseEntity<Object> novita() throws Exception {
		return ResponseEntity.ok(prodS.selectNovita());
	}

	// GET /rest/prodotto/nuovamenteDisponibili - prodotti tornati disponibili dopo essere stati esauriti, per la home, accessibile a chiunque
	@GetMapping("nuovamenteDisponibili")
	public ResponseEntity<Object> nuovamenteDisponibili() throws Exception {
		return ResponseEntity.ok(prodS.selectNuovamenteDisponibili());
	}

	// POST /rest/prodotto/create - crea un prodotto, solo per utenti ADMIN
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("create")
	public ResponseEntity<ResponseDTO> create(
			@RequestBody(required = true) @Validated(ValidationGroups.Create.class) ProdottoReq req) throws Exception {
		prodS.create(req);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("created...")
				.build());
	}

	// PATCH /rest/prodotto/update - modifica un prodotto, solo per utenti ADMIN
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("update")
	public ResponseEntity<ResponseDTO> update(
			@RequestBody(required = true) @Validated(ValidationGroups.Update.class) ProdottoReq req) throws Exception {
		prodS.update(req);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("updated...")
				.build());
	}

	// DELETE /rest/prodotto/delete/{id} - elimina un prodotto, solo per utenti ADMIN
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("delete/{id}")
	public ResponseEntity<ResponseDTO> delete(@PathVariable(required = true) Integer id) throws Exception {
		prodS.delete(id);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("deleted...")
				.build());
	}
}
