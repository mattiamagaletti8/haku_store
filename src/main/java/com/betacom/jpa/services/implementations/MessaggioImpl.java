package com.betacom.jpa.services.implementations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.betacom.jpa.models.MessageID;
import com.betacom.jpa.models.Messaggi;
import com.betacom.jpa.repositories.IMessagiRepository;
import com.betacom.jpa.services.interfaces.IMessaggioServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// ============================================================================
// PROPRIETARIO: Infrastruttura condivisa (non appartiene a una sola persona)
// ============================================================================
@RequiredArgsConstructor
@Slf4j
@Service
public class MessaggioImpl implements IMessaggioServices{

	private final IMessagiRepository msgR;

	// Lingua fissa dell'applicazione, letta da application.properties (lang=IT):
	// il sistema non gestisce multi-lingua dinamico per utente, una sola lingua per l'intero backend
	@Value("${lang}")
	private String lang;

	@Override
	public String get(String code) {
		log.debug("get {}", code);
		String r = null;
		// Cerca la traduzione per (lingua corrente, codice) usando la chiave composita
		Optional<Messaggi> m = msgR.findById(new MessageID(lang, code));
		if (m.isEmpty())
			// Fallback fondamentale: se non esiste una traduzione per questo codice, restituisce
			// il codice stesso invece di lanciare un errore o restituire null — cosi' anche un codice
			// dimenticato in messaggi-init.sql produce comunque una risposta leggibile (il codice grezzo)
			r = code;
		else
			r = m.get().getMessagio();


		return r;
	}

}
