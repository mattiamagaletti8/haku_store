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

// Proprietario: Infrastruttura condivisa
@RequiredArgsConstructor
@Slf4j
@Service
public class MessaggioImpl implements IMessaggioServices{

	private final IMessagiRepository msgR;

	@Value("${lang}")
	private String lang;

	@Override
	public String get(String code) {
		log.debug("get {}", code);
		String r = null;
		Optional<Messaggi> m = msgR.findById(new MessageID(lang, code));
		if (m.isEmpty())
			r = code;
		else
			r = m.get().getMessagio();

		return r;
	}

}
