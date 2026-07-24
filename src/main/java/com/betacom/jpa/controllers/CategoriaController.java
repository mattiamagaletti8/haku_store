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

import com.betacom.jpa.dto.input.CategoriaReq;
import com.betacom.jpa.dto.input.ValidationGroups;
import com.betacom.jpa.dto.output.ResponseDTO;
import com.betacom.jpa.services.interfaces.ICategoriaServices;

import lombok.RequiredArgsConstructor; // genera il costruttore con il campo final (catS)
import lombok.extern.slf4j.Slf4j;      // abilita il logger, anche se qui non viene usato direttamente

// Proprietario: Mattia
@Slf4j
@RequiredArgsConstructor
@RestController                        
@RequestMapping("/rest/categoria")      
public class CategoriaController {
	private final ICategoriaServices catS;

	// GET /rest/categoria/list - restituisce tutte le categorie, accessibile a chiunque
	@GetMapping("/list")
	public ResponseEntity<Object> list() throws Exception {
		return ResponseEntity.ok(catS.list());
	}

	// GET /rest/categoria/getById?id=... - restituisce una categoria, accessibile a chiunque
	@GetMapping("getById")
	public ResponseEntity<Object> getById(@RequestParam(required = true) Integer id) throws Exception {
		return ResponseEntity.ok(catS.getById(id));
	}

	// POST /rest/categoria/create - crea una categoria, solo per utenti ADMIN
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("create")
	public ResponseEntity<ResponseDTO> create(
			@RequestBody(required = true) @Validated(ValidationGroups.Create.class) CategoriaReq req) throws Exception {
		catS.create(req);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("created...")
				.build());
	}

	// PATCH /rest/categoria/update - modifica una categoria, solo per utenti ADMIN
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("update")
	public ResponseEntity<ResponseDTO> update(
			@RequestBody(required = true) @Validated(ValidationGroups.Update.class) CategoriaReq req) throws Exception {
		catS.update(req);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("updated...")
				.build());
	}

	// DELETE /rest/categoria/delete/{id} - elimina una categoria, solo per utenti ADMIN
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("delete/{id}")
	public ResponseEntity<ResponseDTO> delete(@PathVariable(required = true) Integer id) throws Exception {
		catS.delete(id);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("deleted...")
				.build());
	}

	// l'immagine della categoria si carica da UploadController (POST /upload/admin/image), non da qui
}
