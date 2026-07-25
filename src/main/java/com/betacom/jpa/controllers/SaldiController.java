package com.betacom.jpa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.jpa.services.interfaces.ISaldiServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Proprietario: Infrastruttura condivisa (non appartiene a una sola persona)
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/saldi")
public class SaldiController {

	private final ISaldiServices saldiS;

	// GET /rest/saldi/stato - se ci sono saldi in corso (e fino a quando) o, se non attivi,
	// quando saranno i prossimi: accessibile a chiunque, usato dalla topbar del sito
	@GetMapping("stato")
	public ResponseEntity<Object> stato() throws Exception {
		return ResponseEntity.ok(saldiS.getStato());
	}
}
