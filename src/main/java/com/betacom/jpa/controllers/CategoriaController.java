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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
@Slf4j
@RequiredArgsConstructor
// Fa si' che ogni metodo restituisca direttamente JSON (niente viste HTML)
@RestController
// Prefisso comune a tutti gli endpoint di questa classe: /rest/categoria/...
@RequestMapping("/rest/categoria")
public class CategoriaController {
	// Il controller non parla mai direttamente col database: passa sempre dal servizio
	private final ICategoriaServices catS;

	// GET pubblico (nessun controllo di ruolo): chiunque puo' vedere il catalogo, anche senza login
	@GetMapping("/list")
	public ResponseEntity<Object> list() throws Exception {
		return ResponseEntity.ok(catS.list());
	}

	// GET pubblico per singola categoria, tramite query param ?id=...
	@GetMapping("getById")
	public ResponseEntity<Object> getById(@RequestParam(required = true) Integer id) throws Exception {
		return ResponseEntity.ok(catS.getById(id));
	}

	// Solo utenti con ruolo ADMIN possono chiamare questo endpoint (controllato ANCHE prima che il metodo parta)
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("create")
	public ResponseEntity<ResponseDTO> create(
			// @Validated(ValidationGroups.Create.class): applica solo le regole di validazione del gruppo "Create"
			// definite dentro CategoriaReq (qui: nome obbligatorio)
			@RequestBody(required = true) @Validated(ValidationGroups.Create.class) CategoriaReq req) throws Exception {
		catS.create(req);
		// Ogni endpoint di mutazione risponde con lo stesso identico formato {"msg": "..."}
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("created...")
				.build());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("update")
	public ResponseEntity<ResponseDTO> update(
			// Qui invece il gruppo e' "Update": stesso CategoriaReq, regole di validazione diverse (id obbligatorio)
			@RequestBody(required = true) @Validated(ValidationGroups.Update.class) CategoriaReq req) throws Exception {
		catS.update(req);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("updated...")
				.build());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("delete/{id}")
	public ResponseEntity<ResponseDTO> delete(@PathVariable(required = true) Integer id) throws Exception {
		catS.delete(id);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("deleted...")
				.build());
	}
}
