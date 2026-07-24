package com.betacom.jpa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.betacom.jpa.dto.output.ResponseDTO;
import com.betacom.jpa.services.interfaces.ILogoServices;

import lombok.RequiredArgsConstructor;

// Proprietario: Infrastruttura condivisa
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/logo")
public class LogoController {
	private final ILogoServices logoS;

	// GET /rest/logo - restituisce l'URL del logo attuale (o null se non e' mai stato caricato), accessibile a chiunque
	@GetMapping
	public ResponseEntity<ResponseDTO> getUrl() throws Exception {
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg(logoS.getUrl())
				.build());
	}

	// POST /rest/logo - carica/sostituisce il logo del sito, solo per utenti ADMIN
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ResponseDTO> upload(@RequestParam("file") MultipartFile file) throws Exception {
		logoS.upload(file);
		return ResponseEntity.ok(ResponseDTO.builder()
				.msg("logo caricato...")
				.build());
	}
}
